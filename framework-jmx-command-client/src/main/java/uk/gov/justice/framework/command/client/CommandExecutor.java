package uk.gov.justice.framework.command.client;

import uk.gov.justice.framework.command.client.io.CommandPrinter;
import uk.gov.justice.framework.command.client.jmx.SystemCommandInvoker;
import uk.gov.justice.services.jmx.api.command.SystemCommandDetails;
import uk.gov.justice.services.jmx.api.mbean.CommandRunMode;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;

import java.util.List;

import org.apache.commons.cli.CommandLine;

public class CommandExecutor {

    private final SystemCommandInvoker systemCommandInvoker;
    private final CommandPrinter commandPrinter;
    private final CommandRunModeSelector commandRunModeSelector;

    public CommandExecutor(
            final SystemCommandInvoker systemCommandInvoker,
            final CommandPrinter commandPrinter,
            final CommandRunModeSelector commandRunModeSelector) {
        this.systemCommandInvoker = systemCommandInvoker;
        this.commandPrinter = commandPrinter;
        this.commandRunModeSelector = commandRunModeSelector;
    }

    public void executeCommand(
            final CommandLine commandLine,
            final JmxParameters jmxParameters,
            final List<SystemCommandDetails> systemCommandDetails) {

        if (commandLine.hasOption("list")) {
            commandPrinter.printSystemCommands(systemCommandDetails);
        } else {
            final String commandName = commandLine.getOptionValue("command");
            final String commandRuntimeId = commandLine.getOptionValue("crid");
            final CommandRunMode commandRunMode = commandRunModeSelector.selectCommandRunMode(commandLine);
            systemCommandInvoker.runSystemCommand(commandName, jmxParameters, commandRuntimeId, commandRunMode);
        }
    }
}
