package uk.gov.justice.services.jmx.api.mbean;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.jmx.api.mbean.CommandRunMode.FORCED;
import static uk.gov.justice.services.jmx.api.mbean.CommandRunMode.GUARDED;
import static uk.gov.justice.services.jmx.api.parameters.JmxCommandRuntimeParameters.withNoCommandParameters;

import uk.gov.justice.services.jmx.api.UnrunnableSystemCommandException;
import uk.gov.justice.services.jmx.api.command.SystemCommand;
import uk.gov.justice.services.jmx.api.parameters.JmxCommandRuntimeParameters;
import uk.gov.justice.services.jmx.command.SystemCommandLocator;
import uk.gov.justice.services.jmx.command.TestCommand;
import uk.gov.justice.services.jmx.runner.AsynchronousCommandRunner;
import uk.gov.justice.services.jmx.state.observers.SystemCommandStateBean;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JmxCommandRunnerTest {

    @Mock
    private SystemCommandLocator systemCommandLocator;

    @Mock
    private AsynchronousCommandRunner asynchronousCommandRunner;

    @Mock
    private SystemCommandStateBean systemCommandStateBean;

    @Mock
    private JmxCommandVerifier jmxCommandVerifier;

    @InjectMocks
    private JmxCommandRunner jmxCommandRunner;

    @Test
    public void shouldRunSystemCommandInGuardedMode() throws Exception {

        final SystemCommand systemCommand = new TestCommand();
        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = withNoCommandParameters();
        final CommandRunMode commandRunMode = GUARDED;
        final UUID commandId = randomUUID();

        when(systemCommandLocator.forName(systemCommand.getName())).thenReturn(of(systemCommand));
        when(systemCommandStateBean.commandInProgress(systemCommand)).thenReturn(false);
        when(asynchronousCommandRunner.run(systemCommand, jmxCommandRuntimeParameters)).thenReturn(commandId);

        assertThat(jmxCommandRunner.run(systemCommand.getName(), jmxCommandRuntimeParameters, commandRunMode), is(commandId));

        verify(jmxCommandVerifier).verify(systemCommand, jmxCommandRuntimeParameters);
    }

    @Test
    public void shouldFailIfCommandNotFound() throws Exception {

        final SystemCommand systemCommand = new TestCommand();
        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = withNoCommandParameters();
        final CommandRunMode commandRunMode = GUARDED;

        when(systemCommandLocator.forName(systemCommand.getName())).thenReturn(empty());

        final UnrunnableSystemCommandException unrunnableSystemCommandException = assertThrows(UnrunnableSystemCommandException.class,
                () -> jmxCommandRunner.run(
                        systemCommand.getName(),
                        jmxCommandRuntimeParameters,
                        commandRunMode));

        assertThat(unrunnableSystemCommandException.getMessage(), is("The system command 'TEST_COMMAND' is not supported on this context."));

        verifyNoInteractions(asynchronousCommandRunner);
    }

    @Test
    public void shouldFailIfRunningSystemCommandInGuardedModeWhileCommandIsInProgress() throws Exception {

        final SystemCommand systemCommand = new TestCommand();
        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = withNoCommandParameters();
        final CommandRunMode commandRunMode = GUARDED;

        when(systemCommandLocator.forName(systemCommand.getName())).thenReturn(of(systemCommand));
        when(systemCommandStateBean.commandInProgress(systemCommand)).thenReturn(true);


        final UnrunnableSystemCommandException unrunnableSystemCommandException = assertThrows(UnrunnableSystemCommandException.class,
                () -> jmxCommandRunner.run(
                        systemCommand.getName(),
                        jmxCommandRuntimeParameters,
                        commandRunMode));

        assertThat(unrunnableSystemCommandException.getMessage(), is("Cannot run system command 'TEST_COMMAND'. A previous call to that command is still in progress."));

        verify(jmxCommandVerifier).verify(systemCommand, jmxCommandRuntimeParameters);
        verifyNoInteractions(asynchronousCommandRunner);
    }

    @Test
    public void shouldNotCheckIfCommandIsInProgressIfModeIsForced() throws Exception {

        final SystemCommand systemCommand = new TestCommand();
        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = withNoCommandParameters();
        final CommandRunMode commandRunMode = FORCED;
        final UUID commandId = randomUUID();

        when(systemCommandLocator.forName(systemCommand.getName())).thenReturn(of(systemCommand));
        when(asynchronousCommandRunner.run(systemCommand, jmxCommandRuntimeParameters)).thenReturn(commandId);

        assertThat(jmxCommandRunner.run(systemCommand.getName(), jmxCommandRuntimeParameters, commandRunMode), is(commandId));

        verify(jmxCommandVerifier).verify(systemCommand, jmxCommandRuntimeParameters);
        verify(systemCommandStateBean, never()).commandInProgress(systemCommand);
    }
}