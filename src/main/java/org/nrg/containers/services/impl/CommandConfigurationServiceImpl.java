package org.nrg.containers.services.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.nrg.containers.model.configuration.CommandConfigInternal;
import org.nrg.containers.model.configuration.CommandConfiguration;
import org.nrg.containers.model.configuration.CommandConfigurationEntity;
import org.nrg.containers.services.CommandConfigurationEntityService;
import org.nrg.containers.services.CommandConfigurationService;
import org.nrg.containers.services.ContainerConfigService;
import org.nrg.framework.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommandConfigurationServiceImpl implements CommandConfigurationService {
    private final CommandConfigurationEntityService commandConfigurationEntityService;
    private final ContainerConfigService containerConfigService;

    @Autowired
    public CommandConfigurationServiceImpl(final CommandConfigurationEntityService commandConfigurationEntityService,
                                           final ContainerConfigService containerConfigService) {
        this.commandConfigurationEntityService = commandConfigurationEntityService;
        this.containerConfigService = containerConfigService;
    }

    @Override
    public CommandConfiguration retrieve(final long configurationId) {
        return toPojo(commandConfigurationEntityService.retrieve(configurationId));
    }

    @Override
    public CommandConfiguration retrieve(final long wrapperId, final String project) {
        final CommandConfigurationEntity fromDb = commandConfigurationEntityService.retrieve(wrapperId, project);
        if (fromDb != null) {
            return toPojo(fromDb);
        }

        // If we did not find a configuration in the database, check the legacy service
        // TODO Get project config by itself (current methods merge site and project configs before returning, so we need to make a method that returns just project config)
        final CommandConfigInternal legacyCommandConfig = containerConfigService.getProjectConfiguration(project, wrapperId);

        if (legacyCommandConfig != null) {
            return create(CommandConfiguration.create(legacyCommandConfig, wrapperId, project));
        }

        return null;
    }

    @Override
    public CommandConfiguration get(final long configurationId) throws NotFoundException {
        return toPojo(commandConfigurationEntityService.get(configurationId));
    }

    @Override
    @Nonnull
    public CommandConfiguration get(final long wrapperId, final String project) throws NotFoundException {
        final CommandConfiguration commandConfiguration = retrieve(wrapperId, project);
        if (commandConfiguration != null) {
            return commandConfiguration;
        }
        throw new NotFoundException(String.format("No configuration for wrapperId %d, project %s.", wrapperId, project));
    }

    @Override
    @Nonnull
    public CommandConfiguration create(final CommandConfiguration commandConfiguration) {
        return toPojo(commandConfigurationEntityService.create(fromPojo(commandConfiguration)));
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
    public void delete(final long configurationId) throws NotFoundException {
        commandConfigurationEntityService.delete(configurationId);
    }

    @Override
    public void delete(final long wrapperId, final String project) throws NotFoundException {
        commandConfigurationEntityService.delete(wrapperId, project);
    }

    @Override
    public void deleteAllForWrapper(final long wrapperId) {
        commandConfigurationEntityService.deleteAllForWrapper(wrapperId);
    }

    @Nonnull
    private CommandConfiguration toPojo(final @Nonnull CommandConfigurationEntity commandConfigurationEntity) {
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
