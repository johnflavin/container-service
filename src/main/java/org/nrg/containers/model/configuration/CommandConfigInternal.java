package org.nrg.containers.model.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;

/**
 * This class was built on top of the config service. Use the newer
 * {@link CommandConfiguration} class instead, which is built on a
 * dedicated Hibernate class {@link CommandConfigurationEntity} and
 * service {@link org.nrg.containers.services.impl.HibernateCommandConfigurationEntityService}.
 */
@AutoValue
@JsonInclude(JsonInclude.Include.ALWAYS)
@Deprecated
public abstract class CommandConfigInternal {
    @Nullable @JsonProperty("enabled") public abstract Boolean enabled();
    @JsonProperty("inputs") abstract ImmutableMap<String, Input> inputs();
    @JsonProperty("outputs") abstract ImmutableMap<String, Output> outputs();

    @JsonCreator
    public static CommandConfigInternal create(@JsonProperty("enabled") final Boolean enabled,
                                               @JsonProperty("inputs") final Map<String, Input> inputs,
                                               @JsonProperty("outputs") final Map<String, Output> outputs) {
        return builder()
                .enabled(enabled)
                .inputs(inputs == null ? Collections.<String, Input>emptyMap() : inputs)
                .outputs(outputs == null ? Collections.<String, Output>emptyMap() : outputs)
                .build();
    }

    public static CommandConfigInternal create(final Boolean enabled,
                                               final CommandConfig configuration) {
        final Builder builder = builder().enabled(enabled);

        if (configuration != null) {
            for (final Map.Entry<String, CommandConfig.Input> inputEntry : configuration.inputs().entrySet()) {
                builder.addInput(inputEntry.getKey(), inputEntry.getValue());
            }
            for (final Map.Entry<String, CommandConfig.Output> outputEntry : configuration.outputs().entrySet()) {
                builder.addOutput(outputEntry.getKey(), outputEntry.getValue());
            }
        }
        return builder.build();
    }

    public static Builder builder() {
        return new AutoValue_CommandConfigInternal.Builder();
    }

    public abstract Builder toBuilder();

    public CommandConfigInternal merge(final CommandConfigInternal overlay, final boolean enabled) {
        if (overlay == null) {
            return this;
        }

        final Map<String, Input> mergedInputs = Maps.newHashMap(this.inputs());
        for (final Map.Entry<String, Input> otherInput : overlay.inputs().entrySet()) {
            final Input thisInputValue = this.inputs().get(otherInput.getKey());
            mergedInputs.put(otherInput.getKey(),
                    thisInputValue == null ? otherInput.getValue() : thisInputValue.merge(otherInput.getValue()));
        }

        final Map<String, Output> mergedOutputs = Maps.newHashMap(this.outputs());
        for (final Map.Entry<String, Output> otherOutput : overlay.outputs().entrySet()) {
            final Output thisOutputValue = this.outputs().get(otherOutput.getKey());
            mergedOutputs.put(otherOutput.getKey(),
                    thisOutputValue == null ? otherOutput.getValue() : thisOutputValue.merge(otherOutput.getValue()));
        }

        return builder()
                .enabled(enabled)
                .inputs(mergedInputs)
                .outputs(mergedOutputs)
                .build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder enabled(Boolean enabled);

        public abstract Builder inputs(@Nonnull Map<String, Input> inputs);
        abstract ImmutableMap.Builder<String, Input> inputsBuilder();
        public Builder addInput(final @Nonnull String inputName, final Input input) {
            inputsBuilder().put(inputName, input);
            return this;
        }
        public Builder addInput(final @Nonnull String inputName, final CommandConfig.Input input) {
            inputsBuilder().put(inputName, Input.create(input));
            return this;
        }

        public abstract Builder outputs(@Nonnull Map<String, Output> outputs);
        abstract ImmutableMap.Builder<String, Output> outputsBuilder();
        public Builder addOutput(final @Nonnull String outputName, final Output output) {
            outputsBuilder().put(outputName, output);
            return this;
        }
        public Builder addOutput(final @Nonnull String outputName, final CommandConfig.Output output) {
            outputsBuilder().put(outputName, CommandConfigInternal.Output.create(output));
            return this;
        }

        public abstract CommandConfigInternal build();
    }

    @AutoValue
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static abstract class Input {
        @Nullable @JsonProperty("default-value") public abstract String defaultValue();
        @Nullable @JsonProperty("matcher") public abstract String matcher();
        @Nullable @JsonProperty("user-settable") public abstract Boolean userSettable();
        @Nullable @JsonProperty("advanced") public abstract Boolean advanced();

        @JsonCreator
        static Input create(@JsonProperty("default-value") final String defaultValue,
                            @JsonProperty("matcher") final String matcher,
                            @JsonProperty("user-settable") final Boolean userSettable,
                            @JsonProperty("advanced") final Boolean advanced) {
            return builder()
                    .defaultValue(defaultValue)
                    .matcher(matcher)
                    .userSettable(userSettable)
                    .advanced(advanced)
                    .build();
        }

        static Input create(final CommandConfig.Input input) {
            return builder()
                    .defaultValue(input.defaultValue())
                    .matcher(input.matcher())
                    .userSettable(input.userSettable())
                    .advanced(input.advanced())
                    .build();
        }

        public static Input.Builder builder() {
            return new AutoValue_CommandConfigInternal_Input.Builder();
        }

        Input merge(final Input that) {
            if (that == null) {
                return this;
            }
            return create(that.defaultValue() == null ? this.defaultValue() : that.defaultValue(),
                    that.matcher() == null ? this.matcher() : that.matcher(),
                    that.userSettable() == null ? this.userSettable() : that.userSettable(),
                    that.advanced() == null ? this.advanced() : that.advanced());
        }

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder defaultValue(final String defaultValue);
            public abstract Builder matcher(final String matcher);
            public abstract Builder userSettable(final Boolean userSettable);
            public abstract Builder advanced(final Boolean advanced);

            public abstract Input build();
        }
    }

    @AutoValue
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static abstract class Output {
        @Nullable @JsonProperty("label") public abstract String label();

        @JsonCreator
        public static Output create(@JsonProperty("label") final String label) {
            return new AutoValue_CommandConfigInternal_Output(label);
        }

        static Output create(final CommandConfig.Output output) {
            return create(output.label());
        }

        Output merge(final Output that) {
            if (that == null) {
                return this;
            }
            return create(that.label() == null ? this.label() : that.label());
        }
    }
}
