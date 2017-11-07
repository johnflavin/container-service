package org.nrg.containers.services;

import org.nrg.containers.model.configuration.CommandConfiguration;
import org.nrg.framework.exceptions.NotFoundException;

public interface CommandConfigurationService {
    CommandConfiguration retrieve(long configurationId);
    CommandConfiguration retrieve(long wrapperId, String project);
    CommandConfiguration get(long configurationId) throws NotFoundException;
    CommandConfiguration get(long wrapperId, String project) throws NotFoundException;
    CommandConfiguration create(CommandConfiguration commandConfiguration);
    boolean isEnabled(long wrapperId, String project) throws NotFoundException;
    void enable(long wrapperId, String project) throws NotFoundException;
    void disable(long wrapperId, String project) throws NotFoundException;
    void setEnabled(boolean enabled, long wrapperId, String project) throws NotFoundException;

    void delete(long configurationId) throws NotFoundException;
    void delete(long wrapperId, String project) throws NotFoundException;
    void deleteAllForWrapper(long wrapperId);
}
