package uk.gov.justice.services.management.ping.handler;

import static java.lang.String.format;
import static uk.gov.justice.services.jmx.api.domain.CommandState.COMMAND_COMPLETE;
import static uk.gov.justice.services.jmx.api.domain.CommandState.COMMAND_IN_PROGRESS;
import static uk.gov.justice.services.management.ping.commands.LogRuntimeIdCommand.LOG_RUNTIME_ID;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.jmx.command.HandlesSystemCommand;
import uk.gov.justice.services.jmx.state.events.SystemCommandStateChangedEvent;
import uk.gov.justice.services.management.ping.commands.LogRuntimeIdCommand;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.slf4j.Logger;

public class LogRuntimeIdHandler {

    @Inject
    private Event<SystemCommandStateChangedEvent> systemCommandStateChangedEventFirer;

    @Inject
    private UtcClock clock;

    @Inject
    private Logger logger;

    @HandlesSystemCommand(LOG_RUNTIME_ID)
    public void logRuntimeId(final LogRuntimeIdCommand logRuntimeIdCommand, @SuppressWarnings("unused") final UUID commandId, final UUID commandRuntimeId) {

        final ZonedDateTime startedAt = clock.now();
        systemCommandStateChangedEventFirer.fire(new SystemCommandStateChangedEvent(
                commandId,
                logRuntimeIdCommand,
                COMMAND_IN_PROGRESS,
                startedAt,
                "LogRuntimeId command received"
        ));

        logger.info(format("********** Received system command '%s' with %s '%s' at %s **********", LOG_RUNTIME_ID, logRuntimeIdCommand.commandRuntimeIdType(), commandRuntimeId, startedAt));

        systemCommandStateChangedEventFirer.fire(new SystemCommandStateChangedEvent(
                commandId,
                logRuntimeIdCommand,
                COMMAND_COMPLETE,
                clock.now(),
                "LogRuntimeId command complete"
        ));
    }
}
