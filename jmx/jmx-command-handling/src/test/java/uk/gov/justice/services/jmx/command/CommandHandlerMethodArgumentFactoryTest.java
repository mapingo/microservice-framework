package uk.gov.justice.services.jmx.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.services.jmx.api.command.BaseSystemCommand;

import java.util.UUID;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
public class CommandHandlerMethodArgumentFactoryTest {


    @InjectMocks
    private CommandHandlerMethodArgumentFactory commandHandlerMethodArgumentFactory;

    @Test
    public void shouldCreateArrayOfTwoArgumentsIfCommandRuntimeIdIsEmpty() throws Exception {

        final SystemCommandWithoutCommandId systemCommandWithoutCommandId = new SystemCommandWithoutCommandId();
        final UUID commandId = randomUUID();

        final Object[] methodArguments = commandHandlerMethodArgumentFactory.createMethodArguments(
                systemCommandWithoutCommandId,
                commandId,
                empty());

        assertThat(methodArguments.length, is(2));
        assertThat(methodArguments[0], is(systemCommandWithoutCommandId));
        assertThat(methodArguments[1], is(commandId));
    }

    @Test
    public void shouldCreateArrayOfThreeArgumentsIfCommandRuntimeIdIsPresent() throws Exception {

        final SystemCommandWithCommandId systemCommandWithCommandId = new SystemCommandWithCommandId();
        final UUID commandId = randomUUID();
        final UUID commandRuntimeId = randomUUID();

        final Object[] methodArguments = commandHandlerMethodArgumentFactory.createMethodArguments(
                systemCommandWithCommandId,
                commandId,
                of(commandRuntimeId));

        assertThat(methodArguments.length, is(3));
        assertThat(methodArguments[0], is(systemCommandWithCommandId));
        assertThat(methodArguments[1], is(commandId));
        assertThat(methodArguments[2], is(commandRuntimeId));
    }

    private static class SystemCommandWithoutCommandId extends BaseSystemCommand {

        SystemCommandWithoutCommandId() {
            super("COMMAND_WITHOUT_COMMAND_ID", "Dummy command without commandRuntimeId");
        }
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