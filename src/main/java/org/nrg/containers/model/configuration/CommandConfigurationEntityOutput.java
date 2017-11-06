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
public class CommandConfigurationEntityOutput implements Serializable {
    private long id;
    private CommandConfigurationEntity commandConfigurationEntity;
    private String name;
    private String label;

    @Nonnull
    public static CommandConfigurationEntityOutput fromPojo(final @Nonnull CommandConfiguration.Output output) {
        return new CommandConfigurationEntityOutput().update(output);
    }

    @Nonnull
    public CommandConfigurationEntityOutput update(final @Nonnull CommandConfiguration.Output that) {
        if (this.getId() == 0L && that.id() != null) {
            this.setId(that.id());
        }
        this.setName(that.name());
        this.setLabel(that.label());
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

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CommandConfigurationEntityOutput that = (CommandConfigurationEntityOutput) o;
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
                .add("name", name)
                .add("label", label)
                .toString();
    }
}
