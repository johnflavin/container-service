package org.nrg.containers.daos;

import org.hibernate.Hibernate;
import org.nrg.containers.model.configuration.CommandConfigurationEntity;
import org.nrg.framework.orm.hibernate.AbstractHibernateDAO;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class CommandConfigurationRepository extends AbstractHibernateDAO<CommandConfigurationEntity> {
    @Override
    public void initialize(final CommandConfigurationEntity commandConfigurationEntity) {
        if (commandConfigurationEntity == null) {
            return;
        }
        Hibernate.initialize(commandConfigurationEntity);
        Hibernate.initialize(commandConfigurationEntity.getInputs());
        Hibernate.initialize(commandConfigurationEntity.getOutputs());
    }

    public void initialize(final List<CommandConfigurationEntity> commandConfigurationEntities) {
        if (commandConfigurationEntities == null) {
            return;
        }
        for (final CommandConfigurationEntity commandConfigurationEntity : commandConfigurationEntities) {
            initialize(commandConfigurationEntity);
        }
    }

    @Override
    public List<CommandConfigurationEntity> findByProperties(final Map<String, Object> properties) {
        final List<CommandConfigurationEntity> found = super.findByProperties(properties);
        initialize(found);
        return found != null ? found : Collections.<CommandConfigurationEntity>emptyList();
    }
}
