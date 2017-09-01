package org.nrg.containers.model.configuration;

import com.google.common.base.MoreObjects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class CommandConfigurationInputEntity implements Serializable {
    private long id;
    private CommandConfigurationEntity commandConfigurationEntity;
    private String defaultValue;
    private String matcher;
    private Boolean userSettable;
    private Boolean advanced;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @ManyToOne
    public CommandConfigurationEntity getCommandConfigurationEntity() {
        return commandConfigurationEntity;
    }

    public void setCommandConfigurationEntity(final CommandConfigurationEntity commandConfigurationEntity) {
        this.commandConfigurationEntity = commandConfigurationEntity;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getMatcher() {
        return matcher;
    }

    public void setMatcher(final String matcher) {
        this.matcher = matcher;
    }

    public Boolean getUserSettable() {
        return userSettable;
    }

    public void setUserSettable(final Boolean userSettable) {
        this.userSettable = userSettable;
    }

    public Boolean getAdvanced() {
        return advanced;
    }

    public void setAdvanced(final Boolean advanced) {
        this.advanced = advanced;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CommandConfigurationInputEntity that = (CommandConfigurationInputEntity) o;
        return id == that.id &&
                Objects.equals(this.commandConfigurationEntity, that.commandConfigurationEntity) &&
                Objects.equals(this.defaultValue, that.defaultValue) &&
                Objects.equals(this.matcher, that.matcher) &&
                Objects.equals(this.userSettable, that.userSettable) &&
                Objects.equals(this.advanced, that.advanced);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, commandConfigurationEntity, defaultValue, matcher, userSettable, advanced);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("commandConfigurationEntity", commandConfigurationEntity)
                .add("defaultValue", defaultValue)
                .add("matcher", matcher)
                .add("userSettable", userSettable)
                .add("advanced", advanced)
                .toString();
    }
}
