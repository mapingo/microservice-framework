package uk.gov.justice.framework.command.client.jmx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.framework.command.client.CommandLineException;
import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.services.jmx.api.SystemCommandInvocationFailedException;
import uk.gov.justice.services.jmx.api.UnrunnableSystemCommandException;
import uk.gov.justice.services.jmx.api.mbean.CommandRunMode;
import uk.gov.justice.services.jmx.api.mbean.SystemCommanderMBean;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClient;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClientFactory;
import uk.gov.justice.services.jmx.system.command.client.connection.Credentials;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;

import java.util.UUID;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static uk.gov.justice.services.jmx.api.mbean.CommandRunMode.GUARDED;

@ExtendWith(MockitoExtension.class)
public class SystemCommandInvokerTest {

    @Mock
    private SystemCommanderClientFactory systemCommanderClientFactory;

    @Mock
    private CommandPoller commandPoller;

    @Mock
    private ToConsolePrinter toConsolePrinter;

    @InjectMocks
    private SystemCommandInvoker systemCommandInvoker;

    @Test
    public void shouldInvokeMbeanWithCommandNameGivenCommandRuntimeIdNotSupplied() {

        final String contextName = "my-context";
        final String host = "localhost";
        final int port = 92834;
        final String commandName = "SOME_COMMAND";
        final UUID commandId = randomUUID();
        final CommandRunMode commandRunMode = GUARDED;

        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final SystemCommanderClient systemCommanderClient = mock(SystemCommanderClient.class);
        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);

        when(jmxParameters.getContextName()).thenReturn(contextName);
        when(jmxParameters.getHost()).thenReturn(host);
        when(jmxParameters.getPort()).thenReturn(port);
        when(jmxParameters.getCredentials()).thenReturn(empty());
        when(systemCommanderClientFactory.create(jmxParameters)).thenReturn(systemCommanderClient);
        when(systemCommanderClient.getRemote(contextName)).thenReturn(systemCommanderMBean);
        when(systemCommanderMBean.call(commandName, commandRunMode)).thenReturn(commandId);

        systemCommandInvoker.runSystemCommand(commandName, jmxParameters, null, commandRunMode);

        final InOrder inOrder = inOrder(
                toConsolePrinter,
                systemCommanderClientFactory,
                systemCommanderClient,
                systemCommanderMBean,
                commandPoller);

