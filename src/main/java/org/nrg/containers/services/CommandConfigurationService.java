package org.nrg.containers.services;

import org.nrg.containers.model.configuration.CommandConfiguration;

public interface CommandConfigurationService {
    CommandConfiguration get(long wrapperId, String project);
    CommandConfiguration get(long commandId, long wrapperId, String project);
    CommandConfiguration get(long commandId, String wrapperName, String project);

    void configure(CommandConfiguration commandConfiguration, long wrapperId, String project);
    void configure(CommandConfiguration commandConfiguration, long commandId, long wrapperId, String project);
    void configure(CommandConfiguration commandConfiguration, long commandId, String wrapperName, String project);

    Boolean isEnabled(long wrapperId, String project);
    Boolean isEnabled(long commandId, long wrapperId, String project);
    Boolean isEnabled(long commandId, String wrapperName, String project);

    void enable(long wrapperId, String project);
    void enable(long commandId, long wrapperId, String project);
    void enable(long commandId, String wrapperName, String project);
    void disable(long wrapperId, String project);
    void disable(long commandId, long wrapperId, String project);
    void disable(long commandId, String wrapperName, String project);
    void setEnabled(Boolean enabled, long wrapperId, String project);
    void setEnabled(Boolean enabled, long commandId, long wrapperId, String project);
    void setEnabled(Boolean enabled, long commandId, String wrapperName, String project);

    void delete(long wrapperId, String project);
    void delete(long commandId, long wrapperId, String project);
    void delete(long commandId, String wrapperName, String project);
    void deleteAllForCommand(long commandId);
    void deleteAllForWrapper(long wrapperId);
    void deleteAllForWrapper(long commandId, String wrapperName);
}
