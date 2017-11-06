package org.nrg.containers.model.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import org.nrg.containers.model.command.auto.Command;
import org.nrg.containers.model.command.auto.Command.CommandInput;
import org.nrg.containers.model.command.auto.Command.CommandWrapper;
import org.nrg.containers.model.command.auto.Command.CommandWrapperDerivedInput;
import org.nrg.containers.model.command.auto.Command.CommandWrapperExternalInput;
import org.nrg.containers.model.command.auto.Command.CommandWrapperOutput;
import org.nrg.containers.model.command.auto.Command.ConfiguredCommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoValue
@JsonInclude(Include.ALWAYS)
public abstract class CommandConfiguration {
    @JsonProperty("id") public abstract Long id();
    @JsonProperty("wrapper-id") @Nullable public abstract Long wrapperId();
    @JsonProperty("project") public abstract String project();
    @JsonProperty("enabled") @Nullable public abstract Boolean enabled();
    @JsonProperty("inputs") public abstract ImmutableList<Input> inputs();
    @JsonProperty("outputs") public abstract ImmutableList<Output> outputs();

    @JsonCreator
    public static CommandConfiguration create(@JsonProperty("id") final Long id,
                                              @JsonProperty("wrapper-id") final Long wrapperId,
                                              @JsonProperty("project") final String project,
                                              @JsonProperty("enabled") final Boolean enabled,
                                              @JsonProperty("inputs") final List<Input> inputs,
                                              @JsonProperty("outputs") final List<Output> outputs) {
        return builder()
                .id(id)
                .wrapperId(wrapperId)
                .project(project)
                .enabled(enabled)
                .inputs(inputs == null ? Collections.<Input>emptyList() : inputs)
                .outputs(outputs == null ? Collections.<Output>emptyList() : outputs)
                .build();
    }

    // public static CommandConfiguration create(final @Nonnull Command command,
    //                                           final @Nonnull CommandWrapper commandWrapper,
    //                                           final @Nullable CommandConfigInternal commandConfigInternal) {
    //     Builder builder = builder();
    //     final Set<String> handledCommandInputs = Sets.newHashSet();
    //
    //     final Map<String, CommandConfigInternal.Input> configuredInputs
    //             = commandConfigInternal == null ?
    //                     Collections.<String, CommandConfigInternal.Input>emptyMap() :
    //                     commandConfigInternal.inputs();
    //     final Map<String, CommandConfigInternal.Output> configuredOutputs
    //             = commandConfigInternal == null ?
    //                     Collections.<String, CommandConfigInternal.Output>emptyMap() :
    //                     commandConfigInternal.outputs();
    //
    //     for (final CommandWrapperExternalInput externalInput : commandWrapper.externalInputs()) {
    //         builder = builder.addInput(externalInput.name(),
    //                 CommandConfiguration.Input.create(externalInput, configuredInputs.get(externalInput.name())));
    //         handledCommandInputs.add(externalInput.providesValueForCommandInput());
    //     }
    //     for (final CommandWrapperDerivedInput derivedInput : commandWrapper.derivedInputs()) {
    //         builder = builder.addInput(derivedInput.name(),
    //                 CommandConfiguration.Input.create(derivedInput, configuredInputs.get(derivedInput.name())));
    //         handledCommandInputs.add(derivedInput.providesValueForCommandInput());
    //     }
    //     for (final CommandWrapperOutput wrapperOutput : commandWrapper.outputHandlers()) {
    //         builder = builder.addOutput(wrapperOutput.name(),
    //                 Output.create(wrapperOutput, configuredOutputs.get(wrapperOutput.name())));
    //     }
    //
    //     for (final CommandInput commandInput : command.inputs()) {
    //         if (!handledCommandInputs.contains(commandInput.name())) {
    //             builder = builder.addInput(commandInput.name(),
    //                     CommandConfiguration.Input.create(commandInput, configuredInputs.get(commandInput.name())));
    //         }
    //     }
    //
    //     return builder.build();
    // }

    public static CommandConfiguration create(final @Nonnull CommandConfigurationEntity commandConfigurationEntity) {
        final CommandConfiguration.Builder builder = builder()
                .id(commandConfigurationEntity.getId())
                .wrapperId(commandConfigurationEntity.getCommandWrapperId())
                .enabled(commandConfigurationEntity.getEnabled());

        for (final CommandConfigurationEntityInput input : commandConfigurationEntity.getInputs()) {
            builder.addInput(Input.create(input));
        }

        for (final CommandConfigurationEntityOutput output : commandConfigurationEntity.getOutputs()) {
            builder.addOutput(Output.create(output));
        }

        return builder.build();
    }

