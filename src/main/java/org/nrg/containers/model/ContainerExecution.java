package org.nrg.containers.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.nrg.framework.orm.hibernate.AbstractHibernateEntity;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Entity
public class ContainerExecution extends AbstractHibernateEntity {
    @JsonProperty("command-id") private Long commandId;
    @JsonProperty("xnat-command-wrapper-id") private Long xnatCommandWrapperId;
    @JsonProperty("docker-image") private String dockerImage;
    @JsonProperty("command-line") private String commandLine;
    @JsonProperty("env") private Map<String, String> environmentVariables = Maps.newHashMap();
    @JsonProperty("mounts-in") private List<ContainerExecutionMount> mountsIn = Lists.newArrayList();
    @JsonProperty("mounts-out") private List<ContainerExecutionMount> mountsOut = Lists.newArrayList();
    @JsonProperty("container-id") private String containerId;
    @JsonProperty("user-id") private String userId;
    @JsonProperty("raw-input-values") private Map<String, String> rawInputValues;
    @JsonProperty("xnat-input-values") private Map<String, String> xnatInputValues;
    @JsonProperty("command-input-values") private Map<String, String> commandInputValues;
    private List<ContainerExecutionOutput> outputs;
    private List<ContainerExecutionHistory> history = Lists.newArrayList();
    @JsonProperty("log-paths") private Set<String> logPaths;

    public ContainerExecution() {}

    public ContainerExecution(final ResolvedCommand resolvedCommand,
                              final String containerId,
                              final String userId) {
        this.containerId = containerId;
        this.userId = userId;

        this.commandId = resolvedCommand.getCommandId();
        this.xnatCommandWrapperId = resolvedCommand.getXnatCommandWrapperId();
        this.dockerImage = resolvedCommand.getImage();
        this.commandLine = resolvedCommand.getCommandLine();
        this.environmentVariables = resolvedCommand.getEnvironmentVariables() == null ?
                Maps.<String, String>newHashMap() :
                Maps.newHashMap(resolvedCommand.getEnvironmentVariables());
        this.mountsIn = resolvedCommand.getMountsIn() == null ?
                Lists.<ContainerExecutionMount>newArrayList() :
                Lists.newArrayList(resolvedCommand.getMountsIn());
        this.mountsOut = resolvedCommand.getMountsOut() == null ?
                Lists.<ContainerExecutionMount>newArrayList() :
                Lists.newArrayList(resolvedCommand.getMountsOut());
        this.rawInputValues = resolvedCommand.getRawInputValues() == null ?
                Maps.<String, String>newHashMap() :
                resolvedCommand.getRawInputValues();
        this.xnatInputValues = resolvedCommand.getXnatInputValues() == null ?
                Maps.<String, String>newHashMap() :
                resolvedCommand.getXnatInputValues();
        this.commandInputValues = resolvedCommand.getCommandInputValues() == null ?
                Maps.<String, String>newHashMap() :
                resolvedCommand.getCommandInputValues();
        this.outputs = resolvedCommand.getOutputs() == null ?
                Lists.<ContainerExecutionOutput>newArrayList() :
                Lists.newArrayList(resolvedCommand.getOutputs());
    }

    public Long getCommandId() {
        return commandId;
    }

    public void setCommandId(final Long commandId) {
        this.commandId = commandId;
    }

    public Long getXnatCommandWrapperId() {
        return xnatCommandWrapperId;
    }

    public void setXnatCommandWrapperId(final Long xnatCommandWrapperId) {
        this.xnatCommandWrapperId = xnatCommandWrapperId;
    }

    public String getDockerImage() {
        return dockerImage;
    }

    public void setDockerImage(final String dockerImage) {
        this.dockerImage = dockerImage;
    }

    public String getCommandLine() {
        return commandLine;
    }

    public void setCommandLine(final String commandLine) {
        this.commandLine = commandLine;
    }