        inOrder.verify(toConsolePrinter).printf("Running system command '%s'", commandName);
        inOrder.verify(toConsolePrinter).printf("Connecting to %s context at '%s' on port %d", contextName, host, port);
        inOrder.verify(systemCommanderClientFactory).create(jmxParameters);
        inOrder.verify(toConsolePrinter).printf("Connected to %s context", contextName);
        inOrder.verify(systemCommanderClient).getRemote(contextName);
        inOrder.verify(systemCommanderMBean).call(commandName, commandRunMode);
        inOrder.verify(toConsolePrinter).printf("System command '%s' with id '%s' successfully sent to %s", commandName, commandId, contextName);
        inOrder.verify(commandPoller).runUntilComplete(systemCommanderMBean, commandId, commandName);
    }

    @Test
    public void shouldInvokeMbeanWithCommandNameAndRuntimeIdGivenCommandRuntimeIdIsSupplied() {
        final String contextName = "my-context";
        final String host = "localhost";
        final int port = 92834;
        final String commandName = "SOME_COMMAND";
        final UUID commandRuntimeId = UUID.randomUUID();
        final UUID commandId = randomUUID();
        final CommandRunMode commandRunMode = GUARDED;

        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final SystemCommanderClient systemCommanderClient = mock(SystemCommanderClient.class);
        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);

        when(jmxParameters.getContextName()).thenReturn(contextName);
        when(jmxParameters.getHost()).thenReturn(host);
        when(jmxParameters.getPort()).thenReturn(port);
        when(jmxParameters.getCredentials()).thenReturn(empty());
        when(systemCommanderClientFactory.create(jmxParameters)).thenReturn(systemCommanderClient);
        when(systemCommanderClient.getRemote(contextName)).thenReturn(systemCommanderMBean);
        when(systemCommanderMBean.callWithRuntimeId(commandName, commandRuntimeId, commandRunMode)).thenReturn(commandId);

        systemCommandInvoker.runSystemCommand(commandName, jmxParameters, commandRuntimeId.toString(), commandRunMode);

        final InOrder inOrder = inOrder(
                toConsolePrinter,
                systemCommanderClientFactory,
                systemCommanderClient,
                systemCommanderMBean,
                commandPoller);

        inOrder.verify(toConsolePrinter).printf("Running system command '%s'", commandName);
        inOrder.verify(toConsolePrinter).printf("Connecting to %s context at '%s' on port %d", contextName, host, port);
        inOrder.verify(systemCommanderClientFactory).create(jmxParameters);
        inOrder.verify(toConsolePrinter).printf("Connected to %s context", contextName);
        inOrder.verify(systemCommanderClient).getRemote(contextName);
        inOrder.verify(systemCommanderMBean).callWithRuntimeId(commandName, commandRuntimeId, commandRunMode);
        inOrder.verify(toConsolePrinter).printf("System command '%s' with id '%s' successfully sent to %s", commandName, commandId, contextName);
        inOrder.verify(commandPoller).runUntilComplete(systemCommanderMBean, commandId, commandName);
    }

    @Test
    public void shouldLogMessageAndThrowExceptionWhenSuppliedCommandRuntimeIdIsNotUUID() {
        final String contextName = "my-context";
        final String host = "localhost";
        final int port = 92834;
        final String commandName = "SOME_COMMAND";
        final String commandRuntimeId = "NOT_UUID_INVALID";
        final CommandRunMode commandRunMode = GUARDED;

        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final SystemCommanderClient systemCommanderClient = mock(SystemCommanderClient.class);
        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);

        when(jmxParameters.getContextName()).thenReturn(contextName);
        when(jmxParameters.getHost()).thenReturn(host);
        when(jmxParameters.getPort()).thenReturn(port);
        when(jmxParameters.getCredentials()).thenReturn(empty());
        when(systemCommanderClientFactory.create(jmxParameters)).thenReturn(systemCommanderClient);
        when(systemCommanderClient.getRemote(contextName)).thenReturn(systemCommanderMBean);

        final CommandLineException e = assertThrows(CommandLineException.class, () -> systemCommandInvoker.runSystemCommand(commandName, jmxParameters, commandRuntimeId, commandRunMode));
        assertThat(e.getMessage(), is("Unable to invoke command as supplied commandRuntimeId is not uuid format: " + commandRuntimeId));
        verifyNoInteractions(systemCommanderMBean);
    }

    @Test
    public void shouldLogIfUsingCredentials() throws Exception {

        final String contextName = "my-context";
        final String username = "Fred";
        final String host = "localhost";
        final int port = 92834;
        final String commandName = "SOME_COMMAND";
        final UUID commandId = randomUUID();
        final CommandRunMode commandRunMode = GUARDED;

        final Credentials credentials = mock(Credentials.class);
        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final SystemCommanderClient systemCommanderClient = mock(SystemCommanderClient.class);
        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);

        when(jmxParameters.getContextName()).thenReturn(contextName);
        when(jmxParameters.getHost()).thenReturn(host);
        when(jmxParameters.getPort()).thenReturn(port);
        when(jmxParameters.getCredentials()).thenReturn(of(credentials));
        when(credentials.getUsername()).thenReturn(username);
        when(systemCommanderClientFactory.create(jmxParameters)).thenReturn(systemCommanderClient);
        when(systemCommanderClient.getRemote(contextName)).thenReturn(systemCommanderMBean);
        when(systemCommanderMBean.call(commandName, GUARDED)).thenReturn(commandId);

        systemCommandInvoker.runSystemCommand(commandName, jmxParameters, null, commandRunMode);

        final InOrder inOrder = inOrder(
                toConsolePrinter,
                systemCommanderClientFactory,
                systemCommanderClient,
                systemCommanderMBean,
                commandPoller);

        inOrder.verify(toConsolePrinter).printf("Running system command '%s'", commandName);
        inOrder.verify(toConsolePrinter).printf("Connecting to %s context at '%s' on port %d", contextName, host, port);
        inOrder.verify(toConsolePrinter).printf("Connecting with credentials for user '%s'", username);
        inOrder.verify(systemCommanderClientFactory).create(jmxParameters);
        inOrder.verify(toConsolePrinter).printf("Connected to %s context", contextName);
        inOrder.verify(systemCommanderClient).getRemote(contextName);
        inOrder.verify(systemCommanderMBean).call(commandName, commandRunMode);
        inOrder.verify(toConsolePrinter).printf("System command '%s' with id '%s' successfully sent to %s", commandName, commandId, contextName);
        inOrder.verify(commandPoller).runUntilComplete(systemCommanderMBean, commandId, commandName);
    }

    @Test
    public void shouldLogIfTheCommandIsUnsupported() throws Exception {

        final String contextName = "secret";
        final String host = "localhost";
        final int port = 92834;
        final String username = "Fred";
        final String commandName = "PING";
        final CommandRunMode commandRunMode = GUARDED;

        final UnrunnableSystemCommandException unrunnableSystemCommandException = new UnrunnableSystemCommandException("Ooops");

        final Credentials credentials = mock(Credentials.class);
        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final SystemCommanderClient systemCommanderClient = mock(SystemCommanderClient.class);
        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);

        when(jmxParameters.getContextName()).thenReturn(contextName);
        when(jmxParameters.getHost()).thenReturn(host);
        when(jmxParameters.getPort()).thenReturn(port);
        when(jmxParameters.getCredentials()).thenReturn(of(credentials));
        when(credentials.getUsername()).thenReturn(username);
        when(systemCommanderClientFactory.create(jmxParameters)).thenReturn(systemCommanderClient);
        when(systemCommanderClient.getRemote(contextName)).thenReturn(systemCommanderMBean);
        doThrow(unrunnableSystemCommandException).when(systemCommanderMBean).call(commandName, commandRunMode);

        assertThrows(UnrunnableSystemCommandException.class, () -> systemCommandInvoker.runSystemCommand(commandName, jmxParameters, "", commandRunMode));

        final InOrder inOrder = inOrder(
                toConsolePrinter,
                systemCommanderClientFactory,
                systemCommanderClient);

        inOrder.verify(toConsolePrinter).printf("Running system command '%s'", commandName);
        inOrder.verify(toConsolePrinter).printf("Connecting to %s context at '%s' on port %d", contextName, host, port);
        inOrder.verify(systemCommanderClientFactory).create(jmxParameters);
        inOrder.verify(toConsolePrinter).printf("Connected to %s context", contextName);
        inOrder.verify(toConsolePrinter).printf("The command '%s' is not supported on this %s context", commandName, contextName);
    }

    @Test
    public void shouldLogAndPrintTheServerStackTraceIfTheCommandFails() throws Exception {

        final String contextName = "secret";
        final String host = "localhost";
        final int port = 92834;
        final String username = "Fred";
        final String serverStackTrace = "the stack trace from the server";
        final String errorMessage = "Ooops";
        final String commandName = "PING";
        final CommandRunMode commandRunMode = GUARDED;

        final SystemCommandInvocationFailedException systemCommandInvocationFailedException = new SystemCommandInvocationFailedException(errorMessage, serverStackTrace);

        final Credentials credentials = mock(Credentials.class);
        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final SystemCommanderClient systemCommanderClient = mock(SystemCommanderClient.class);
        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);

        when(jmxParameters.getContextName()).thenReturn(contextName);
        when(jmxParameters.getHost()).thenReturn(host);
        when(jmxParameters.getPort()).thenReturn(port);
        when(jmxParameters.getCredentials()).thenReturn(of(credentials));
        when(credentials.getUsername()).thenReturn(username);
        when(systemCommanderClientFactory.create(jmxParameters)).thenReturn(systemCommanderClient);
        when(systemCommanderClient.getRemote(contextName)).thenReturn(systemCommanderMBean);
        doThrow(systemCommandInvocationFailedException).when(systemCommanderMBean).call(commandName, commandRunMode);

        assertThrows(SystemCommandInvocationFailedException.class, () -> systemCommandInvoker.runSystemCommand(commandName, jmxParameters, null, commandRunMode));

        final InOrder inOrder = inOrder(
                toConsolePrinter,
                systemCommanderClientFactory,
                systemCommanderClient);

        inOrder.verify(toConsolePrinter).printf("Connecting to %s context at '%s' on port %d", contextName, host, port);
        inOrder.verify(toConsolePrinter).printf("Connecting with credentials for user '%s'", username);
        inOrder.verify(toConsolePrinter).printf("Connected to %s context", contextName);
        inOrder.verify(toConsolePrinter).printf("The command '%s' failed: %s", commandName, errorMessage);
        inOrder.verify(toConsolePrinter).println(serverStackTrace);
    }
}
