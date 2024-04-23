package uk.gov.justice.services.jmx.runner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.jmx.api.command.SystemCommand;
import uk.gov.justice.services.jmx.state.events.SystemCommandStateChangedEvent;

import javax.enterprise.event.Event;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static uk.gov.justice.services.jmx.api.domain.CommandState.COMMAND_FAILED;

@ExtendWith(MockitoExtension.class)
public class SystemCommandInvocationFailureHandlerTest {

    private static final UUID commandId = UUID.fromString("a3b3b3b3-3b3b-3b3b-3b3b-3b3b3b3b3b3b");

    @Mock
    private Logger logger;

    @Mock
    private Event<SystemCommandStateChangedEvent> stateChangedEventFirer;

    @Mock
    private UtcClock clock;

    @InjectMocks
    private SystemCommandInvocationFailureHandler systemCommandInvocationFailureHandler;

    @Test
    public void shouldLogErrorAndRaiseEventGivenCommandRuntimeId() {
        final RuntimeException e = new RuntimeException("exceptionMessage");
        final Optional<UUID> commandRuntimeId = Optional.of(UUID.fromString("d3b3b3b3-3b3b-3b3b-3b3b-3b3b3b3b3b3b"));
        final String commandName = "Ping";
        final String commandRuntimeIdType = "eventId";
        final SystemCommand systemCommand = mock(SystemCommand.class);
        final ZonedDateTime now = ZonedDateTime.now();
        when(systemCommand.getName()).thenReturn(commandName);
        when(systemCommand.commandRuntimeIdType()).thenReturn(commandRuntimeIdType);
        when(clock.now()).thenReturn(now);

        systemCommandInvocationFailureHandler.handle(e, systemCommand, commandId, commandRuntimeId);

        final String expectedErrorMessage = "Failed to run System Command 'Ping' for eventId 'd3b3b3b3-3b3b-3b3b-3b3b-3b3b3b3b3b3b'";
        verify(logger).error(expectedErrorMessage, e);
        verify(stateChangedEventFirer).fire(new SystemCommandStateChangedEvent(
                commandId,
                systemCommand,
                COMMAND_FAILED,
                now,
                expectedErrorMessage + ". Caused by java.lang.RuntimeException: exceptionMessage"
        ));
    }

    @Test
    public void shouldLogErrorAndRaiseEventGivenNoCommandRuntimeId() {
        final RuntimeException e = new RuntimeException("exceptionMessage");
        final String commandName = "Ping";
        final SystemCommand systemCommand = mock(SystemCommand.class);
        final ZonedDateTime now = ZonedDateTime.now();
        when(systemCommand.getName()).thenReturn(commandName);
        when(clock.now()).thenReturn(now);

        systemCommandInvocationFailureHandler.handle(e, systemCommand, commandId, Optional.empty());

        final String expectedErrorMessage = "Failed to run System Command 'Ping'";
        verify(logger).error(expectedErrorMessage, e);
        verify(stateChangedEventFirer).fire(new SystemCommandStateChangedEvent(
                commandId,
                systemCommand,
                COMMAND_FAILED,
                now,
                expectedErrorMessage + ". Caused by java.lang.RuntimeException: exceptionMessage"
        ));
    }
}