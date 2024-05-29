package uk.gov.justice.services.jmx.api.mbean;

import static java.lang.String.format;

import uk.gov.justice.services.jmx.api.UnrunnableSystemCommandException;
import uk.gov.justice.services.jmx.api.command.SystemCommand;

import java.util.Optional;
import java.util.UUID;

public class SystemCommandVerifier {

    public void verify(final SystemCommand systemCommand, final Optional<UUID> commandRuntimeId) {

        if (systemCommand.requiresCommandRuntimeId() && ! commandRuntimeId.isPresent()) {
            throw new UnrunnableSystemCommandException(format("The JMX command '%s' requires '%s' to be able to run. Please re-run using the '--command-runtime-id' switch",
                    systemCommand.getName(),
                    systemCommand.commandRuntimeIdType()));
        }
    }
}
