package org.nrg.containers.model.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
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
import java.util.Map;
import java.util.Set;

/**
 * This class was built on top of the config service. Use the newer
 * {@link CommandConfiguration} class instead, which is built on a
 * dedicated Hibernate class {@link CommandConfigurationEntity} and
 * service {@link org.nrg.containers.services.impl.HibernateCommandConfigurationEntityService}.
 */
@AutoValue
@JsonInclude(Include.ALWAYS)
@Deprecated
public abstract class CommandConfig {
    @JsonProperty("inputs") public abstract ImmutableMap<String, Input> inputs();
    @JsonProperty("outputs") public abstract ImmutableMap<String, Output> outputs();

    @JsonCreator
    public static CommandConfig create(@JsonProperty("inputs") final Map<String, Input> inputs,
                                       @JsonProperty("outputs") final Map<String, Output> outputs) {
        return builder()
                .inputs(inputs == null ? Collections.<String, Input>emptyMap() : inputs)
                .outputs(outputs == null ? Collections.<String, Output>emptyMap() : outputs)
                .build();
    }

    public static CommandConfig create(final @Nonnull Command command,
                                       final @Nonnull CommandWrapper commandWrapper,
                                       final @Nullable CommandConfigInternal commandConfigInternal) {
        Builder builder = builder();
        final Set<String> handledCommandInputs = Sets.newHashSet();

        final Map<String, CommandConfigInternal.Input> configuredInputs
                = commandConfigInternal == null ?
                        Collections.<String, CommandConfigInternal.Input>emptyMap() :
                        commandConfigInternal.inputs();
        final Map<String, CommandConfigInternal.Output> configuredOutputs
                = commandConfigInternal == null ?
                        Collections.<String, CommandConfigInternal.Output>emptyMap() :
                        commandConfigInternal.outputs();

        for (final Command.CommandWrapperExternalInput externalInput : commandWrapper.externalInputs()) {
            builder = builder.addInput(externalInput.name(),
                    CommandConfig.Input.create(externalInput, configuredInputs.get(externalInput.name())));
            handledCommandInputs.add(externalInput.providesValueForCommandInput());
        }
        for (final Command.CommandWrapperDerivedInput derivedInput : commandWrapper.derivedInputs()) {
            builder = builder.addInput(derivedInput.name(),
                    CommandConfig.Input.create(derivedInput, configuredInputs.get(derivedInput.name())));
            handledCommandInputs.add(derivedInput.providesValueForCommandInput());
        }
        for (final Command.CommandWrapperOutput wrapperOutput : commandWrapper.outputHandlers()) {
            builder = builder.addOutput(wrapperOutput.name(),
                    Output.create(wrapperOutput, configuredOutputs.get(wrapperOutput.name())));
        }

        for (final Command.CommandInput commandInput : command.inputs()) {
            if (!handledCommandInputs.contains(commandInput.name())) {
                builder = builder.addInput(commandInput.name(),
                        CommandConfig.Input.create(commandInput, configuredInputs.get(commandInput.name())));
            }
        }

        return builder.build();
    }

    @Nonnull
    public static CommandConfig create(final @Nonnull CommandConfiguration newStyleConfig) {
        Builder builder = builder();

        for (final CommandConfiguration.Input input : newStyleConfig.inputs()) {
            builder.addInput(input.name(), Input.create(input));
        }

        for (final CommandConfiguration.Output output : newStyleConfig.outputs()) {
            builder.addOutput(output.name(), Output.create(output));
        }

        return builder.build();
    }

    public static Builder builder() {
        return new AutoValue_CommandConfig.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder inputs(@Nonnull Map<String, Input> inputs);
        abstract ImmutableMap.Builder<String, Input> inputsBuilder();
        public Builder addInput(final @Nonnull String inputName, final Input input) {
            inputsBuilder().put(inputName, input);
            return this;
        }

        public abstract Builder outputs(@Nonnull Map<String, Output> outputs);
        abstract ImmutableMap.Builder<String, Output> outputsBuilder();
        public Builder addOutput(final @Nonnull String outputName, final Output output) {
            outputsBuilder().put(outputName, output);
            return this;
        }

        public abstract CommandConfig build();
    }

    @AutoValue
    @JsonInclude(Include.ALWAYS)
    public static abstract class Input {
        @Nullable @JsonProperty("description") public abstract String description();
        @Nullable @JsonProperty("type") public abstract String type();
        @Nullable @JsonProperty("default-value") public abstract String defaultValue();
        @Nullable @JsonProperty("matcher") public abstract String matcher();
        @Nullable @JsonProperty("user-settable") public abstract Boolean userSettable();
        @Nullable @JsonProperty("advanced") public abstract Boolean advanced();
        @Nullable @JsonProperty("required") public abstract Boolean required();

        @JsonCreator
        static Input create(@JsonProperty("description") final String description,
                            @JsonProperty("type") final String type,
                            @JsonProperty("default-value") final String defaultValue,
                            @JsonProperty("matcher") final String matcher,
                            @JsonProperty("user-settable") final Boolean userSettable,
                            @JsonProperty("advanced") final Boolean advanced,
                            @JsonProperty("required") final Boolean required) {
            return builder()
                    .description(description)
                    .type(type)
                    .defaultValue(defaultValue)
                    .matcher(matcher)
                    .userSettable(userSettable)
                    .advanced(advanced)
                    .required(required)
                    .build();
        }

