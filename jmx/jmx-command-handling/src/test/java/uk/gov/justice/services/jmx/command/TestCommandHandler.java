package uk.gov.justice.services.jmx.command;

import uk.gov.justice.services.jmx.api.parameters.JmxCommandRuntimeParameters;

import java.util.UUID;

public class TestCommandHandler {

    @HandlesSystemCommand("some-command_1")
    public void validHandlerMethod(
            final TestCommand testCommand,
            final UUID commandId,
            final JmxCommandRuntimeParameters jmxCommandRuntimeParameters) {
        
    }

    @HandlesSystemCommand("some-command_2")
    private void privateHandlerMethod(
            final TestCommand testCommand,
            final UUID commandId,
            final JmxCommandRuntimeParameters jmxCommandRuntimeParameters) {

    }

    @HandlesSystemCommand("some-command_3")
    protected void protectedHandlerMethod(
            final TestCommand testCommand,
            final UUID commandId,
            final JmxCommandRuntimeParameters jmxCommandRuntimeParameters) {

    }

    @HandlesSystemCommand("some-command_4")
    void packageProtectedHandlerMethod(
            final TestCommand testCommand,
            final UUID commandId,
            final JmxCommandRuntimeParameters jmxCommandRuntimeParameters) {

    }

    @HandlesSystemCommand("some-command_5")
    public void missingSystemCommand(
            final String thisShouldBeASystemCommand,
            final UUID commandId,
            final JmxCommandRuntimeParameters jmxCommandRuntimeParameters) {

    }

    @HandlesSystemCommand("some-command_5")
    public void missingCommandId(
            final TestCommand testCommand,
            final String thisShouldBeACommandId,
            final JmxCommandRuntimeParameters jmxCommandRuntimeParameters) {

    }

    @HandlesSystemCommand("some-command_6")
    public void missingJmxCommandRuntimeParameters(
            final TestCommand testCommand,
            final UUID commandId,
            final String thisShouldBeJmxCommandRuntimeParameters) {

    }

    @HandlesSystemCommand("some-command_7")
    public void tooFewParameters(
            final TestCommand testCommand,
            final UUID commandId) {

    }
}
