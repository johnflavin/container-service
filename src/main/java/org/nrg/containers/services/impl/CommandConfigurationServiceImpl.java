package org.nrg.containers.services.impl;

import org.nrg.containers.model.configuration.CommandConfiguration;
import org.nrg.containers.services.CommandConfigurationService;
import org.springframework.stereotype.Service;

@Service
public class CommandConfigurationServiceImpl implements CommandConfigurationService {

    @Override
    public CommandConfiguration get(final long wrapperId, final Scope scope, final String scopeEntityId) {
        return null;
    }

    @Override
    public CommandConfiguration get(final long commandId, final long wrapperId, final Scope scope, final String scopeEntityId) {
        return null;
    }

    @Override
    public CommandConfiguration get(final long commandId, final String wrapperName, final Scope scope, final String scopeEntityId) {
        return null;
    }

    @Override
    public void configure(final CommandConfiguration commandConfiguration, final long wrapperId, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public void configure(final CommandConfiguration commandConfiguration, final long commandId, final long wrapperId, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public void configure(final CommandConfiguration commandConfiguration, final long commandId, final String wrapperName, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public Boolean isEnabled(final long wrapperId, final Scope scope, final String scopeEntityId) {
        return null;
    }

    @Override
    public Boolean isEnabled(final long commandId, final long wrapperId, final Scope scope, final String scopeEntityId) {
        return null;
    }

    @Override
    public Boolean isEnabled(final long commandId, final String wrapperName, final Scope scope, final String scopeEntityId) {
        return null;
    }

    @Override
    public void enable(final long wrapperId, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public void enable(final long commandId, final long wrapperId, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public void enable(final long commandId, final String wrapperName, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public void disable(final long wrapperId, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public void disable(final long commandId, final long wrapperId, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public void disable(final long commandId, final String wrapperName, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public void setEnabled(final Boolean enabled, final long wrapperId, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public void setEnabled(final Boolean enabled, final long commandId, final long wrapperId, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public void setEnabled(final Boolean enabled, final long commandId, final String wrapperName, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public void delete(final long wrapperId, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public void delete(final long commandId, final long wrapperId, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public void delete(final long commandId, final String wrapperName, final Scope scope, final String scopeEntityId) {

    }

    @Override
    public void deleteAllForCommand(final long commandId) {

    }

    @Override
    public void deleteAllForWrapper(final long wrapperId) {

    }

    @Override
    public void deleteAllForWrapper(final long commandId, final String wrapperName) {

    }
}
