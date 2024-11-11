package uk.gov.justice.services.jmx.api.parameters;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class JmxCommandRuntimeParameters implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID commandRuntimeId;
    private final String commandRuntimeString;

    public JmxCommandRuntimeParameters(final UUID commandRuntimeId, final String commandRuntimeString) {
        this.commandRuntimeId = commandRuntimeId;
        this.commandRuntimeString = commandRuntimeString;
    }

    public UUID getCommandRuntimeId() {
        return commandRuntimeId;
    }

    public String getCommandRuntimeString() {
        return commandRuntimeString;
    }

    public static JmxCommandRuntimeParameters withNoCommandParameters() {
        return new JmxCommandRuntimeParameters(null, null);
    }

    @Override
    public String toString() {

        final StringBuilder stringBuilder = new StringBuilder("JmxCommandRuntimeParameters{");

        if (commandRuntimeId != null) {
            stringBuilder.append("commandRuntimeId='").append(commandRuntimeId).append("'");
        }

        if (commandRuntimeString != null) {
            if (commandRuntimeId != null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("commandRuntimeString='").append(commandRuntimeString).append("'");
        }

        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final JmxCommandRuntimeParameters that)) return false;
        return Objects.equals(commandRuntimeId, that.commandRuntimeId) && Objects.equals(commandRuntimeString, that.commandRuntimeString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandRuntimeId, commandRuntimeString);
    }

    public static class JmxCommandRuntimeParametersBuilder {

        private UUID commandRuntimeId;
        private String commandRuntimeString;

        public JmxCommandRuntimeParametersBuilder withCommandRuntimeId(final UUID commandRuntimeId) {
            this.commandRuntimeId = commandRuntimeId;
            return this;
        }

        public JmxCommandRuntimeParametersBuilder withCommandRuntimeString(final String commandRuntimeString) {
            this.commandRuntimeString = commandRuntimeString;
            return this;
        }

        public JmxCommandRuntimeParameters build() {
            return new JmxCommandRuntimeParameters(commandRuntimeId, commandRuntimeString);
        }
    }
}
