package uk.gov.justice.services.jmx.command;

import java.util.UUID;

public class TestCommandHandler {

    @HandlesSystemCommand("some-command_1")
    public void validHandlerMethodWithOutRuntimeId(final TestCommand testCommand, final UUID commandId) {
        
    }

    @HandlesSystemCommand("some-command_2")
    private void invalidPrivateHandlerMethodWithOutRuntimeId(final TestCommand testCommand, final UUID commandId) {

    }

    @HandlesSystemCommand("some-command_4")
    public void validHandlerMethodWithRuntimeId(final TestCommand testCommand, final UUID commandId, final UUID commandRuntimeId) {

    }

    @HandlesSystemCommand("some-command_5")
    private void invalidPrivateHandlerMethodWithRuntimeId(final TestCommand testCommand, final UUID commandId, final UUID commandRuntimeId) {

    }

    @HandlesSystemCommand("some-command_3")
    public void invalidMissingParameterHandlerMethod() {

    }

    @HandlesSystemCommand("some-command_4")
    public void invalidTooManyParametersHandlerMethod(final TestCommand testCommand, final UUID commandId, final UUID commandRuntimeId, final String someString) {

    }

    @HandlesSystemCommand("some-command_5")
    public void invalidNoSystemCommandHandlerMethod(final String someString) {

    }

    @HandlesSystemCommand("some-command_6")
    public void invalidNoCommandIdMethod(final TestCommand testCommand) {

    }

    @HandlesSystemCommand("some-command_6")
    public void invalidNoCommandRuntimeIdMethod(final TestCommand testCommand, final UUID commandId) {

    }
}
