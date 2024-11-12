package uk.gov.justice.services.jmx.api.mbean;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

import uk.gov.justice.services.jmx.api.UnrunnableSystemCommandException;
import uk.gov.justice.services.jmx.api.command.SystemCommand;
import uk.gov.justice.services.jmx.api.parameters.JmxCommandRuntimeParameters;

import java.util.Optional;
import java.util.UUID;

public class JmxCommandVerifier {

    public void verify(final SystemCommand systemCommand, final JmxCommandRuntimeParameters jmxCommandRuntimeParameters) {

        final Optional<UUID> commandRuntimeId = ofNullable(jmxCommandRuntimeParameters.getCommandRuntimeId());
        final Optional<String> commandRuntimeString = ofNullable(jmxCommandRuntimeParameters.getCommandRuntimeString());

        if (systemCommand.requiresCommandRuntimeId() && commandRuntimeId.isEmpty()) {
            throw new UnrunnableSystemCommandException(format("The JMX command '%s' requires '%s' to be able to run. Please re-run using the '--command-runtime-id' switch",
                    systemCommand.getName(),
                    systemCommand.commandRuntimeIdType()));
        }
        if (systemCommand.requiresCommandRuntimeString() && commandRuntimeString.isEmpty()) {
            throw new UnrunnableSystemCommandException(format("The JMX command '%s' requires '%s' to be able to run. Please re-run using the '--command-runtime-string' switch",
                    systemCommand.getName(),
                    systemCommand.commandRuntimeStringType()));
        }
    }
}
