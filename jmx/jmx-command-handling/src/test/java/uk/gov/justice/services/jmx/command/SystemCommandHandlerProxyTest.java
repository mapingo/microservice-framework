package uk.gov.justice.services.jmx.command;

import org.junit.jupiter.api.Test;
import uk.gov.justice.services.jmx.api.InvalidHandlerMethodException;
import uk.gov.justice.services.jmx.api.SystemCommandInvocationException;
import uk.gov.justice.services.jmx.api.command.SystemCommand;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static uk.gov.justice.services.jmx.command.TestCommand.TEST_COMMAND;

public class SystemCommandHandlerProxyTest {

    @Test
    public void shouldInvokeTheCommandHandlerMethod() throws Exception {

        final UUID commandId = randomUUID();
        final TestCommand testCommand = new TestCommand();
        final Optional<UUID> commandRuntimeId = empty();
        final Object[] methodArguments = {testCommand, commandId};

        final DummyHandler dummyHandler = new DummyHandler();
        final Method method = getMethod("someHandlerMethod", dummyHandler);
        final CommandHandlerMethodArgumentFactory commandHandlerMethodArgumentFactory = mock(CommandHandlerMethodArgumentFactory.class);

        final HandlerMethodValidator handlerMethodValidator = mock(HandlerMethodValidator.class);

        final SystemCommandHandlerProxy systemCommandHandlerProxy = new SystemCommandHandlerProxy(
                testCommand.getName(),
                method,
                dummyHandler,
                handlerMethodValidator,
                commandHandlerMethodArgumentFactory);

        when(commandHandlerMethodArgumentFactory.createMethodArguments(testCommand, commandId, commandRuntimeId)).thenReturn(methodArguments);

        assertThat(systemCommandHandlerProxy.getCommandName(), is(testCommand.getName()));
        assertThat(systemCommandHandlerProxy.getInstance(), is(dummyHandler));

        assertThat(dummyHandler.someHandlerMethodWasCalled(), is(false));

        systemCommandHandlerProxy.invokeCommand(testCommand, commandId, commandRuntimeId);

        assertThat(dummyHandler.someHandlerMethodWasCalled(), is(true));

        verify(handlerMethodValidator).checkHandlerMethodIsValid(method, dummyHandler, commandRuntimeId);
    }

    @Test
    public void shouldFailIfTheMethodIsInaccessible() throws Exception {

        final UUID commandId = randomUUID();
        final Optional<UUID> commandRuntimeId = empty();
        final TestCommand testCommand = new TestCommand();
        final DummyHandler dummyHandler = new DummyHandler();
        final Method method = getMethod("aPrivateMethod", dummyHandler);
        final Object[] methodArguments = {testCommand, commandId};

        final HandlerMethodValidator handlerMethodValidator = mock(HandlerMethodValidator.class);
        final CommandHandlerMethodArgumentFactory commandHandlerMethodArgumentFactory = mock(CommandHandlerMethodArgumentFactory.class);

        final SystemCommandHandlerProxy systemCommandHandlerProxy = new SystemCommandHandlerProxy(
                testCommand.getName(),
                method,
                dummyHandler,
                handlerMethodValidator,
                commandHandlerMethodArgumentFactory);

        when(commandHandlerMethodArgumentFactory.createMethodArguments(testCommand, commandId, commandRuntimeId)).thenReturn(methodArguments);

        try {
            systemCommandHandlerProxy.invokeCommand(testCommand, commandId, commandRuntimeId);
            fail();
        } catch (final SystemCommandInvocationException expected) {
            assertThat(expected.getMessage(), is("Failed to call method 'aPrivateMethod()' on " + dummyHandler.getClass().getName() + ". Is the method public?"));
            assertThat(expected.getCause(), is(instanceOf(IllegalAccessException.class)));
        }

        verify(handlerMethodValidator).checkHandlerMethodIsValid(method, dummyHandler, commandRuntimeId);
    }

