package uk.gov.justice.services.jmx.runner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.services.jmx.api.command.SystemCommand;

import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RunSystemCommandTaskTest {

    private static final UUID commandId = randomUUID();
    private static final Optional<UUID> commandRuntimeId = empty();

    @Mock
    private SystemCommandRunner systemCommandRunner;

    @Mock
    private SystemCommand systemCommand;

    @Mock
    private SystemCommandInvocationFailureHandler systemCommandInvocationFailureHandler;

    private RunSystemCommandTask runSystemCommandTask;

    @BeforeEach
    public void setUp() {
        runSystemCommandTask = new RunSystemCommandTask(
                systemCommandRunner,
                systemCommand,
                commandId,
                commandRuntimeId,
                systemCommandInvocationFailureHandler
        );
    }

    @Test
    public void shouldRunTheSystemCommand() throws Exception {
        runSystemCommandTask.call();

        verify(systemCommandRunner).run(systemCommand, commandId, commandRuntimeId);
    }

    @Test
    public void onFailureShouldInvokeExceptionHandler() throws Exception {
        final RuntimeException exception = new RuntimeException();
        doThrow(exception).when(systemCommandRunner).run(systemCommand, commandId, commandRuntimeId);

        runSystemCommandTask.call();

        verify(systemCommandInvocationFailureHandler).handle(exception, systemCommand, commandId, commandRuntimeId);
    }
}
