package org.nrg.containers.services;

import org.nrg.containers.model.configuration.CommandConfigurationEntity;
import org.nrg.framework.exceptions.NotFoundException;
import org.nrg.framework.orm.hibernate.BaseHibernateService;

public interface CommandConfigurationEntityService extends BaseHibernateService<CommandConfigurationEntity> {
    CommandConfigurationEntity retrieve(long wrapperId, String project);
    CommandConfigurationEntity get(long wrapperId, String project) throws NotFoundException;

    boolean isEnabled(long wrapperId, String project) throws NotFoundException;

    void enable(long wrapperId, String project) throws NotFoundException;
    void disable(long wrapperId, String project) throws NotFoundException;
    void setEnabled(boolean enabled, long wrapperId, String project) throws NotFoundException;

    void delete(long wrapperId, String project) throws NotFoundException;
    void deleteAllForWrapper(long wrapperId);
}
