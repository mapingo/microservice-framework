package uk.gov.justice.framework.command.client;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static uk.gov.justice.framework.command.client.ReturnCode.AUTHENTICATION_FAILED;
import static uk.gov.justice.framework.command.client.ReturnCode.COMMAND_FAILED;
import static uk.gov.justice.framework.command.client.ReturnCode.CONNECTION_FAILED;
import static uk.gov.justice.framework.command.client.ReturnCode.EXCEPTION_OCCURRED;

import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.services.jmx.api.SystemCommandInvocationFailedException;
import uk.gov.justice.services.jmx.system.command.client.MBeanClientConnectionException;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxAuthenticationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReturnCodeFactoryTest {

    @Mock
    private ToConsolePrinter toConsolePrinter;

    @InjectMocks
    private ReturnCodeFactory returnCodeFactory;

    @Test
    public void shouldReturnCorrectCodeForJmxAuthenticationException() {

        final JmxAuthenticationException jmxAuthenticationException = new JmxAuthenticationException("Test", new Exception());

        assertThat(returnCodeFactory.createFor(jmxAuthenticationException), is(AUTHENTICATION_FAILED));
        verify(toConsolePrinter).println("Authentication failed. Please ensure your username and password are correct");
    }

    @Test
    public void shouldReturnCorrectCodeForMBeanClientConnectionException() {

        assertThat(returnCodeFactory.createFor(new MBeanClientConnectionException("Test")), is(CONNECTION_FAILED));

        verify(toConsolePrinter).println("Test");
    }

    @Test
    public void shouldReturnCorrectCodeForSystemCommandFailedException() {

        assertThat(returnCodeFactory.createFor(new SystemCommandFailedException("Test")), is(COMMAND_FAILED));

        verifyNoInteractions(toConsolePrinter);
    }

    @Test
    public void shouldReturnCorrectCodeForSystemCommandInvocationFailedException() {

        assertThat(returnCodeFactory.createFor(new SystemCommandInvocationFailedException("Test", "Stack Trace")), is(EXCEPTION_OCCURRED));


        final InOrder inOrder = inOrder(toConsolePrinter);

        inOrder.verify(toConsolePrinter).printf("-------------------------------------------------");
        inOrder.verify(toConsolePrinter).printf("SystemCommandInvocationFailedException occurred on server: Test");
        inOrder.verify(toConsolePrinter).printf("-------------------------------------------------");
        inOrder.verify(toConsolePrinter).println("Stack Trace");
        inOrder.verify(toConsolePrinter).printf("-------------------------------------------------");
        inOrder.verify(toConsolePrinter).printf("End SystemCommandInvocationFailedException from server");
        inOrder.verify(toConsolePrinter).printf("-------------------------------------------------");
    }

    @Test
    public void shouldReturnCorrectCodeForAnyOtherException() {

        final Exception exception = new Exception("Test");

        assertThat(returnCodeFactory.createFor(exception), is(EXCEPTION_OCCURRED));

        verify(toConsolePrinter).println(exception);
    }
}