        static Input create(final Command.CommandInput commandInput,
                            final CommandConfigInternal.Input input) {
            final Builder builder = builder()
                    .description(commandInput.description())
                    .type(commandInput.type())
                    .defaultValue(commandInput.defaultValue())
                    .matcher(commandInput.matcher())
                    .required(commandInput.required());

            if (input != null) {
                // Set those things that the command input does not have, even if they are null
                builder.userSettable(input.userSettable())
                        .advanced(input.advanced());

                // Override those things the command input does have only if they are not null
                if (input.defaultValue() != null) {
                    builder.defaultValue(input.defaultValue());
                }
                if (input.matcher() != null) {
                    builder.matcher(input.matcher());
                }
            }

            return builder.build();
        }

        static Input create(final Command.CommandWrapperInput commandWrapperInput,
                            final CommandConfigInternal.Input input) {
            final Builder builder = builder()
                    .description(commandWrapperInput.description())
                    .type(commandWrapperInput.type())
                    .defaultValue(commandWrapperInput.defaultValue())
                    .matcher(commandWrapperInput.matcher())
                    .userSettable(commandWrapperInput.userSettable());

            if (input != null) {
                // Set those things that the command wrapper input does not have, even if they are null
                builder.advanced(input.advanced());

                // Override those things the command wrapper input does have only if they are not null
                if (input.defaultValue() != null) {
                    builder.defaultValue(input.defaultValue());
                }
                if (input.matcher() != null) {
                    builder.matcher(input.matcher());
                }
                if (input.userSettable() != null) {
                    builder.userSettable(input.userSettable());
                }
            }

            return builder.build();
        }

        static Input create(final CommandConfiguration.Input newStyleInput) {
            return builder()
                    .description("")
                    .type("")
                    .defaultValue(newStyleInput.defaultValue())
                    .matcher(newStyleInput.matcher())
                    .userSettable(newStyleInput.userSettable())
                    .advanced(newStyleInput.advanced())
                    .required(newStyleInput.required())
                    .build();
        }

        public static Builder builder() {
            return new AutoValue_CommandConfig_Input.Builder()
                    .userSettable(true)
                    .advanced(false);
        }

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder description(String description);
            public abstract Builder type(String type);
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
        @Nullable @JsonProperty("type") public abstract String type();
        @Nullable @JsonProperty("label") public abstract String label();

        @JsonCreator
        public static Output create(@JsonProperty("type") final String type,
                                    @JsonProperty("label") final String label) {
            return builder()
                    .type(type)
                    .label(label)
                    .build();
        }

        static Output create(final Command.CommandWrapperOutput commandWrapperOutput,
                             final CommandConfigInternal.Output output) {
            final Builder builder = builder()
                    .type(commandWrapperOutput.type())
                    .label(commandWrapperOutput.label());

            if (output != null) {
                if (output.label() != null) {
                    builder.label(output.label());
                }
            }

            return builder.build();
        }

        @Nonnull
        static Output create(final @Nonnull CommandConfiguration.Output newStyleOutput) {
            return builder()
                    .label(newStyleOutput.label())
                    .build();
        }

        public static Builder builder() {
            return new AutoValue_CommandConfig_Output.Builder();
        }

        @AutoValue.Builder
        public static abstract class Builder {
            public abstract Builder type(String type);
            public abstract Builder label(String label);

            public abstract Output build();
        }
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    public ConfiguredCommand apply(final Command commandWithOneWrapper) {
        // Initialize the command builder copy
        final ConfiguredCommand.Builder commandBuilder = ConfiguredCommand.initialize(commandWithOneWrapper);

        // Things we need to apply configuration to:
        // command inputs
        // wrapper inputs
        // wrapper outputs

        for (final CommandInput commandInput : commandWithOneWrapper.inputs()) {
            commandBuilder.addInput(
                    this.inputs().containsKey(commandInput.name()) ?
                            commandInput.applyConfiguration(this.inputs().get(commandInput.name())) :
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
                    this.inputs().containsKey(externalInput.name()) ?
                            externalInput.applyConfiguration(this.inputs().get(externalInput.name())) :
                            externalInput
            );
        }

        for (final CommandWrapperDerivedInput derivedInput : originalCommandWrapper.derivedInputs()) {
            commandWrapperBuilder.addDerivedInput(
                    this.inputs().containsKey(derivedInput.name()) ?
                            derivedInput.applyConfiguration(this.inputs().get(derivedInput.name())) :
                            derivedInput
            );
        }

        for (final CommandWrapperOutput output : originalCommandWrapper.outputHandlers()) {
            commandWrapperBuilder.addOutputHandler(
                    this.outputs().containsKey(output.name()) ?
                            output.applyConfiguration(this.outputs().get(output.name())) :
                            output
            );
        }

        return commandBuilder.wrapper(commandWrapperBuilder.build()).build();
    }
}
