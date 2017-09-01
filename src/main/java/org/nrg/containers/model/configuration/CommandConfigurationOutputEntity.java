package org.nrg.containers.model.configuration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class CommandConfigurationOutputEntity implements Serializable {
    private long id;
    private CommandConfigurationEntity commandConfigurationEntity;
    private String label;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

}
