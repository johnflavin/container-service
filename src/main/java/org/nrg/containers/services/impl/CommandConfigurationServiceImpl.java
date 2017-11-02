package org.nrg.containers.services.impl;

import org.nrg.containers.model.configuration.CommandConfiguration;
import org.nrg.containers.model.configuration.CommandConfigurationEntity;
import org.nrg.containers.services.CommandConfigurationEntityService;
import org.nrg.containers.services.CommandConfigurationService;
import org.nrg.containers.services.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommandConfigurationServiceImpl implements CommandConfigurationService {
    private final CommandService commandService;
    private final CommandConfigurationEntityService commandConfigurationEntityService;

    @Autowired
    public CommandConfigurationServiceImpl(final CommandService commandService,
                                           final CommandConfigurationEntityService commandConfigurationEntityService) {
        this.commandService = commandService;
        this.commandConfigurationEntityService = commandConfigurationEntityService;
    }

    @Override
    public CommandConfiguration get(final long wrapperId, final String project) {
        return null;
    }

    @Override
    public CommandConfiguration get(final long commandId, final long wrapperId, final String project) {
        return null;
    }

    @Override
    public CommandConfiguration get(final long commandId, final String wrapperName, final String project) {
        return null;
    }

    @Override
    public void configure(final CommandConfiguration commandConfiguration, final long wrapperId, final String project) {

    }

    @Override
    public void configure(final CommandConfiguration commandConfiguration, final long commandId, final long wrapperId, final String project) {

    }

    @Override
    public void configure(final CommandConfiguration commandConfiguration, final long commandId, final String wrapperName, final String project) {

    }

    @Override
    public Boolean isEnabled(final long wrapperId, final String project) {
        return null;
    }

    @Override
    public Boolean isEnabled(final long commandId, final long wrapperId, final String project) {
        return null;
    }

    @Override
    public Boolean isEnabled(final long commandId, final String wrapperName, final String project) {
        return null;
    }

    @Override
    public void enable(final long wrapperId, final String project) {

    }

    @Override
    public void enable(final long commandId, final long wrapperId, final String project) {

    }

    @Override
    public void enable(final long commandId, final String wrapperName, final String project) {

    }

    @Override
    public void disable(final long wrapperId, final String project) {

    }

    @Override
    public void disable(final long commandId, final long wrapperId, final String project) {

    }

    @Override
    public void disable(final long commandId, final String wrapperName, final String project) {

    }

    @Override
    public void setEnabled(final Boolean enabled, final long wrapperId, final String project) {

    }

    @Override
    public void setEnabled(final Boolean enabled, final long commandId, final long wrapperId, final String project) {

    }

    @Override
    public void setEnabled(final Boolean enabled, final long commandId, final String wrapperName, final String project) {

    }

    @Override
    public void delete(final long wrapperId, final String project) {

    }

    @Override
    public void delete(final long commandId, final long wrapperId, final String project) {

    }

    @Override
    public void delete(final long commandId, final String wrapperName, final String project) {

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

    private CommandConfiguration toPojo(final CommandConfigurationEntity commandConfigurationEntity) {

    }
}
