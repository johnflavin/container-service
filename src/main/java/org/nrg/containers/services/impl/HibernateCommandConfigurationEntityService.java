package org.nrg.containers.services.impl;

import com.google.common.collect.ImmutableMap;
import org.nrg.containers.daos.CommandConfigurationRepository;
import org.nrg.containers.model.configuration.CommandConfigurationEntity;
import org.nrg.containers.services.CommandConfigurationEntityService;
import org.nrg.framework.exceptions.NotFoundException;
import org.nrg.framework.orm.hibernate.AbstractHibernateEntityService;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Service
public class HibernateCommandConfigurationEntityService
        extends AbstractHibernateEntityService<CommandConfigurationEntity, CommandConfigurationRepository>
        implements CommandConfigurationEntityService {

    @Override
    @Nullable
    public CommandConfigurationEntity retrieve(final long wrapperId, final String project) {
        final List<CommandConfigurationEntity> found = getDao().findByProperties(ImmutableMap.<String, Object>of("wrapperId", wrapperId, "project", project));
        return found.size() == 1 ? found.get(0) : null;
    }

    @Override
    @Nonnull
    public CommandConfigurationEntity get(final long wrapperId, final String project) throws NotFoundException {
        final CommandConfigurationEntity entity = retrieve(wrapperId, project);
        if (entity == null) {
            throw new NotFoundException(String.format("Could not find unique configuration for wrapperId %d for %s", wrapperId, project == null ? "site." : "project " + project + "."));
        }
        return entity;
    }

    @Override
    public boolean isEnabled(final long wrapperId, final String project) throws NotFoundException {
        final CommandConfigurationEntity entity = get(wrapperId, project);
        return entity.isEnabled();
    }

    @Override
    public void enable(final long wrapperId, final String project) throws NotFoundException {
        setEnabled(true, wrapperId, project);
    }

    @Override
    public void disable(final long wrapperId, final String project) throws NotFoundException {
        setEnabled(false, wrapperId, project);
    }

    @Override
    public void setEnabled(final boolean enabled, final long wrapperId, final String project) throws NotFoundException {
        final CommandConfigurationEntity entity = get(wrapperId, project);
        entity.setEnabled(enabled);
        update(entity);
    }

    @Override
    public void delete(final long wrapperId, final String project) throws NotFoundException {
        delete(get(wrapperId, project));
    }

    @Override
    public void deleteAllForWrapper(final long wrapperId) {
        for (final CommandConfigurationEntity configurationForWrapper : getDao().findByProperties(ImmutableMap.<String, Object>of("wrapperId", wrapperId))) {
            delete(configurationForWrapper);
        }
    }
}
