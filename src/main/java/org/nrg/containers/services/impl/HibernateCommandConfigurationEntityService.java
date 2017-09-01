package org.nrg.containers.services.impl;

import org.nrg.containers.daos.CommandConfigurationRepository;
import org.nrg.containers.model.configuration.CommandConfigurationEntity;
import org.nrg.containers.services.CommandConfigurationEntityService;
import org.nrg.framework.orm.hibernate.AbstractHibernateEntityService;
import org.springframework.stereotype.Service;

@Service
public class HibernateCommandConfigurationEntityService
        extends AbstractHibernateEntityService<CommandConfigurationEntity, CommandConfigurationRepository>
        implements CommandConfigurationEntityService {
}
