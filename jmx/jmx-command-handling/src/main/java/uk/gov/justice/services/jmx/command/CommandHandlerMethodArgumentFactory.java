package uk.gov.justice.services.jmx.command;

import uk.gov.justice.services.jmx.api.command.SystemCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CommandHandlerMethodArgumentFactory {

    public Object[] createMethodArguments(
            final SystemCommand systemCommand,
            final UUID commandId,
            final Optional<UUID> commandRuntimeId) {

        final List<Object> methodArguments = new ArrayList<>();
        methodArguments.add(systemCommand);
        methodArguments.add(commandId);

        commandRuntimeId.ifPresent(methodArguments::add);

        return methodArguments.toArray();
    }
}
