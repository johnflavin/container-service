package org.nrg.containers.services;

import org.nrg.containers.model.configuration.CommandConfigurationEntity;
import org.nrg.framework.exceptions.NotFoundException;
import org.nrg.framework.orm.hibernate.BaseHibernateService;

public interface CommandConfigurationEntityService extends BaseHibernateService<CommandConfigurationEntity> {
    CommandConfigurationEntity get(long wrapperId, String project) throws NotFoundException;

    Boolean isEnabled(long wrapperId, String project) throws NotFoundException;

    void enable(long wrapperId, String project) throws NotFoundException;
    void disable(long wrapperId, String project);
    void setEnabled(Boolean enabled, long wrapperId, String project);

    void delete(long wrapperId, String project);
    void deleteAllForCommand(long commandId);
    void deleteAllForWrapper(long wrapperId);
}
