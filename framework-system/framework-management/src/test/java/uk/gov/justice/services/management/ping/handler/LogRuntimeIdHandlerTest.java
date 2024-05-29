package uk.gov.justice.services.management.ping.handler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.jmx.state.events.SystemCommandStateChangedEvent;
import uk.gov.justice.services.management.ping.commands.LogRuntimeIdCommand;

import javax.enterprise.event.Event;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.of;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.jmx.api.domain.CommandState.COMMAND_COMPLETE;
import static uk.gov.justice.services.jmx.api.domain.CommandState.COMMAND_IN_PROGRESS;

@ExtendWith(MockitoExtension.class)
public class LogRuntimeIdHandlerTest {

    @Mock
    private UtcClock clock;

    @Mock
    private Logger logger;

    @Mock
    private Event<SystemCommandStateChangedEvent> systemCommandStateChangedEventFirer;

    @InjectMocks
    private LogRuntimeIdHandler logRuntimeIdHandler;

    @Captor
    private ArgumentCaptor<SystemCommandStateChangedEvent> systemCommandStateChangedEventCaptor;

    @Test
    public void shouldLogCommandRuntimeId() throws Exception {

        final UUID commandId = randomUUID();
        final UUID commandRuntimeId = fromString("aabcf155-7d9b-4f4d-8f83-fcbc221c0b31");
        final LogRuntimeIdCommand logRuntimeIdCommand = new LogRuntimeIdCommand();

        final ZonedDateTime startedAt = of(2024, 3, 28, 15, 22, 25, 0, UTC);
        final ZonedDateTime completeAt = startedAt.plusSeconds(2);

        when(clock.now()).thenReturn(startedAt, completeAt);

        logRuntimeIdHandler.logRuntimeId(logRuntimeIdCommand, commandId, commandRuntimeId);

        final InOrder inOrder = inOrder(systemCommandStateChangedEventFirer, logger);

        inOrder.verify(systemCommandStateChangedEventFirer).fire(systemCommandStateChangedEventCaptor.capture());
        inOrder.verify(logger).info("********** Received system command 'LOG_RUNTIME_ID' with command-runtime-id 'aabcf155-7d9b-4f4d-8f83-fcbc221c0b31' at 2024-03-28T15:22:25Z **********");
        inOrder.verify(systemCommandStateChangedEventFirer).fire(systemCommandStateChangedEventCaptor.capture());

        final List<SystemCommandStateChangedEvent> allValues = systemCommandStateChangedEventCaptor.getAllValues();

        final SystemCommandStateChangedEvent startEvent = allValues.get(0);
        final SystemCommandStateChangedEvent completeEvent = allValues.get(1);

        assertThat(startEvent.getCommandId(), is(commandId));
        assertThat(startEvent.getCommandState(), is(COMMAND_IN_PROGRESS));
        assertThat(startEvent.getSystemCommand(), is(logRuntimeIdCommand));
        assertThat(startEvent.getStatusChangedAt(), is(startedAt));
        assertThat(startEvent.getMessage(), is("LogRuntimeId command received"));

        assertThat(completeEvent.getCommandId(), is(commandId));
        assertThat(completeEvent.getCommandState(), is(COMMAND_COMPLETE));
        assertThat(completeEvent.getSystemCommand(), is(logRuntimeIdCommand));
        assertThat(completeEvent.getStatusChangedAt(), is(completeAt));
        assertThat(completeEvent.getMessage(), is("LogRuntimeId command complete"));
    }
}