    public static Builder builder() {
        return new AutoValue_CommandConfiguration.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Long id);
        public abstract Builder wrapperId(Long wrapperId);
        public abstract Builder project(String project);
        public abstract Builder enabled(Boolean enabled);

        public abstract Builder inputs(@Nonnull List<Input> inputs);
        abstract ImmutableList.Builder<Input> inputsBuilder();
        public Builder addInput(final @Nonnull Input input) {
            inputsBuilder().add(input);
            return this;
        }

        public abstract Builder outputs(@Nonnull List<Output> outputs);
        abstract ImmutableList.Builder<Output> outputsBuilder();
        public Builder addOutput(final @Nonnull Output output) {
            outputsBuilder().add(output);
            return this;
        }

        public abstract CommandConfiguration build();
    }

    @AutoValue
    @JsonInclude(Include.ALWAYS)
    public static abstract class Input {
        @Nullable @JsonProperty("id") public abstract Long id();
        @Nullable @JsonProperty("name") public abstract String name();
        @Nullable @JsonProperty("default-value") public abstract String defaultValue();
        @Nullable @JsonProperty("matcher") public abstract String matcher();
        @Nullable @JsonProperty("user-settable") public abstract Boolean userSettable();
        @Nullable @JsonProperty("advanced") public abstract Boolean advanced();
        @Nullable @JsonProperty("required") public abstract Boolean required();

        @JsonCreator
        static Input create(@JsonProperty("id") final Long id,
                            @JsonProperty("name") final String name,
                            @JsonProperty("default-value") final String defaultValue,
                            @JsonProperty("matcher") final String matcher,
                            @JsonProperty("user-settable") final Boolean userSettable,
                            @JsonProperty("advanced") final Boolean advanced,
                            @JsonProperty("required") final Boolean required) {
            return builder()
                    .id(id)
                    .name(name)
                    .defaultValue(defaultValue)
                    .matcher(matcher)
                    .userSettable(userSettable)
                    .advanced(advanced)
                    .required(required)
                    .build();
        }

        // static Input create(final CommandInput commandInput,
        //                     final CommandConfigInternal.Input input) {
        //     final Builder builder = builder()
        //             .description(commandInput.description())
        //             .type(commandInput.type())
        //             .defaultValue(commandInput.defaultValue())
        //             .matcher(commandInput.matcher())
        //             .required(commandInput.required());
        //
        //     if (input != null) {
        //         // Set those things that the command input does not have, even if they are null
        //         builder.userSettable(input.userSettable())
        //                 .advanced(input.advanced());
        //
        //         // Override those things the command input does have only if they are not null
        //         if (input.defaultValue() != null) {
        //             builder.defaultValue(input.defaultValue());
        //         }
        //         if (input.matcher() != null) {
        //             builder.matcher(input.matcher());
        //         }
        //     }
        //
        //     return builder.build();
        // }

        // static Input create(final Command.CommandWrapperInput commandWrapperInput,
        //                     final CommandConfigInternal.Input input) {
        //     final Builder builder = builder()
        //             .description(commandWrapperInput.description())
        //             .type(commandWrapperInput.type())
        //             .defaultValue(commandWrapperInput.defaultValue())
        //             .matcher(commandWrapperInput.matcher())
        //             .userSettable(commandWrapperInput.userSettable());
        //
        //     if (input != null) {
        //         // Set those things that the command wrapper input does not have, even if they are null
        //         builder.advanced(input.advanced());
        //
        //         // Override those things the command wrapper input does have only if they are not null
        //         if (input.defaultValue() != null) {
        //             builder.defaultValue(input.defaultValue());
        //         }
        //         if (input.matcher() != null) {
        //             builder.matcher(input.matcher());
        //         }
        //         if (input.userSettable() != null) {
        //             builder.userSettable(input.userSettable());
        //         }
        //     }
        //
        //     return builder.build();
        // }

        public static Input create(final @Nonnull CommandConfigurationEntityInput commandConfigurationEntityInput) {
            return builder()
                    .id(commandConfigurationEntityInput.getId())
                    .name(commandConfigurationEntityInput.getName())
                    .defaultValue(commandConfigurationEntityInput.getDefaultValue())
                    .matcher(commandConfigurationEntityInput.getMatcher())
                    .userSettable(commandConfigurationEntityInput.getUserSettable())
                    .advanced(commandConfigurationEntityInput.getAdvanced())
                    .build();
        }

        public static Builder builder() {
            return new AutoValue_CommandConfiguration_Input.Builder()
                    .userSettable(true)
                    .advanced(false);
        }

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder id(Long id);
            public abstract Builder name(String name);
            public abstract Builder defaultValue(final String defaultValue);
            public abstract Builder matcher(final String matcher);
            public abstract Builder userSettable(final Boolean userSettable);
            public abstract Builder advanced(final Boolean advanced);
            public abstract Builder required(final Boolean required);

            public abstract Input build();
        }
    }

    @AutoValue
    @JsonInclude(Include.ALWAYS)
    public static abstract class Output {
        @Nullable @JsonProperty("id") public abstract Long id();
        @Nullable @JsonProperty("name") public abstract String name();
        @Nullable @JsonProperty("label") public abstract String label();

        @JsonCreator
        public static Output create(@JsonProperty("id") final Long id,
                                    @JsonProperty("name") final String name,
                                    @JsonProperty("label") final String label) {
            return builder()
                    .id(id)
                    .name(name)
                    .label(label)
                    .build();
        }

        // static Output create(final CommandWrapperOutput commandWrapperOutput,
        //                      final CommandConfigInternal.Output output) {
        //     final Builder builder = builder()
        //             .name(commandWrapperOutput.name())
        //             .type(commandWrapperOutput.type())
        //             .label(commandWrapperOutput.label());
        //
        //     if (output != null) {
        //         if (output.label() != null) {
        //             builder.label(output.label());
        //         }
        //     }
        //
        //     return builder.build();
        // }

        public static Output create(final @Nonnull CommandConfigurationEntityOutput commandConfigurationEntityOutput) {
            return builder()
                    .id(commandConfigurationEntityOutput.getId())
                    .name(commandConfigurationEntityOutput.getName())
                    .label(commandConfigurationEntityOutput.getLabel())
                    .build();
        }

        public static Builder builder() {
            return new AutoValue_CommandConfiguration_Output.Builder();
        }

        @AutoValue.Builder
        public static abstract class Builder {
            public abstract Builder id(Long id);
            public abstract Builder name(String name);
            public abstract Builder label(String label);

            public abstract Output build();
        }
    }

    @Nonnull
    public ConfiguredCommand apply(final Command commandWithOneWrapper) {
        // Initialize the command builder copy
        final ConfiguredCommand.Builder commandBuilder = ConfiguredCommand.initialize(commandWithOneWrapper);

        // Things we need to apply configuration to:
        // command inputs
        // wrapper inputs
        // wrapper outputs

        final Map<String, Input> inputsByName = new HashMap<>();
        for (final Input input : this.inputs()) {
            inputsByName.put(input.name(), input);
        }
        final Map<String, Output> outputsByName = new HashMap<>();
        for (final Output output : this.outputs()) {
            outputsByName.put(output.name(), output);
        }


        for (final CommandInput commandInput : commandWithOneWrapper.inputs()) {
            commandBuilder.addInput(
                    inputsByName.containsKey(commandInput.name()) ?
                            commandInput.applyConfiguration(inputsByName.get(commandInput.name())) :
                            commandInput
            );
        }

        final CommandWrapper originalCommandWrapper = commandWithOneWrapper.xnatCommandWrappers().get(0);
        final CommandWrapper.Builder commandWrapperBuilder = CommandWrapper.builder()
                .id(originalCommandWrapper.id())
                .name(originalCommandWrapper.name())
                .description(originalCommandWrapper.description())
                .contexts(originalCommandWrapper.contexts());

        for (final CommandWrapperExternalInput externalInput : originalCommandWrapper.externalInputs()) {
            commandWrapperBuilder.addExternalInput(
                    inputsByName.containsKey(externalInput.name()) ?
                            externalInput.applyConfiguration(inputsByName.get(externalInput.name())) :
                            externalInput
            );
        }

        for (final CommandWrapperDerivedInput derivedInput : originalCommandWrapper.derivedInputs()) {
            commandWrapperBuilder.addDerivedInput(
                    inputsByName.containsKey(derivedInput.name()) ?
                            derivedInput.applyConfiguration(inputsByName.get(derivedInput.name())) :
                            derivedInput
            );
        }

        for (final CommandWrapperOutput output : originalCommandWrapper.outputHandlers()) {
            commandWrapperBuilder.addOutputHandler(
                    outputsByName.containsKey(output.name()) ?
                            output.applyConfiguration(outputsByName.get(output.name())) :
                            output
            );
        }

        return commandBuilder.wrapper(commandWrapperBuilder.build()).build();
    }
}
