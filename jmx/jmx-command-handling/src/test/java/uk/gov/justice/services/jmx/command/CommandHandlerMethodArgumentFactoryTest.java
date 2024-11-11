package uk.gov.justice.services.jmx.command;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.services.jmx.api.command.BaseSystemCommand;
import uk.gov.justice.services.jmx.api.parameters.JmxCommandRuntimeParameters;
import uk.gov.justice.services.jmx.api.parameters.JmxCommandRuntimeParameters.JmxCommandRuntimeParametersBuilder;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommandHandlerMethodArgumentFactoryTest {


    @InjectMocks
    private CommandHandlerMethodArgumentFactory commandHandlerMethodArgumentFactory;

    @Test
    public void shouldCreateArrayOfThreeArgumentsOfSystemCommandCommandIdAndJmxRuntimeParameters() throws Exception {

        final SystemCommandWithCommandId systemCommandWithCommandId = new SystemCommandWithCommandId();
        final UUID commandId = randomUUID();
        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = new JmxCommandRuntimeParametersBuilder()
                .withCommandRuntimeId(randomUUID())
                .build();

        final Object[] methodArguments = commandHandlerMethodArgumentFactory.createMethodArguments(
                systemCommandWithCommandId,
                commandId,
                jmxCommandRuntimeParameters);

        assertThat(methodArguments.length, is(3));
        assertThat(methodArguments[0], is(systemCommandWithCommandId));
        assertThat(methodArguments[1], is(commandId));
        assertThat(methodArguments[2], is(jmxCommandRuntimeParameters));
    }

    private static class SystemCommandWithCommandId extends BaseSystemCommand {

        SystemCommandWithCommandId() {
            super("COMMAND_WITH_COMMAND_ID", "Dummy command with commandRuntimeId");
        }

        @Override
        public boolean requiresCommandRuntimeId() {
            return true;
        }

        @Override
        public String commandRuntimeIdType() {
            return "some-uuid";
        }
    }
}