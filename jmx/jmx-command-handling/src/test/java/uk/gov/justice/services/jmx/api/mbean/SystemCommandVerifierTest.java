package uk.gov.justice.services.jmx.api.mbean;

import org.junit.jupiter.api.Test;
import uk.gov.justice.services.jmx.api.UnrunnableSystemCommandException;
import uk.gov.justice.services.jmx.api.command.SystemCommand;

import java.util.UUID;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SystemCommandVerifierTest {

    private final SystemCommandVerifier systemCommandVerifier = new SystemCommandVerifier();

    @Test
    public void shouldVerifySuccessfullyCommandThatRequiresRuntimeId() throws Exception {

        final UUID commandRuntimeId = randomUUID();
        final SystemCommandWithRuntimeId systemCommandWithRuntimeId = new SystemCommandWithRuntimeId();

        systemCommandVerifier.verify(systemCommandWithRuntimeId, of(commandRuntimeId));
    }

    @Test
    public void shouldFailIfCommandThatRequiresRuntimeIdHasMissingRuntimeId() throws Exception {

        final SystemCommandWithRuntimeId systemCommandWithRuntimeId = new SystemCommandWithRuntimeId();

        final UnrunnableSystemCommandException unrunnableSystemCommandException = assertThrows(
                UnrunnableSystemCommandException.class,
                () -> systemCommandVerifier.verify(systemCommandWithRuntimeId, empty()));

        assertThat(unrunnableSystemCommandException.getMessage(), is("The JMX command 'SYSTEM_COMMAND_WITH_RUNTIME_ID' requires 'eventId' to be able to run. Please re-run using the '--command-runtime-id' switch"));
    }

    @Test
    public void shouldVerifySuccessfullyCommandThatDoesNotRequireRuntimeId() throws Exception {

        final UUID commandRuntimeId = randomUUID();
        final SystemCommandWithoutRuntimeId systemCommandWithoutRuntimeId = new SystemCommandWithoutRuntimeId();

        systemCommandVerifier.verify(systemCommandWithoutRuntimeId, of(commandRuntimeId));
    }

    private static class SystemCommandWithRuntimeId implements SystemCommand {

        @Override
        public String getName() {
            return "SYSTEM_COMMAND_WITH_RUNTIME_ID";
        }

        @Override
        public String getDescription() {
            return "Dumb class for testing";
        }

        @Override
        public boolean requiresCommandRuntimeId() {
            return true;
        }

        @Override
        public String commandRuntimeIdType() {
            return "eventId";
        }
    }

    private static class SystemCommandWithoutRuntimeId implements SystemCommand {

        @Override
        public String getName() {
            return "SYSTEM_COMMAND_WITHOUT_RUNTIME_ID";
        }

        @Override
        public String getDescription() {
            return "Dumb class for testing";
        }
    }
}