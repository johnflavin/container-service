package org.nrg.containers.services.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.nrg.containers.model.configuration.CommandConfiguration;
import org.nrg.containers.model.configuration.CommandConfigurationEntity;
import org.nrg.containers.services.CommandConfigurationEntityService;
import org.nrg.containers.services.CommandConfigurationService;
import org.nrg.framework.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommandConfigurationServiceImpl implements CommandConfigurationService {
    private final CommandConfigurationEntityService commandConfigurationEntityService;

    @Autowired
    public CommandConfigurationServiceImpl(final CommandConfigurationEntityService commandConfigurationEntityService) {
        this.commandConfigurationEntityService = commandConfigurationEntityService;
    }

    @Override
    public CommandConfiguration get(final long wrapperId, final String project) throws NotFoundException {
        return toPojo(commandConfigurationEntityService.get(wrapperId, project));
    }


    @Override
    public void configure(final CommandConfiguration commandConfiguration, final long wrapperId, final String project) {
        // todo
    }

    @Override
    public boolean isEnabled(final long wrapperId, final String project) throws NotFoundException {
        return commandConfigurationEntityService.isEnabled(wrapperId, project);
    }

    @Override
    public void enable(final long wrapperId, final String project) throws NotFoundException {
        commandConfigurationEntityService.enable(wrapperId, project);
    }

    @Override
    public void disable(final long wrapperId, final String project) throws NotFoundException {
        commandConfigurationEntityService.disable(wrapperId, project);
    }

    @Override
    public void setEnabled(final boolean enabled, final long wrapperId, final String project) throws NotFoundException {
        commandConfigurationEntityService.setEnabled(enabled, wrapperId, project);
    }

    @Override
    public void delete(final long wrapperId, final String project) throws NotFoundException {
        commandConfigurationEntityService.delete(wrapperId, project);
    }

    @Override
    public void deleteAllForWrapper(final long wrapperId) {
        commandConfigurationEntityService.deleteAllForWrapper(wrapperId);
    }

    private CommandConfiguration toPojo(final CommandConfigurationEntity commandConfigurationEntity) {
        return CommandConfiguration.create(commandConfigurationEntity);
    }

    private List<CommandConfiguration> toPojo(final List<CommandConfigurationEntity> commandConfigurationEntities) {
        final List<CommandConfiguration> toReturn = new ArrayList<>();
        if (commandConfigurationEntities != null) {
            toReturn.addAll(Lists.transform(commandConfigurationEntities, new Function<CommandConfigurationEntity, CommandConfiguration>() {
                @Override
                public CommandConfiguration apply(final CommandConfigurationEntity input) {
                    return toPojo(input);
                }
            }));
        }
        return toReturn;
    }

    private CommandConfigurationEntity fromPojo(final CommandConfiguration commandConfiguration) {
        final CommandConfigurationEntity template = commandConfigurationEntityService.retrieve(commandConfiguration.id());
        return template == null ? CommandConfigurationEntity.fromPojo(commandConfiguration) : template.update(commandConfiguration);
    }
}
