package org.nrg.containers.services;

import org.nrg.containers.model.CommandConfiguration;

public interface ContainerConfigService {
    String TOOL_ID = "container-service";

    long getDefaultDockerHubId();
    void setDefaultDockerHubId(long hubId, String username, String reason);

    void configureForSite(long commandId, String wrapperName, CommandConfiguration commandConfiguration, String username, String reason) throws CommandConfigurationException;
    void configureForProject(String project, long commandId, String wrapperName, CommandConfiguration commandConfiguration, String username, String reason) throws CommandConfigurationException;

    CommandConfiguration getSiteConfiguration(long commandId, String wrapperName);
    CommandConfiguration getProjectConfiguration(String project, long commandId, String wrapperName);

    void deleteSiteConfiguration(long commandId, String wrapperName, final String username) throws CommandConfigurationException;
    void deleteProjectConfiguration(String project, long commandId, String wrapperName, final String username) throws CommandConfigurationException;
    void deleteAllConfiguration(long commandId, String wrapperName);
    void deleteAllConfiguration(long commandId);

    class CommandConfigurationException extends Exception {
        public CommandConfigurationException(final String message, final Throwable e) {
            super(message, e);
        }
    }
}
