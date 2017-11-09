package org.nrg.containers.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.nrg.config.services.ConfigService;
import org.nrg.containers.model.configuration.CommandConfigurationEntity;
import org.nrg.containers.rest.CommandConfigurationRestApi;
import org.nrg.containers.services.CommandConfigurationEntityService;
import org.nrg.containers.services.CommandConfigurationService;
import org.nrg.containers.services.CommandEntityService;
import org.nrg.containers.services.CommandService;
import org.nrg.containers.services.ContainerConfigService;
import org.nrg.containers.services.impl.CommandConfigurationServiceImpl;
import org.nrg.containers.services.impl.CommandServiceImpl;
import org.nrg.containers.services.impl.ContainerConfigServiceImpl;
import org.nrg.framework.services.ContextService;
import org.nrg.xdat.security.services.PermissionsServiceI;
import org.nrg.xdat.security.services.RoleHolder;
import org.nrg.xdat.security.services.UserManagementServiceI;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.TestingAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableWebSecurity
@Import({RestApiTestConfig.class})
public class CommandConfigurationRestApiTestConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public CommandConfigurationRestApi commandConfigurationRestApi(final CommandService commandService,
                                                                   final CommandConfigurationService commandConfigurationService,
                                                                   final UserManagementServiceI userManagementServiceI,
                                                                   final RoleHolder roleHolder) {
        return new CommandConfigurationRestApi(commandService, commandConfigurationService, userManagementServiceI, roleHolder);
    }

    @Bean
    public CommandService commandService(final CommandEntityService commandEntityService,
                                         final ContainerConfigService containerConfigService) {
        return new CommandServiceImpl(commandEntityService, containerConfigService);
    }

    @Bean
    public CommandEntityService mockCommandEntityService() {
        return Mockito.mock(CommandEntityService.class);
    }

    @Bean
    public ContainerConfigService containerConfigService(final ConfigService configService,
                                                         final ObjectMapper objectMapper) {
        return new ContainerConfigServiceImpl(configService, objectMapper);
    }

    @Bean
    public ConfigService configService() {
        return Mockito.mock(ConfigService.class);
    }

    @Bean
    public CommandConfigurationEntityService mockCommandConfigurationEntityService() {
        return Mockito.mock(CommandConfigurationEntityService.class);
    }

    @Bean
    public CommandConfigurationService commandConfigurationService(final CommandConfigurationEntityService commandConfigurationEntityService,
                                                                   final ContainerConfigService containerConfigService) {
        return new CommandConfigurationServiceImpl(commandConfigurationEntityService, containerConfigService);
    }

    @Bean
    public ContextService contextService(final ApplicationContext applicationContext) {
        final ContextService contextService = new ContextService();
        contextService.setApplicationContext(applicationContext);
        return contextService;
    }

    @Bean
    public PermissionsServiceI permissionsService() {
        return Mockito.mock(PermissionsServiceI.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new TestingAuthenticationProvider());
    }

}
