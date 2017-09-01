package org.nrg.containers.model.configuration;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.nrg.containers.model.command.entity.CommandWrapperEntity;
import org.nrg.framework.orm.hibernate.AbstractHibernateEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

@Entity
public class CommandConfigurationEntity extends AbstractHibernateEntity {
    private CommandWrapperEntity wrapper;
    private Scope scope;
    private String scopeEntityId;
    private List<CommandConfigurationInputEntity> inputs;
    private List<CommandConfigurationOutputEntity> outputs;
    private Boolean enabled;

    @ManyToOne
    public CommandWrapperEntity getWrapper() {
        return wrapper;
    }

    public void setWrapper(final CommandWrapperEntity wrapper) {
        this.wrapper = wrapper;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(final Scope scope) {
        this.scope = scope;
    }

    public String getScopeEntityId() {
        return scopeEntityId;
    }

    public void setScopeEntityId(final String scopeEntityId) {
        this.scopeEntityId = scopeEntityId;
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
        return Objects.equals(this.wrapper, that.wrapper) &&
                Objects.equals(this.scope, that.scope) &&
                Objects.equals(this.scopeEntityId, that.scopeEntityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), wrapper, scope, scopeEntityId);
    }

    @Override
    public String toString() {
        return addParentPropertiesToString(MoreObjects.toStringHelper(this))
                .add("wrapper", wrapper)
                .add("scope", scope)
                .add("scopeEntityId", scopeEntityId)
                .add("inputs", inputs)
                .add("outputs", outputs)
                .add("enabled", enabled)
                .toString();
    }
}
