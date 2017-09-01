package org.nrg.containers.services;

import org.nrg.containers.model.configuration.CommandConfiguration;
import org.nrg.containers.model.configuration.Scope;

public interface CommandConfigurationService {
    CommandConfiguration get(long wrapperId, Scope scope, String scopeEntityId);
    CommandConfiguration get(long commandId, long wrapperId, Scope scope, String scopeEntityId);
    CommandConfiguration get(long commandId, String wrapperName, Scope scope, String scopeEntityId);

    void configure(CommandConfiguration commandConfiguration, long wrapperId, Scope scope, String scopeEntityId);
    void configure(CommandConfiguration commandConfiguration, long commandId, long wrapperId, Scope scope, String scopeEntityId);
    void configure(CommandConfiguration commandConfiguration, long commandId, String wrapperName, Scope scope, String scopeEntityId);

    Boolean isEnabled(long wrapperId, Scope scope, String scopeEntityId);
    Boolean isEnabled(long commandId, long wrapperId, Scope scope, String scopeEntityId);
    Boolean isEnabled(long commandId, String wrapperName, Scope scope, String scopeEntityId);

    void enable(long wrapperId, Scope scope, String scopeEntityId);
    void enable(long commandId, long wrapperId, Scope scope, String scopeEntityId);
    void enable(long commandId, String wrapperName, Scope scope, String scopeEntityId);
    void disable(long wrapperId, Scope scope, String scopeEntityId);
    void disable(long commandId, long wrapperId, Scope scope, String scopeEntityId);
    void disable(long commandId, String wrapperName, Scope scope, String scopeEntityId);
    void setEnabled(Boolean enabled, long wrapperId, Scope scope, String scopeEntityId);
    void setEnabled(Boolean enabled, long commandId, long wrapperId, Scope scope, String scopeEntityId);
    void setEnabled(Boolean enabled, long commandId, String wrapperName, Scope scope, String scopeEntityId);

    void delete(long wrapperId, Scope scope, String scopeEntityId);
    void delete(long commandId, long wrapperId, Scope scope, String scopeEntityId);
    void delete(long commandId, String wrapperName, Scope scope, String scopeEntityId);
    void deleteAllForCommand(long commandId);
    void deleteAllForWrapper(long wrapperId);
    void deleteAllForWrapper(long commandId, String wrapperName);
}
