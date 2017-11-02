package org.nrg.containers.model.configuration;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.nrg.framework.orm.hibernate.AbstractHibernateEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

@Entity
public class CommandConfigurationEntity extends AbstractHibernateEntity {
    private Long commandWrapperId;
    private String project;
    private List<CommandConfigurationInputEntity> inputs;
    private List<CommandConfigurationOutputEntity> outputs;
    private Boolean enabled;

    public static CommandConfigurationEntity fromPojo(final CommandConfiguration commandConfiguration) {
        return new CommandConfigurationEntity().update(commandConfiguration);
    }

    public CommandConfigurationEntity update(final CommandConfiguration commandConfiguration) {
        // todo stuff

        return this;
    }

    public Long getCommandWrapperId() {
        return commandWrapperId;
    }

    public void setCommandWrapperId(final Long commandWrapperId) {
        this.commandWrapperId = commandWrapperId;
    }

    public String getProject() {
        return project;
    }

    public void setProject(final String project) {
        this.project = project;
    }

    @OneToMany(mappedBy = "commandConfigurationEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<CommandConfigurationInputEntity> getInputs() {
        return inputs;
    }

    public void setInputs(final List<CommandConfigurationInputEntity> inputs) {
        this.inputs = inputs == null ?
                Lists.<CommandConfigurationInputEntity>newArrayList() :
                inputs;
        for (final CommandConfigurationInputEntity input : this.inputs) {
            input.setCommandConfigurationEntity(this);
        }
    }

    public void addInput(final CommandConfigurationInputEntity input) {
        if (input == null) {
            return;
        }
        input.setCommandConfigurationEntity(this);

        if (this.inputs == null) {
            this.inputs = Lists.newArrayList();
        }
        this.inputs.add(input);
    }

    @OneToMany(mappedBy = "commandConfigurationEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<CommandConfigurationOutputEntity> getOutputs() {
        return outputs;
    }

    public void setOutputs(final List<CommandConfigurationOutputEntity> outputs) {
        this.outputs = outputs == null ?
                Lists.<CommandConfigurationOutputEntity>newArrayList() :
                outputs;
        for (final CommandConfigurationOutputEntity output : this.outputs) {
            output.setCommandConfigurationEntity(this);
        }
    }

    public void addOutput(final CommandConfigurationOutputEntity output) {
        if (output == null) {
            return;
        }
        output.setCommandConfigurationEntity(this);

        if (this.outputs == null) {
            this.outputs = Lists.newArrayList();
        }
        this.outputs.add(output);
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(final Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final CommandConfigurationEntity that = (CommandConfigurationEntity) o;
        return Objects.equals(this.commandWrapperId, that.commandWrapperId) &&
                Objects.equals(this.project, that.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), commandWrapperId, project);
    }

    @Override
    public String toString() {
        return addParentPropertiesToString(MoreObjects.toStringHelper(this))
                .add("commandWrapperId", commandWrapperId)
                .add("project", project)
                .add("inputs", inputs)
                .add("outputs", outputs)
                .add("enabled", enabled)
                .toString();
    }
}
