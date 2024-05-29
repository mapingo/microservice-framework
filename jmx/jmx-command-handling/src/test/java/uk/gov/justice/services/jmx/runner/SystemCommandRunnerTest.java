package uk.gov.justice.services.jmx.runner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import uk.gov.justice.services.jmx.command.SystemCommandHandlerProxy;
import uk.gov.justice.services.jmx.command.SystemCommandStore;
import uk.gov.justice.services.jmx.command.TestCommand;

import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SystemCommandRunnerTest {

    @Mock
    private SystemCommandStore systemCommandStore;

    @Mock
    private Logger logger;

    @InjectMocks
    private SystemCommandRunner systemCommandRunner;

    @Test
    public void shouldFindTheCorrectProxyForTheCommandAndInvokeGivenCommandRuntimeId() throws Exception {

        final UUID commandId = randomUUID();
        final TestCommand testCommand = new TestCommand();
        final Optional<UUID> commandRuntimeId = of(UUID.fromString("d3b3b3b3-3b3b-3b3b-3b3b-3b3b3b3b3b3b"));

        final SystemCommandHandlerProxy systemCommandHandlerProxy = mock(SystemCommandHandlerProxy.class);

        when(systemCommandStore.findCommandProxy(testCommand)).thenReturn(systemCommandHandlerProxy);

        systemCommandRunner.run(testCommand, commandId, commandRuntimeId);

        verify(systemCommandHandlerProxy).invokeCommand(testCommand, commandId, commandRuntimeId);
        verify(logger).info("Running system command 'TEST_COMMAND' with EVENT_ID 'd3b3b3b3-3b3b-3b3b-3b3b-3b3b3b3b3b3b'");
    }

    @Test
    public void logGivenNoCommandRuntimeId() throws Exception {

        final UUID commandId = randomUUID();
        final TestCommand testCommand = new TestCommand();

        final SystemCommandHandlerProxy systemCommandHandlerProxy = mock(SystemCommandHandlerProxy.class);

        when(systemCommandStore.findCommandProxy(testCommand)).thenReturn(systemCommandHandlerProxy);

        systemCommandRunner.run(testCommand, commandId, Optional.empty());

        verify(logger).info("Running system command 'TEST_COMMAND'");
    }
}
