package uk.gov.justice.framework.command.client;

import static uk.gov.justice.services.jmx.api.mbean.CommandRunMode.FORCED;
import static uk.gov.justice.services.jmx.api.mbean.CommandRunMode.GUARDED;

import uk.gov.justice.services.jmx.api.mbean.CommandRunMode;

import org.apache.commons.cli.CommandLine;

public class CommandRunModeSelector {

    public CommandRunMode selectCommandRunMode(final CommandLine commandLine) {
        if (commandLine.hasOption("force")) {
            return FORCED;
        }

        return GUARDED;
    }
}
