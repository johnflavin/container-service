package org.nrg.containers.services.impl;

import org.nrg.containers.daos.CommandConfigurationRepository;
import org.nrg.containers.model.command.entity.CommandEntity;
import org.nrg.containers.model.configuration.CommandConfigurationEntity;
import org.nrg.containers.model.configuration.Scope;
import org.nrg.containers.services.CommandConfigurationEntityService;
import org.nrg.containers.services.CommandEntityService;
import org.nrg.framework.exceptions.NotFoundException;
import org.nrg.framework.orm.hibernate.AbstractHibernateEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HibernateCommandConfigurationEntityService
        extends AbstractHibernateEntityService<CommandConfigurationEntity, CommandConfigurationRepository>
        implements CommandConfigurationEntityService {

    @Override
    public CommandConfigurationEntity get(final long wrapperId, final Scope scope, final String scopeEntityId) throws NotFoundException {
        return null;
    }

    @Override
    public Boolean isEnabled(final long wrapperId, final Scope scope, final String scopeEntityId) throws NotFoundException {
        return null;
    }

    @Override
    public void enable(final long wrapperId, final Scope scope, final String scopeEntityId) throws NotFoundException {

    }

    @Override
    public void disable(final long wrapperId, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public void setEnabled(final Boolean enabled, final long wrapperId, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public void delete(final long wrapperId, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public void deleteAllForCommand(final long commandId) {

    }

    @Override
    public void deleteAllForWrapper(final long wrapperId) {

    }
}
