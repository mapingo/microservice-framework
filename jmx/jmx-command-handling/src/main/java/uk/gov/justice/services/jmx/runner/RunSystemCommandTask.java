package uk.gov.justice.services.jmx.runner;

import uk.gov.justice.services.jmx.api.command.SystemCommand;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

public class RunSystemCommandTask implements Callable<Boolean> {

    private final SystemCommandRunner systemCommandRunner;
    private final SystemCommand systemCommand;
    private final UUID commandId;
    private final Optional<UUID> commandRuntimeId;

    private final SystemCommandInvocationFailureHandler systemCommandInvocationFailureHandler;

    public RunSystemCommandTask(
            final SystemCommandRunner systemCommandRunner,
            final SystemCommand systemCommand,
            final UUID commandId,
            final Optional<UUID> commandRuntimeId,
            final SystemCommandInvocationFailureHandler systemCommandInvocationFailureHandler) {
        this.systemCommandRunner = systemCommandRunner;
        this.systemCommand = systemCommand;
        this.commandId = commandId;
        this.commandRuntimeId = commandRuntimeId;
        this.systemCommandInvocationFailureHandler = systemCommandInvocationFailureHandler;
    }

    @Override
    public Boolean call() {
        try {
            systemCommandRunner.run(systemCommand, commandId, commandRuntimeId);
        } catch (final Exception e) {
            systemCommandInvocationFailureHandler.handle(e, systemCommand, commandId, commandRuntimeId);
        }

        return true;
    }
}