    @Test
    public void shouldFailIfTheInvokedMethodThrowsAnException() throws Exception {

        final UUID commandId = randomUUID();
        final Optional<UUID> commandRuntimeId = of(randomUUID());
        final TestCommand testCommand = new TestCommand();
        final DummyHandler dummyHandler = new DummyHandler();
        final Method method = getMethod("anExceptionThrowingMethod", dummyHandler);
        final Object[] methodArguments = {testCommand, commandId};

        final HandlerMethodValidator handlerMethodValidator = mock(HandlerMethodValidator.class);
        final CommandHandlerMethodArgumentFactory commandHandlerMethodArgumentFactory = mock(CommandHandlerMethodArgumentFactory.class);

        final SystemCommandHandlerProxy systemCommandHandlerProxy = new SystemCommandHandlerProxy(
                testCommand.getName(),
                method,
                dummyHandler,
                handlerMethodValidator,
                commandHandlerMethodArgumentFactory);

        when(commandHandlerMethodArgumentFactory.createMethodArguments(testCommand, commandId, commandRuntimeId)).thenReturn(methodArguments);

        try {
            systemCommandHandlerProxy.invokeCommand(testCommand, commandId, commandRuntimeId);
            fail();
        } catch (final SystemCommandInvocationException expected) {
            assertThat(expected.getMessage(), is("IOException thrown when calling method 'anExceptionThrowingMethod()' on " + dummyHandler.getClass().getName()));
            assertThat(expected.getCause(), is(instanceOf(IOException.class)));
        }

        verify(handlerMethodValidator).checkHandlerMethodIsValid(method, dummyHandler, commandRuntimeId);
    }

    @Test
    public void shouldConvertAndThrowExceptionOnInvalidMethodException() throws Exception {

        final UUID commandId = randomUUID();
        final Optional<UUID> commandRuntimeId = of(randomUUID());
        final TestCommand testCommand = new TestCommand();
        final DummyHandler dummyHandler = new DummyHandler();
        final Method method = getMethod("anExceptionThrowingMethod", dummyHandler);
        final InvalidHandlerMethodException invalidHandlerMethodException = new InvalidHandlerMethodException("Found invalid number of method arguments");

        final HandlerMethodValidator handlerMethodValidator = mock(HandlerMethodValidator.class);
        final CommandHandlerMethodArgumentFactory commandHandlerMethodArgumentFactory = mock(CommandHandlerMethodArgumentFactory.class);
        doThrow(invalidHandlerMethodException).when(handlerMethodValidator).checkHandlerMethodIsValid(method, dummyHandler, commandRuntimeId);
        final SystemCommandHandlerProxy systemCommandHandlerProxy = new SystemCommandHandlerProxy(
                testCommand.getName(),
                method,
                dummyHandler,
                handlerMethodValidator,
                commandHandlerMethodArgumentFactory);

        final SystemCommandInvocationException exception = assertThrows(SystemCommandInvocationException.class,
                () -> systemCommandHandlerProxy.invokeCommand(testCommand, commandId, commandRuntimeId));

        assertThat(exception.getMessage(), is("Found invalid number of method arguments"));
        assertThat(exception.getCause(), is(invalidHandlerMethodException));
    }

    private Method getMethod(final String methodName, final Object instance) {

        for (final Method method : instance.getClass().getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }

        return null;
    }

    private class DummyHandler {

        private boolean someHandlerMethodCalled = false;

        @HandlesSystemCommand(TEST_COMMAND)
        public void someHandlerMethod(final TestCommand testCommand, final UUID commandId) {
            someHandlerMethodCalled = true;
        }

        @HandlesSystemCommand("PRIVATE_TEST_COMMAND")
        private void aPrivateMethod(final SystemCommand systemCommand, final UUID commandId) {
        }

        @HandlesSystemCommand("DODGY_TEST_COMMAND")
        public void anExceptionThrowingMethod(final SystemCommand systemCommand, final UUID commandId) throws IOException {
            throw new IOException("Ooops");
        }

        public boolean someHandlerMethodWasCalled() {
            return someHandlerMethodCalled;
        }
    }
}
