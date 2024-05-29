package uk.gov.justice.services.management.ping.commands;

import uk.gov.justice.services.jmx.api.command.BaseSystemCommand;

public class LogRuntimeIdCommand extends BaseSystemCommand {

    public static final String LOG_RUNTIME_ID = "LOG_RUNTIME_ID";
    public static final String DESCRIPTION = "Logs to console any uuid set from the JmxClient using the  switch";

    public LogRuntimeIdCommand() {
        super(LOG_RUNTIME_ID, DESCRIPTION);
    }

    @Override
    public boolean requiresCommandRuntimeId() {
        return true;
    }

    @Override
    public String commandRuntimeIdType() {
        return "command-runtime-id";
    }
}
