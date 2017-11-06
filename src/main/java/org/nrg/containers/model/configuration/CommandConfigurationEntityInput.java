package org.nrg.containers.model.configuration;

import com.google.common.base.MoreObjects;

import javax.annotation.Nonnull;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class CommandConfigurationEntityInput implements Serializable {
    private long id;
    private CommandConfigurationEntity commandConfigurationEntity;
    private String name;
    private String defaultValue;
    private String matcher;
    private Boolean userSettable;
    private Boolean advanced;

    @Nonnull
    public static CommandConfigurationEntityInput fromPojo(final @Nonnull CommandConfiguration.Input that) {
        return new CommandConfigurationEntityInput().update(that);
    }

    @Nonnull
    public CommandConfigurationEntityInput update(final @Nonnull CommandConfiguration.Input that) {
        if (this.getId() == 0L && that.id() != null) {
            this.setId(that.id());
        }
        this.setName(that.name());
        this.setDefaultValue(that.defaultValue());
        this.setMatcher(that.matcher());
        this.setUserSettable(that.userSettable());
        this.setAdvanced(that.advanced());
        return this;
    }

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

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
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
        final CommandConfigurationEntityInput that = (CommandConfigurationEntityInput) o;
        return id == that.id &&
                Objects.equals(this.commandConfigurationEntity, that.commandConfigurationEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, commandConfigurationEntity);
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
