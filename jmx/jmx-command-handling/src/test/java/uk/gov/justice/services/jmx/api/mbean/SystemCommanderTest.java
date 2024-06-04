package uk.gov.justice.services.jmx.api.mbean;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.jmx.api.mbean.CommandRunMode.FORCED;
import static uk.gov.justice.services.jmx.api.mbean.CommandRunMode.GUARDED;

import uk.gov.justice.services.jmx.api.CommandNotFoundException;
import uk.gov.justice.services.jmx.api.UnrunnableSystemCommandException;
import uk.gov.justice.services.jmx.api.command.SystemCommand;
import uk.gov.justice.services.jmx.api.command.SystemCommandDetails;
import uk.gov.justice.services.jmx.api.domain.SystemCommandStatus;
import uk.gov.justice.services.jmx.command.CommandConverter;
import uk.gov.justice.services.jmx.command.SystemCommandLocator;
import uk.gov.justice.services.jmx.command.SystemCommandScanner;
import uk.gov.justice.services.jmx.command.TestCommand;
import uk.gov.justice.services.jmx.runner.AsynchronousCommandRunner;
import uk.gov.justice.services.jmx.state.observers.SystemCommandStateBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class SystemCommanderTest {

    @Mock
    private SystemCommandLocator systemCommandLocator;

    @Mock
    private AsynchronousCommandRunner asynchronousCommandRunner;

    @Mock
    private SystemCommandScanner systemCommandScanner;

    @Mock
    private SystemCommandStateBean systemCommandStateBean;

    @Mock
    private CommandConverter commandConverter;

    @Mock
    private SystemCommandVerifier systemCommandVerifier;

    @Mock
    private Logger logger;

    @InjectMocks
    private SystemCommander systemCommander;

    @Test
    public void shouldRunTheSystemCommandInForcedModeWhenCommandRunModeNotSupplied() throws Exception {

        final UUID commandId = randomUUID();
        final TestCommand testCommand = new TestCommand();
        final Optional<UUID> commandRuntimeId = empty();

        when(systemCommandLocator.forName(testCommand.getName())).thenReturn(of(testCommand));
        when(asynchronousCommandRunner.run(testCommand, commandRuntimeId)).thenReturn(commandId);

        assertThat(systemCommander.call("TEST_COMMAND"), is(commandId));

        final InOrder inOrder = inOrder(logger, systemCommandVerifier, asynchronousCommandRunner);

        inOrder.verify(logger).info("Received System Command 'TEST_COMMAND'");
        inOrder.verify(logger).info("Running 'TEST_COMMAND' in 'FORCED' mode");
        inOrder.verify(systemCommandVerifier).verify(testCommand, commandRuntimeId);
        inOrder.verify(asynchronousCommandRunner).run(testCommand, commandRuntimeId);
    }

    @Test
    public void shouldRunTheSystemCommandIfSupported() throws Exception {

        final UUID commandId = randomUUID();
        final TestCommand testCommand = new TestCommand();
        final Optional<UUID> commandRuntimeId = empty();

        when(systemCommandLocator.forName(testCommand.getName())).thenReturn(of(testCommand));
        when(asynchronousCommandRunner.run(testCommand, commandRuntimeId)).thenReturn(commandId);

        assertThat(systemCommander.call("TEST_COMMAND", GUARDED), is(commandId));

        final InOrder inOrder = inOrder(logger, systemCommandVerifier, asynchronousCommandRunner);

        inOrder.verify(logger).info("Received System Command 'TEST_COMMAND'");
        inOrder.verify(logger).info("Running 'TEST_COMMAND' in 'GUARDED' mode");
        inOrder.verify(systemCommandVerifier).verify(testCommand, commandRuntimeId);
        inOrder.verify(asynchronousCommandRunner).run(testCommand, commandRuntimeId);
    }

    @Test
    public void shouldRunTheSystemCommandWithIdIfSupported() throws Exception {

        final UUID commandId = randomUUID();
        final UUID commandRuntimeId = fromString("ce37d217-48a4-4a76-8a86-2e1d2d4c1ec2");
        final TestCommand testCommand = new TestCommand();
        final CommandRunMode commandRunMode = GUARDED;

        when(systemCommandLocator.forName(testCommand.getName())).thenReturn(of(testCommand));
        when(asynchronousCommandRunner.run(testCommand, of(commandRuntimeId))).thenReturn(commandId);

        assertThat(systemCommander.callWithRuntimeId("TEST_COMMAND", commandRuntimeId, commandRunMode), is(commandId));

        final InOrder inOrder = inOrder(logger, systemCommandVerifier, asynchronousCommandRunner);

        inOrder.verify(logger).info("Received System Command 'TEST_COMMAND' with UUID '" + commandRuntimeId + "'");
        inOrder.verify(logger).info("Running 'TEST_COMMAND' with UUID '%s' in 'GUARDED' mode".formatted(commandRuntimeId));
        inOrder.verify(systemCommandVerifier).verify(testCommand, of(commandRuntimeId));
        inOrder.verify(asynchronousCommandRunner).run(testCommand, of(commandRuntimeId));
    }

    @Test
    public void shouldFailIfSystemCommandNotSupported() throws Exception {

        final TestCommand testCommand = new TestCommand();

        when(systemCommandLocator.forName(testCommand.getName())).thenReturn(empty());

        try {
            systemCommander.call("TEST_COMMAND", GUARDED);
            fail();
        } catch (final UnrunnableSystemCommandException expected) {
            assertThat(expected.getMessage(), is("The system command 'TEST_COMMAND' is not supported on this context."));
        }
    }

    @Test
    public void shouldFailIfPreviousSystemCommandIsInProgress() throws Exception {

        final TestCommand testCommand = new TestCommand();

        when(systemCommandLocator.forName(testCommand.getName())).thenReturn(of(testCommand));
        when(systemCommandStateBean.commandInProgress(testCommand)).thenReturn(true);

        try {
            systemCommander.call("TEST_COMMAND", GUARDED);
            fail();
        } catch (final UnrunnableSystemCommandException expected) {
            assertThat(expected.getMessage(), is("Cannot run system command 'TEST_COMMAND'. A previous call to that command is still in progress."));
        }
    }

    @Test
    public void shouldFailIfPreviousSystemCommandWithIdIsInProgress() throws Exception {

        final TestCommand testCommand = new TestCommand();

        when(systemCommandLocator.forName(testCommand.getName())).thenReturn(of(testCommand));
        when(systemCommandStateBean.commandInProgress(testCommand)).thenReturn(true);

        try {
            systemCommander.call("TEST_COMMAND", GUARDED);
            fail();
        } catch (final UnrunnableSystemCommandException expected) {
            assertThat(expected.getMessage(), is("Cannot run system command 'TEST_COMMAND'. A previous call to that command is still in progress."));
        }
    }

    @Test
    public void shouldIgnoreAnyPreviousCommandInProgressIfRunModeIsForced() throws Exception {

        final UUID commandId = randomUUID();
        final TestCommand testCommand = new TestCommand();

        when(systemCommandLocator.forName(testCommand.getName())).thenReturn(of(testCommand));
        when(asynchronousCommandRunner.run(testCommand, empty())).thenReturn(commandId);

        assertThat(systemCommander.call("TEST_COMMAND", FORCED), is(commandId));

        final InOrder inOrder = inOrder(logger, asynchronousCommandRunner);

        inOrder.verify(logger).info("Received System Command 'TEST_COMMAND'");
        inOrder.verify(logger).info("Running 'TEST_COMMAND' in 'FORCED' mode");
        inOrder.verify(asynchronousCommandRunner).run(testCommand, empty());

        verify(systemCommandStateBean, never()).commandInProgress(testCommand);
    }

    @Test
    public void shouldListAllSystemCommands() throws Exception {

        final SystemCommand systemCommand_1 = mock(SystemCommand.class);
        final SystemCommand systemCommand_2 = mock(SystemCommand.class);
        final SystemCommand systemCommand_3 = mock(SystemCommand.class);

        final SystemCommandDetails systemCommandDetails_1 = mock(SystemCommandDetails.class);
        final SystemCommandDetails systemCommandDetails_2 = mock(SystemCommandDetails.class);
        final SystemCommandDetails systemCommandDetails_3 = mock(SystemCommandDetails.class);

        when(systemCommandScanner.findCommands()).thenReturn(asList(
                systemCommand_1,
                systemCommand_2,
                systemCommand_3));

        when(commandConverter.toCommandDetails(systemCommand_1)).thenReturn(systemCommandDetails_1);
        when(commandConverter.toCommandDetails(systemCommand_2)).thenReturn(systemCommandDetails_2);
        when(commandConverter.toCommandDetails(systemCommand_3)).thenReturn(systemCommandDetails_3);

        final List<SystemCommandDetails> systemCommandDetails = systemCommander.listCommands();

        assertThat(systemCommandDetails.size(), is(3));
        assertThat(systemCommandDetails, hasItem(systemCommandDetails_1));
        assertThat(systemCommandDetails, hasItem(systemCommandDetails_2));
        assertThat(systemCommandDetails, hasItem(systemCommandDetails_3));
    }

    @Test
    public void shouldGetSystemCommandStatus() throws Exception {

        final UUID commandId = randomUUID();

        final SystemCommandStatus systemCommandStatus = mock(SystemCommandStatus.class);
        when(systemCommandStateBean.getCommandStatus(commandId)).thenReturn(of(systemCommandStatus));

        assertThat(systemCommander.getCommandStatus(commandId), is(systemCommandStatus));
    }

    @Test
    public void shouldThrowExceptionIfSystemCommandNotFound() throws Exception {

        final UUID commandId = fromString("08fe90e9-c35b-4850-9af2-e5e743f6736e");

        when(systemCommandStateBean.getCommandStatus(commandId)).thenReturn(empty());

        try {
            systemCommander.getCommandStatus(commandId);
            fail();
        } catch (CommandNotFoundException expected) {
            assertThat(expected.getMessage(), is("No SystemCommand found with id 08fe90e9-c35b-4850-9af2-e5e743f6736e"));
        }
    }
}
