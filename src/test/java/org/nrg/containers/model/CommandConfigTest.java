package org.nrg.containers.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.nrg.config.entities.Configuration;
import org.nrg.config.services.ConfigService;
import org.nrg.containers.config.CommandConfigurationTestConfig;
import org.nrg.containers.model.configuration.CommandConfigInternal;
import org.nrg.containers.model.configuration.CommandConfigInternal.Input;
import org.nrg.containers.model.configuration.CommandConfigInternal.Output;
import org.nrg.containers.services.ContainerConfigService;
import org.nrg.framework.constants.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.when;
import static org.nrg.containers.services.ContainerConfigService.TOOL_ID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommandConfigurationTestConfig.class)
public class CommandConfigTest {

    private static final long WRAPPER_ID = 12L;
    private static final String PROJECT_NAME = "xyz";

    @Autowired private ObjectMapper mapper;
    @Autowired private ContainerConfigService containerConfigService;
    @Autowired private ConfigService mockConfigService;

    @Test
    public void testSpringConfiguration() {
        assertThat(containerConfigService, not(nullValue()));
    }

    @Test
    public void testConfigureCommandForSite() throws Exception {
        final CommandConfigInternal site = CommandConfigInternal.builder()
                .addInput("foo", Input.builder().defaultValue("a").userSettable(true).build())
                .addOutput("bar", Output.create("label"))
                .build();
        final String siteJson = mapper.writeValueAsString(site);

        final Configuration mockSiteConfiguration = Mockito.mock(Configuration.class);
        when(mockSiteConfiguration.getContents()).thenReturn(siteJson);
        when(mockConfigService.getConfig(eq(TOOL_ID), anyString(), eq(Scope.Site), isNull(String.class))).thenReturn(mockSiteConfiguration);

        final CommandConfigInternal retrieved = containerConfigService.getSiteConfiguration(WRAPPER_ID);
        assertThat(retrieved, is(site));
    }

    @Test
    public void testConfigureCommandForProject() throws Exception {

        final Map<String, Input> siteInputs = Maps.newHashMap();
        final Map<String, Input> projectInputs = Maps.newHashMap();
        final Map<String, Input> expectedInputs = Maps.newHashMap();

        final Input allNullInput = Input.builder().build();

        final Input allNotNullInput = allNotNullInputBuilder().build();
        final Input allNotNullInput2 = Input.builder()
                .defaultValue("fly")
                .matcher("fools")
                .userSettable(false)
                .advanced(false)
                .build();

        siteInputs.put("a", allNotNullInput);
        projectInputs.put("a", allNullInput);
        expectedInputs.put("a", allNotNullInput);

        siteInputs.put("e", allNullInput);
        projectInputs.put("e", allNullInput);
        expectedInputs.put("e", allNullInput);

        siteInputs.put("f", allNotNullInput);
        projectInputs.put("f", allNotNullInput2);
        expectedInputs.put("f", allNotNullInput2);

        siteInputs.put("b", allNotNullInput);
        projectInputs.put("b", Input.builder().defaultValue("not-null").build());
        expectedInputs.put("b", allNotNullInputBuilder().defaultValue("not-null").build());

        siteInputs.put("c", allNotNullInput);
        projectInputs.put("c", Input.builder().matcher("not-null").build());
        expectedInputs.put("c", allNotNullInputBuilder().matcher("not-null").build());

        siteInputs.put("d", allNotNullInput);
        projectInputs.put("d", Input.builder().userSettable(false).build());
        expectedInputs.put("d", allNotNullInputBuilder().userSettable(false).build());

        final Map<String, Output> siteOutputs = Maps.newHashMap();
        final Map<String, Output> projectOutputs = Maps.newHashMap();
        final Map<String, Output> expectedOutputs = Maps.newHashMap();

        final Output allNull = Output.create(null);
        final Output nonNull = Output.create("181024y2");
        final Output nonNull2 = Output.create("2");

        siteOutputs.put("a", nonNull);
        projectOutputs.put("a", allNull);
        expectedOutputs.put("a", nonNull);

        siteOutputs.put("b", allNull);
        projectOutputs.put("b", nonNull);
        expectedOutputs.put("b", nonNull);

        siteOutputs.put("c", nonNull);
        projectOutputs.put("c", nonNull2);
        expectedOutputs.put("c", nonNull2);

        final CommandConfigInternal site = CommandConfigInternal.create(true, siteInputs, siteOutputs);
        final CommandConfigInternal project = CommandConfigInternal.create(true, projectInputs, projectOutputs);
        final CommandConfigInternal expected = CommandConfigInternal.create(true, expectedInputs, expectedOutputs);

        final String siteJson = mapper.writeValueAsString(site);
        final String projectJson = mapper.writeValueAsString(project);

        final Configuration mockSiteConfiguration = Mockito.mock(Configuration.class);
        when(mockSiteConfiguration.getContents()).thenReturn(siteJson);
        when(mockConfigService.getConfig(eq(TOOL_ID), anyString(), eq(Scope.Site), isNull(String.class))).thenReturn(mockSiteConfiguration);
        final Configuration mockProjectConfiguration = Mockito.mock(Configuration.class);
        when(mockProjectConfiguration.getContents()).thenReturn(projectJson);
        when(mockConfigService.getConfig(eq(TOOL_ID), anyString(), eq(Scope.Project), isNotNull(String.class))).thenReturn(mockProjectConfiguration);

        final CommandConfigInternal retrieved =
                containerConfigService.getProjectConfiguration(PROJECT_NAME, WRAPPER_ID);
        assertThat(retrieved, is(expected));
    }

    private Input.Builder allNotNullInputBuilder() {
        return Input.builder()
                .defaultValue("who")
                .matcher("cares")
                .advanced(true)
                .userSettable(true);
    }
}
