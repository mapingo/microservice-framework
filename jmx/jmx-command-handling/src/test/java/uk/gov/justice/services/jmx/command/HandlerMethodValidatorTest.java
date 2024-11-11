package uk.gov.justice.services.jmx.command;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import uk.gov.justice.services.jmx.api.InvalidHandlerMethodException;
import uk.gov.justice.services.jmx.api.parameters.JmxCommandRuntimeParameters;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HandlerMethodValidatorTest {

    @InjectMocks
    private HandlerMethodValidator handlerMethodValidator;

    @Test
    public void shouldHavePublicHandlerMethod() throws Exception {

        final TestCommandHandler testCommandHandler = new TestCommandHandler();
        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = mock(JmxCommandRuntimeParameters.class);

        final Method validHandlerMethod = getMethod("validHandlerMethod", testCommandHandler.getClass());
        handlerMethodValidator.checkHandlerMethodIsValid(validHandlerMethod, testCommandHandler, jmxCommandRuntimeParameters);
    }

    @Test
    public void shouldFailIfHandlerMethodIsPrivate() throws Exception {

        final TestCommandHandler testCommandHandler = new TestCommandHandler();
        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = mock(JmxCommandRuntimeParameters.class);

        final Method validHandlerMethod = getMethod("privateHandlerMethod", testCommandHandler.getClass());
        final InvalidHandlerMethodException invalidHandlerMethodException = assertThrows(InvalidHandlerMethodException.class, () -> handlerMethodValidator.checkHandlerMethodIsValid(
                validHandlerMethod,
                testCommandHandler,
                jmxCommandRuntimeParameters));

        assertThat(invalidHandlerMethodException.getMessage(), is("Handler method 'privateHandlerMethod(...)' on class 'uk.gov.justice.services.jmx.command.TestCommandHandler' is not public."));
    }

    @Test
    public void shouldFailIfHandlerMethodIsProtected() throws Exception {

        final TestCommandHandler testCommandHandler = new TestCommandHandler();
        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = mock(JmxCommandRuntimeParameters.class);

        final Method validHandlerMethod = getMethod("protectedHandlerMethod", testCommandHandler.getClass());
        final InvalidHandlerMethodException invalidHandlerMethodException = assertThrows(InvalidHandlerMethodException.class, () -> handlerMethodValidator.checkHandlerMethodIsValid(
                validHandlerMethod,
                testCommandHandler,
                jmxCommandRuntimeParameters));

        assertThat(invalidHandlerMethodException.getMessage(), is("Handler method 'protectedHandlerMethod(...)' on class 'uk.gov.justice.services.jmx.command.TestCommandHandler' is not public."));
    }

    @Test
    public void shouldFailIfHandlerMethodIsPackageProtected() throws Exception {

        final TestCommandHandler testCommandHandler = new TestCommandHandler();
        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = mock(JmxCommandRuntimeParameters.class);

        final Method validHandlerMethod = getMethod("packageProtectedHandlerMethod", testCommandHandler.getClass());
        final InvalidHandlerMethodException invalidHandlerMethodException = assertThrows(InvalidHandlerMethodException.class, () -> handlerMethodValidator.checkHandlerMethodIsValid(
                validHandlerMethod,
                testCommandHandler,
                jmxCommandRuntimeParameters));

        assertThat(invalidHandlerMethodException.getMessage(), is("Handler method 'packageProtectedHandlerMethod(...)' on class 'uk.gov.justice.services.jmx.command.TestCommandHandler' is not public."));
    }

    @Test
    public void shouldAlwaysHaveSystemCommandAsFirstParameter() throws Exception {

        final TestCommandHandler testCommandHandler = new TestCommandHandler();
        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = mock(JmxCommandRuntimeParameters.class);

        final Method validHandlerMethod = getMethod("validHandlerMethod", testCommandHandler.getClass());

        handlerMethodValidator.checkHandlerMethodIsValid(validHandlerMethod, testCommandHandler, jmxCommandRuntimeParameters);
    }

    @Test
    public void shouldFailIFirstParameterIsNotSystemCommand() throws Exception {

        final TestCommandHandler testCommandHandler = new TestCommandHandler();
        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = mock(JmxCommandRuntimeParameters.class);

        final Method validHandlerMethod = getMethod("missingSystemCommand", testCommandHandler.getClass());
        final InvalidHandlerMethodException invalidHandlerMethodException = assertThrows(InvalidHandlerMethodException.class, () -> handlerMethodValidator.checkHandlerMethodIsValid(
                validHandlerMethod,
                testCommandHandler,
                jmxCommandRuntimeParameters));

        assertThat(invalidHandlerMethodException.getMessage(), is("Invalid handler method 'missingSystemCommand(...)' on class 'uk.gov.justice.services.jmx.command.TestCommandHandler'. Method should have first parameter of type 'uk.gov.justice.services.jmx.api.command.SystemCommand'."));
    }

    @Test
    public void shouldAlwaysHaveCommandIdAsSecondParameter() throws Exception {

        final TestCommandHandler testCommandHandler = new TestCommandHandler();
        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = mock(JmxCommandRuntimeParameters.class);

        final Method validHandlerMethod = getMethod("validHandlerMethod", testCommandHandler.getClass());

        handlerMethodValidator.checkHandlerMethodIsValid(validHandlerMethod, testCommandHandler, jmxCommandRuntimeParameters);
    }

    @Test
    public void shouldFailIfSecondParameterIsNotSystemCommandId() throws Exception {

        final TestCommandHandler testCommandHandler = new TestCommandHandler();
        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = mock(JmxCommandRuntimeParameters.class);

        final Method validHandlerMethod = getMethod("missingCommandId", testCommandHandler.getClass());
        final InvalidHandlerMethodException invalidHandlerMethodException = assertThrows(InvalidHandlerMethodException.class, () -> handlerMethodValidator.checkHandlerMethodIsValid(
                validHandlerMethod,
                testCommandHandler,
                jmxCommandRuntimeParameters));

        assertThat(invalidHandlerMethodException.getMessage(), is("Invalid handler method 'missingCommandId(...)' on class 'uk.gov.justice.services.jmx.command.TestCommandHandler'. Second parameter should be the commandId of type 'java.util.UUID'."));
    }

    @Test
    public void shouldAlwaysHaveJmxCommandRuntimeParametersAsThirdParameter() throws Exception {

        final TestCommandHandler testCommandHandler = new TestCommandHandler();
        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = mock(JmxCommandRuntimeParameters.class);

        final Method validHandlerMethod = getMethod("validHandlerMethod", testCommandHandler.getClass());

        handlerMethodValidator.checkHandlerMethodIsValid(validHandlerMethod, testCommandHandler, jmxCommandRuntimeParameters);
    }

    @Test
    public void shouldFailIfThirdParameterIsNotJmxCommandRuntimeParameters() throws Exception {

        final TestCommandHandler testCommandHandler = new TestCommandHandler();
        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = mock(JmxCommandRuntimeParameters.class);

        final Method validHandlerMethod = getMethod("missingJmxCommandRuntimeParameters", testCommandHandler.getClass());
        final InvalidHandlerMethodException invalidHandlerMethodException = assertThrows(InvalidHandlerMethodException.class, () -> handlerMethodValidator.checkHandlerMethodIsValid(
                validHandlerMethod,
                testCommandHandler,
                jmxCommandRuntimeParameters));

        assertThat(invalidHandlerMethodException.getMessage(), is("Invalid handler method 'missingJmxCommandRuntimeParameters(...)' on class 'uk.gov.justice.services.jmx.command.TestCommandHandler'. Third parameter should be of type 'uk.gov.justice.services.jmx.api.parameters.JmxCommandRuntimeParameters'."));
    }

    @Test
    public void shouldFailIfTooFewParameters() throws Exception {

        final TestCommandHandler testCommandHandler = new TestCommandHandler();
        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = mock(JmxCommandRuntimeParameters.class);

        final Method validHandlerMethod = getMethod("tooFewParameters", testCommandHandler.getClass());
        final InvalidHandlerMethodException invalidHandlerMethodException = assertThrows(InvalidHandlerMethodException.class, () -> handlerMethodValidator.checkHandlerMethodIsValid(
                validHandlerMethod,
                testCommandHandler,
                jmxCommandRuntimeParameters));

        assertThat(invalidHandlerMethodException.getMessage(), is("Invalid handler method 'tooFewParameters(...)' on class 'uk.gov.justice.services.jmx.command.TestCommandHandler'. Method should have 3 parameters. First of type 'uk.gov.justice.services.jmx.api.command.SystemCommand' and second of type 'java.util.UUID' and third of type 'uk.gov.justice.services.jmx.api.parameters.JmxCommandRuntimeParameters'."));
    }

    private Method getMethod(final String methodName, final Class<?> handlerClass) {

        for (final Method method : handlerClass.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }

        return null;
    }
}