    @ElementCollection
    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public void setEnvironmentVariables(final Map<String, String> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    @ElementCollection
    public List<ContainerExecutionMount> getMountsIn() {
        return mountsIn;
    }

    public void setMountsIn(final List<ContainerExecutionMount> mountsIn) {
        this.mountsIn = mountsIn;
    }

    @ElementCollection
    public List<ContainerExecutionMount> getMountsOut() {
        return mountsOut;
    }

    public void setMountsOut(final List<ContainerExecutionMount> mountsOut) {
        this.mountsOut = mountsOut;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(final String containerId) {
        this.containerId = containerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String user) {
        this.userId = user;
    }

    @ElementCollection
    @Column(columnDefinition = "TEXT")
    public Map<String, String> getRawInputValues() {
        return rawInputValues;
    }

    public void setRawInputValues(final Map<String, String> rawInputValues) {
        this.rawInputValues = rawInputValues == null ?
                Maps.<String, String>newHashMap() :
                rawInputValues;
    }

    @ElementCollection
    @Column(columnDefinition = "TEXT")
    public Map<String, String> getXnatInputValues() {
        return xnatInputValues;
    }

    public void setXnatInputValues(final Map<String, String> xnatInputValues) {
        this.xnatInputValues = xnatInputValues == null ?
                Maps.<String, String>newHashMap() :
                xnatInputValues;
    }

    @ElementCollection
    @Column(columnDefinition = "TEXT")
    public Map<String, String> getCommandInputValues() {
        return commandInputValues;
    }

    public void setCommandInputValues(final Map<String, String> commandInputValues) {
        this.commandInputValues = commandInputValues == null ?
                Maps.<String, String>newHashMap() :
                commandInputValues;
    }

    @ElementCollection
    public List<ContainerExecutionOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(final List<ContainerExecutionOutput> outputs) {
        this.outputs = outputs;
    }

    @ElementCollection
    public List<ContainerExecutionHistory> getHistory() {
        return history;
    }

    public void setHistory(final List<ContainerExecutionHistory> history) {
        this.history = history;
    }

    @Transient
    public void addToHistory(final ContainerExecutionHistory historyItem) {
        if (this.history == null) {
            this.history = Lists.newArrayList();
        }
        this.history.add(historyItem);
    }

    @ElementCollection
    public Set<String> getLogPaths() {
        return logPaths;
    }

    public void setLogPaths(final Set<String> logPaths) {
        this.logPaths = logPaths;
    }

    @Transient
    public void addLogPath(final String logPath) {
        if (StringUtils.isBlank(logPath)) {
            return;
        }

        if (this.logPaths == null) {
            this.logPaths = Sets.newHashSet();
        }
        this.logPaths.add(logPath);
    }

    @Transient
    public void addLogPaths(final Set<String> logPaths) {
        if (logPaths == null || logPaths.isEmpty()) {
            return;
        }

        for (final String logPath : logPaths) {
            addLogPath(logPath);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final ContainerExecution that = (ContainerExecution) o;
        return Objects.equals(this.commandId, that.commandId) &&
                Objects.equals(this.xnatCommandWrapperId, that.xnatCommandWrapperId) &&
                Objects.equals(this.dockerImage, that.dockerImage) &&
                Objects.equals(this.commandLine, that.commandLine) &&
                Objects.equals(this.environmentVariables, that.environmentVariables) &&
                Objects.equals(this.mountsIn, that.mountsIn) &&
                Objects.equals(this.mountsOut, that.mountsOut) &&
                Objects.equals(this.containerId, that.containerId) &&
                Objects.equals(this.userId, that.userId) &&
                Objects.equals(this.rawInputValues, that.rawInputValues) &&
                Objects.equals(this.xnatInputValues, that.xnatInputValues) &&
                Objects.equals(this.commandInputValues, that.commandInputValues) &&
                Objects.equals(this.outputs, that.outputs) &&
                Objects.equals(this.history, that.history) &&
                Objects.equals(this.logPaths, that.logPaths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), commandId, xnatCommandWrapperId, dockerImage, commandLine, environmentVariables,
                mountsIn, mountsOut, containerId, userId, rawInputValues, xnatInputValues, commandInputValues, outputs, history, logPaths);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("commandId", commandId)
                .add("xnatCommandWrapperId", xnatCommandWrapperId)
                .add("dockerImage", dockerImage)
                .add("commandLine", commandLine)
                .add("environmentVariables", environmentVariables)
                .add("mountsIn", mountsIn)
                .add("mountsOut", mountsOut)
                .add("containerId", containerId)
                .add("userId", userId)
                .add("rawInputValues", rawInputValues)
                .add("xnatInputValues", xnatInputValues)
                .add("commandInputValues", commandInputValues)
                .add("outputs", outputs)
                .add("history", history)
                .add("logPaths", logPaths)
                .toString();
    }
}
