package uk.gov.justice.services.jmx.command;

import uk.gov.justice.services.jmx.api.command.SystemCommand;
import uk.gov.justice.services.jmx.api.parameters.JmxCommandRuntimeParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandHandlerMethodArgumentFactory {

    public Object[] createMethodArguments(
            final SystemCommand systemCommand,
            final UUID commandId,
            final JmxCommandRuntimeParameters jmxCommandRuntimeParameters) {

        final List<Object> methodArguments = new ArrayList<>();
        methodArguments.add(systemCommand);
        methodArguments.add(commandId);
        methodArguments.add(jmxCommandRuntimeParameters);

        return methodArguments.toArray();
    }
}
