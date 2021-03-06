package org.nrg.containers.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.nrg.config.entities.Configuration;
import org.nrg.config.exceptions.ConfigServiceException;
import org.nrg.config.services.ConfigService;
import org.nrg.containers.model.configuration.CommandConfigurationInternal;
import org.nrg.containers.model.settings.ContainerServiceSettings;
import org.nrg.containers.services.ContainerConfigService;
import org.nrg.framework.constants.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

@Service
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
    public void configureForProject(final CommandConfigurationInternal commandConfigurationInternal, final String project, final long wrapperId, final String username, final String reason) throws CommandConfigurationException {
        setCommandConfigurationInternal(commandConfigurationInternal, Scope.Project, project, wrapperId, username, reason);
    }

    @Override
    public void configureForSite(final CommandConfigurationInternal commandConfigurationInternal, final long wrapperId, final String username, final String reason) throws CommandConfigurationException {
        setCommandConfigurationInternal(commandConfigurationInternal, Scope.Site, null, wrapperId, username, reason);
    }

    @Override
    @Nullable
    public CommandConfigurationInternal getSiteConfiguration(final long wrapperId) {
        return getCommandConfiguration(Scope.Site, null, wrapperId);
    }

    @Override
    @Nullable
    public CommandConfigurationInternal getProjectConfiguration(final String project, final long wrapperId) {
        final CommandConfigurationInternal siteConfig = getSiteConfiguration(wrapperId);
        final CommandConfigurationInternal baseConfig = siteConfig != null ? siteConfig : CommandConfigurationInternal.builder().build();
        final CommandConfigurationInternal projectConfig = getCommandConfiguration(Scope.Project, project, wrapperId);
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
    @Nonnull
    public ContainerServiceSettings getSettings() {
        return ContainerServiceSettings.create(getOptInToSiteCommands());
    }

    @Override
    @Nonnull
    public ContainerServiceSettings getSettings(final String project) {
        return ContainerServiceSettings.create(getOptInToSiteCommands(project));
    }

    @Override
    public Boolean getOptInToSiteCommands() {
        final Boolean setting = parseBooleanConfig(configService.getConfig(TOOL_ID, OPT_IN_PATH, Scope.Site, null));
        return setting == null ? OPT_IN_DEFAULT_VALUE : setting;
    }

    @Override
    public void setOptInToSiteCommands(final String username, final String reason) throws ConfigServiceException {
        setOptInToSiteCommands(true, username, reason);
    }

    @Override
    public void setOptOutOfSiteCommands(final String username, final String reason) throws ConfigServiceException {
        setOptInToSiteCommands(false, username, reason);
    }

    @Override
    public void deleteOptInToSiteCommands(final String username, final String reason) throws ConfigServiceException {
        setOptInToSiteCommands(OPT_IN_DEFAULT_VALUE, username, reason);
    }

    private void setOptInToSiteCommands(final boolean optInDefault, final String username, final String reason) throws ConfigServiceException {
        configService.replaceConfig(username, reason,
                TOOL_ID, OPT_IN_PATH,
                String.valueOf(optInDefault),
                Scope.Site, null);
    }

    @Override
    public Boolean getOptInToSiteCommands(final String project) {
        final Boolean projectSetting = parseBooleanConfig(configService.getConfig(TOOL_ID, OPT_IN_PATH, Scope.Project, project));
        return projectSetting == null ? getOptInToSiteCommands() : projectSetting;
    }

    @Override
    public void optInToSiteCommands(final String project, final String username, final String reason) throws ConfigServiceException {
        setOptInToSiteCommands(true, project, username, reason);
    }

    @Override
    public void optOutOfSiteCommands(final String project, final String username, final String reason) throws ConfigServiceException {
        setOptInToSiteCommands(false, project, username, reason);
    }

    @Override
    public void deleteOptInToSiteCommandsSetting(final String project, final String username, final String reason) throws ConfigServiceException {
        setOptInToSiteCommands(getOptInToSiteCommands(), project, username, reason);
    }

    private void setOptInToSiteCommands(final boolean optIn, final String project, final String username, final String reason) throws ConfigServiceException {
        configService.replaceConfig(username, reason,
                TOOL_ID, OPT_IN_PATH,
                String.valueOf(optIn),
                Scope.Project, project);
    }

    @Nullable
    private Boolean parseBooleanConfig(final @Nullable Configuration configuration) {
        return (configuration == null || configuration.getContents() == null) ? null : Boolean.parseBoolean(configuration.getContents());
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
        // If the project "enabled" is null, then check the project "opt in": do we enable by default or not?
        return isEnabledForSite(wrapperId) &&
                (projectIsEnabledConfig != null ? projectIsEnabledConfig : getOptInToSiteCommands(project));
    }

    private void setCommandEnabled(final Boolean enabled, final Scope scope, final String project, final long wrapperId, final String username, final String reason) throws CommandConfigurationException {
        final CommandConfigurationInternal alreadyExists = getCommandConfiguration(scope, project, wrapperId);
        final CommandConfigurationInternal toSet =
                (alreadyExists == null ? CommandConfigurationInternal.builder() : alreadyExists.toBuilder())
                        .enabled(enabled)
                        .build();
        setCommandConfigurationInternal(toSet, scope, project, wrapperId, username, reason);
    }

    private void setCommandConfigurationInternal(final CommandConfigurationInternal commandConfigurationInternal,
                                                 final Scope scope, final String project, final long wrapperId, final String username, final String reason) throws CommandConfigurationException {
        if (scope.equals(Scope.Project) && StringUtils.isBlank(project)) {
            // TODO error: project can't be blank
        }

        if (wrapperId == 0L) {
            // TODO error
        }

        String contents = "";
        try {
            contents = mapper.writeValueAsString(commandConfigurationInternal);
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
        final CommandConfigurationInternal commandConfigurationInternal = getCommandConfiguration(scope, project, wrapperId);
        return commandConfigurationInternal == null ? null : commandConfigurationInternal.enabled();
    }

    @Nullable
    private CommandConfigurationInternal getCommandConfiguration(final Scope scope, final String project, final long wrapperId) {
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
            return mapper.readValue(configurationJson, CommandConfigurationInternal.class);
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

        final CommandConfigurationInternal commandConfigurationInternal = getCommandConfiguration(scope, project, wrapperId);
        if (commandConfigurationInternal == null) {
            return;
        }
        if (commandConfigurationInternal.enabled() == null) {
            final String path = String.format(WRAPPER_CONFIG_PATH_TEMPLATE, wrapperId);
            configService.delete(configService.getConfig(TOOL_ID, path, scope, project));
            return;
        }

        setCommandConfigurationInternal(CommandConfigurationInternal.create(commandConfigurationInternal.enabled(), null),
                scope, project, wrapperId, username, "Deleting command configuration");
    }
}
