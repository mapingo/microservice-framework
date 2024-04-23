package uk.gov.justice.services.jmx.runner;

import org.slf4j.Logger;
import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.jmx.api.command.SystemCommand;
import uk.gov.justice.services.jmx.state.events.SystemCommandStateChangedEvent;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static uk.gov.justice.services.jmx.api.domain.CommandState.COMMAND_FAILED;

public class SystemCommandInvocationFailureHandler {

    @Inject
    private Logger logger;

    @Inject
    private Event<SystemCommandStateChangedEvent> stateChangedEventFirer;

    @Inject
    private UtcClock clock;

    public void handle(final Exception e, final SystemCommand systemCommand,
                       final UUID commandId,
                       final Optional<UUID> commandRuntimeId) {
        final StringBuilder message = new StringBuilder(format("Failed to run System Command '%s'", systemCommand.getName()));

        commandRuntimeId.ifPresent(commandRuntimeUuid -> message.append(format(" for %s '%s'", systemCommand.commandRuntimeIdType(), commandRuntimeUuid)));

        logger.error(message.toString(), e);

        final String errorMessage = message.append(". Caused by ")
                .append(e.getClass().getName())
                .append(": ")
                .append(e.getMessage())
                .toString();

        stateChangedEventFirer.fire(new SystemCommandStateChangedEvent(
                commandId,
                systemCommand,
                COMMAND_FAILED,
                clock.now(),
                errorMessage
        ));
    }
}
