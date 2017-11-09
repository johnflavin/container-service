package org.nrg.containers.services;

import org.nrg.containers.model.configuration.CommandConfigInternal;

public interface ContainerConfigService {
    String TOOL_ID = "container-service";
    String DEFAULT_DOCKER_HUB_PATH = "default-docker-hub-id";
    String WRAPPER_CONFIG_PATH_TEMPLATE = "wrapper-%d";

    long getDefaultDockerHubId();
    void setDefaultDockerHubId(long hubId, String username, String reason);

    void configureForSite(CommandConfigInternal commandConfiguration, long wrapperId, String username, String reason) throws CommandConfigurationException;
    void configureForProject(CommandConfigInternal commandConfiguration, String project, long wrapperId, String username, String reason) throws CommandConfigurationException;

    CommandConfigInternal getSiteConfiguration(long wrapperId);
    CommandConfigInternal getProjectConfiguration(String project, long wrapperId);

    void deleteSiteConfiguration(long commandId, final String username) throws CommandConfigurationException;
    void deleteProjectConfiguration(String project, long wrapperId, final String username) throws CommandConfigurationException;
    void deleteAllConfiguration(long wrapperId);

    void enableForSite(long wrapperId, final String username, final String reason) throws CommandConfigurationException;

    void disableForSite(long wrapperId, final String username, final String reason) throws CommandConfigurationException;
    boolean isEnabledForSite(long wrapperId);
    void enableForProject(String project, long wrapperId, final String username, final String reason) throws CommandConfigurationException;
    void disableForProject(String project, long wrapperId, final String username, final String reason) throws CommandConfigurationException;
    boolean isEnabledForProject(String project, long wrapperId);

    class CommandConfigurationException extends Exception {
        public CommandConfigurationException(final String message, final Throwable e) {
            super(message, e);
        }
    }
}
