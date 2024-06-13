package uk.gov.justice.services.jmx.bootstrap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import uk.gov.justice.services.jmx.command.CommandHandlerMethodArgumentFactory;
import uk.gov.justice.services.jmx.command.HandlerMethodValidator;
import uk.gov.justice.services.jmx.command.SystemCommandHandlerProxy;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class HandlesSystemCommandHandlerProxyFactoryTest {

    @InjectMocks
    private SystemCommandHandlerProxyFactory systemCommandHandlerProxyFactory;

    @Test
    public void shouldCreateSystemCommandHandlerProxy() throws Exception {

        final String commandName = "command name";
        final Method method = AnotherHandlesSystemCommandHandler.class.getDeclaredMethod("aCommandMethod");
        final Object instance = new AnotherHandlesSystemCommandHandler();

        final HandlerMethodValidator handlerMethodValidator = mock(HandlerMethodValidator.class);
        final CommandHandlerMethodArgumentFactory commandHandlerMethodArgumentFactory = mock(CommandHandlerMethodArgumentFactory.class);

        final SystemCommandHandlerProxy systemCommandHandlerProxy = systemCommandHandlerProxyFactory.create(
                commandName,
                method,
                instance,
                handlerMethodValidator,
                commandHandlerMethodArgumentFactory);

        assertThat(getValueOfField(systemCommandHandlerProxy, "commandName", String.class), is(commandName));
        assertThat(getValueOfField(systemCommandHandlerProxy, "method", Method.class), is(method));
        assertThat(getValueOfField(systemCommandHandlerProxy, "instance", Object.class), is(instance));
        assertThat(getValueOfField(systemCommandHandlerProxy, "handlerMethodValidator", HandlerMethodValidator.class), is(handlerMethodValidator));
        assertThat(getValueOfField(systemCommandHandlerProxy, "commandHandlerMethodArgumentFactory", CommandHandlerMethodArgumentFactory.class), is(commandHandlerMethodArgumentFactory));
    }

    private class AnotherHandlesSystemCommandHandler {

        public void aCommandMethod() {

        }
    }
}
