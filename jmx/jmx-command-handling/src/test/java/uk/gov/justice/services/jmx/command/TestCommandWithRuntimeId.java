package uk.gov.justice.services.jmx.command;

import uk.gov.justice.services.jmx.api.command.BaseSystemCommand;

public class TestCommandWithRuntimeId extends BaseSystemCommand {

    public static final String TEST_COMMAND = "TEST_COMMAND";

    public TestCommandWithRuntimeId() {
        super(TEST_COMMAND, "This is a command used for testing");
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
