package uk.gov.justice.services.jmx.runner;

import org.slf4j.Logger;
import uk.gov.justice.services.jmx.api.SystemCommandInvocationException;
import uk.gov.justice.services.jmx.api.command.SystemCommand;
import uk.gov.justice.services.jmx.command.SystemCommandStore;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static javax.transaction.Transactional.TxType.NEVER;

public class SystemCommandRunner {

    @Inject
    private SystemCommandStore systemCommandStore;

    @Inject
    private Logger logger;

    @Transactional(NEVER)
    public void run(final SystemCommand systemCommand, final UUID commandId, final Optional<UUID> commandRuntimeId) throws SystemCommandInvocationException {
        if(commandRuntimeId.isPresent())  {
            logger.info(format("Running system command '%s' with %s '%s'", systemCommand.getName(), systemCommand.commandRuntimeIdType(), commandRuntimeId.get()));
        } else {
            logger.info(format("Running system command '%s'", systemCommand.getName()));
        }

        systemCommandStore.findCommandProxy(systemCommand).invokeCommand(systemCommand, commandId, commandRuntimeId);
    }
}
