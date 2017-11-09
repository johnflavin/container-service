package org.nrg.containers.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.nrg.config.entities.Configuration;
import org.nrg.config.exceptions.ConfigServiceException;
import org.nrg.config.services.ConfigService;
import org.nrg.containers.model.configuration.CommandConfigInternal;
import org.nrg.containers.services.ContainerConfigService;
import org.nrg.framework.constants.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.io.IOException;

@Service
@SuppressWarnings("deprecation")
public class ContainerConfigServiceImpl implements ContainerConfigService {
    private static final Logger log = LoggerFactory.getLogger(ContainerConfigService.class);

    private final ConfigService configService;
    private final ObjectMapper mapper;

    @Autowired
    public ContainerConfigServiceImpl(final ConfigService configService,
                                      final ObjectMapper mapper) {
        this.configService = configService;
        this.mapper = mapper;
    }

    @Override
    public long getDefaultDockerHubId() {
        final Configuration defaultDockerHubConfig = configService.getConfig(TOOL_ID, DEFAULT_DOCKER_HUB_PATH);
        long id = 0L;
        if (defaultDockerHubConfig != null) {
            final String contents = defaultDockerHubConfig.getContents();
            if (StringUtils.isNotBlank(contents)) {
                try {
                    id = Long.valueOf(contents);
                } catch (NumberFormatException ignored) {
                    // ignored
                }
            }
        }
        return id;
    }

    @Override
    public void setDefaultDockerHubId(final long hubId, final String username, final String reason) {
        try {
            configService.replaceConfig(username, reason, TOOL_ID, DEFAULT_DOCKER_HUB_PATH, String.valueOf(hubId));
        } catch (ConfigServiceException e) {
            log.error("Could not save default docker hub config.", e);
        }
    }

    @Override
    public void configureForProject(final CommandConfigInternal commandConfigInternal, final String project, final long wrapperId, final String username, final String reason) throws CommandConfigurationException {
        setCommandConfigurationInternal(commandConfigInternal, Scope.Project, project, wrapperId, username, reason);
    }

    @Override
    public void configureForSite(final CommandConfigInternal commandConfigInternal, final long wrapperId, final String username, final String reason) throws CommandConfigurationException {
        setCommandConfigurationInternal(commandConfigInternal, Scope.Site, null, wrapperId, username, reason);
    }

    @Override
    @Nullable
    public CommandConfigInternal getSiteConfiguration(final long wrapperId) {
        return getCommandConfiguration(Scope.Site, null, wrapperId);
    }

    @Override
    @Nullable
    public CommandConfigInternal getProjectConfiguration(final String project, final long wrapperId) {
        final CommandConfigInternal siteConfig = getSiteConfiguration(wrapperId);
        final CommandConfigInternal baseConfig = siteConfig != null ? siteConfig : CommandConfigInternal.builder().build();
        final CommandConfigInternal projectConfig = getCommandConfiguration(Scope.Project, project, wrapperId);
        return baseConfig.merge(projectConfig, isEnabledForProject(project, wrapperId));
    }

    @Override
    public void deleteSiteConfiguration(final long wrapperId, final String username) throws CommandConfigurationException {
        deleteCommandConfiguration(Scope.Site, null, wrapperId, username);
    }

    @Override
    public void deleteProjectConfiguration(final String project, final long wrapperId, final String username) throws CommandConfigurationException {
        deleteCommandConfiguration(Scope.Project, project, wrapperId, username);
    }

    @Override
    public void deleteAllConfiguration(final long wrapperId) {
        // TODO
    }

    @Override
    public void enableForSite(final long wrapperId, final String username, final String reason) throws CommandConfigurationException {
        setCommandEnabled(true, Scope.Site, null, wrapperId, username, reason);
    }

    @Override
    public void disableForSite(final long wrapperId, final String username, final String reason) throws CommandConfigurationException {
        setCommandEnabled(false, Scope.Site, null, wrapperId, username, reason);
    }

    @Override
    public boolean isEnabledForSite(final long wrapperId) {
        final Boolean isEnabledConfiguration = getCommandIsEnabledConfiguration(Scope.Site, null, wrapperId);
        return !Boolean.FALSE.equals(isEnabledConfiguration);
    }

    @Override
    public void enableForProject(final String project, final long wrapperId, final String username, final String reason) throws CommandConfigurationException {
        setCommandEnabled(true, Scope.Project, project, wrapperId, username, reason);
    }

    @Override
    public void disableForProject(final String project, final long wrapperId, final String username, final String reason) throws CommandConfigurationException {
        setCommandEnabled(false, Scope.Project, project, wrapperId, username, reason);
    }

