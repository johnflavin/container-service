package org.nrg.containers.services;

import org.nrg.containers.model.configuration.CommandConfigurationEntity;
import org.nrg.containers.model.configuration.Scope;
import org.nrg.framework.exceptions.NotFoundException;
import org.nrg.framework.orm.hibernate.BaseHibernateService;

public interface CommandConfigurationEntityService extends BaseHibernateService<CommandConfigurationEntity> {
    CommandConfigurationEntity get(long wrapperId, Scope scope, String scopeEntityId) throws NotFoundException;

    Boolean isEnabled(long wrapperId, Scope scope, String scopeEntityId) throws NotFoundException;

    void enable(long wrapperId, Scope scope, String scopeEntityId) throws NotFoundException;
    void disable(long wrapperId, Scope scope, String scopeEntityId);
    void setEnabled(Boolean enabled, long wrapperId, Scope scope, String scopeEntityId);

    void delete(long wrapperId, Scope scope, String scopeEntityId);
    void deleteAllForCommand(long commandId);
    void deleteAllForWrapper(long wrapperId);
}