    @Override
    public boolean isEnabledForProject(final String project, final long wrapperId) {
        final Boolean projectIsEnabledConfig = getCommandIsEnabledConfiguration(Scope.Project, project, wrapperId);

        // How to know if the command is enabled for a project:
        // If the command is disabled on the site, then the result is disabled
        // Else, if the site "enabled" is true or null, check the project "enabled".
        // If the project "enabled" is false, the result is disabled.
        // If the project "enabled" is true, then the result is enabled.
        return isEnabledForSite(wrapperId) && projectIsEnabledConfig != null && projectIsEnabledConfig;
    }

    private void setCommandEnabled(final Boolean enabled, final Scope scope, final String project, final long wrapperId, final String username, final String reason) throws CommandConfigurationException {
        final CommandConfigInternal alreadyExists = getCommandConfiguration(scope, project, wrapperId);
        final CommandConfigInternal toSet =
                (alreadyExists == null ? CommandConfigInternal.builder() : alreadyExists.toBuilder())
                        .enabled(enabled)
                        .build();
        setCommandConfigurationInternal(toSet, scope, project, wrapperId, username, reason);
    }

    private void setCommandConfigurationInternal(final CommandConfigInternal commandConfigInternal,
                                                 final Scope scope, final String project, final long wrapperId, final String username, final String reason) throws CommandConfigurationException {
        if (scope.equals(Scope.Project) && StringUtils.isBlank(project)) {
            // TODO error: project can't be blank
        }

        if (wrapperId == 0L) {
            // TODO error
        }

        String contents = "";
        try {
            contents = mapper.writeValueAsString(commandConfigInternal);
        } catch (JsonProcessingException e) {
            final String message = String.format("Could not save configuration for wrapper id %d.", wrapperId);
            log.error(message);
            throw new CommandConfigurationException(message, e);
        }

        final String path = String.format(WRAPPER_CONFIG_PATH_TEMPLATE, wrapperId);
        try {
            configService.replaceConfig(username, reason, TOOL_ID, path, contents, scope, project);
        } catch (ConfigServiceException e) {
            final String message = String.format("Could not save configuration for wrapper id %d.", wrapperId);
            log.error(message);
            throw new CommandConfigurationException(message, e);
        }
    }

    @Nullable
    private Boolean getCommandIsEnabledConfiguration(final Scope scope, final String project, final long wrapperId) {
        final CommandConfigInternal commandConfigInternal = getCommandConfiguration(scope, project, wrapperId);
        return commandConfigInternal == null ? null : commandConfigInternal.enabled();
    }

    @Nullable
    private CommandConfigInternal getCommandConfiguration(final Scope scope, final String project, final long wrapperId) {
        if (scope.equals(Scope.Project) && StringUtils.isBlank(project)) {
            // TODO error: project can't be blank
        }

        if (wrapperId == 0L) {
            // TODO error
        }

        final String path = String.format(WRAPPER_CONFIG_PATH_TEMPLATE, wrapperId);
        final Configuration configuration = configService.getConfig(TOOL_ID, path, scope, project);
        if (configuration == null) {
            return null;
        }

        final String configurationJson = configuration.getContents();
        if (StringUtils.isBlank(configurationJson)) {
            return null;
        }

        try {
            return mapper.readValue(configurationJson, CommandConfigInternal.class);
        } catch (IOException e) {
            final String message = String.format("Could not deserialize Command Configuration for %s, wrapper id %d.",
                    scope.equals(Scope.Site) ? "site" : "project " + project,
                    wrapperId);
            log.error(message, e);
            //throw new CommandConfigurationException(message, e);
        }
        return null;
    }

    private void deleteCommandConfiguration(final Scope scope, final String project, final long wrapperId, final String username) throws CommandConfigurationException {
        if (scope.equals(Scope.Project) && StringUtils.isBlank(project)) {
            // TODO error: project can't be blank
        }

        if (wrapperId == 0L) {
            // TODO error
        }

        final CommandConfigInternal commandConfigInternal = getCommandConfiguration(scope, project, wrapperId);
        if (commandConfigInternal == null) {
            return;
        }
        if (commandConfigInternal.enabled() == null) {
            final String path = String.format(WRAPPER_CONFIG_PATH_TEMPLATE, wrapperId);
            configService.delete(configService.getConfig(TOOL_ID, path, scope, project));
            return;
        }

        setCommandConfigurationInternal(CommandConfigInternal.create(commandConfigInternal.enabled(), null),
                scope, project, wrapperId, username, "Deleting command configuration");
    }
}
