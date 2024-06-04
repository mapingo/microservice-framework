package uk.gov.justice.services.jmx.api.mbean;

import org.slf4j.Logger;
import uk.gov.justice.services.jmx.api.CommandNotFoundException;
import uk.gov.justice.services.jmx.api.UnrunnableSystemCommandException;
import uk.gov.justice.services.jmx.api.command.SystemCommand;
import uk.gov.justice.services.jmx.api.command.SystemCommandDetails;
import uk.gov.justice.services.jmx.api.domain.SystemCommandStatus;
import uk.gov.justice.services.jmx.command.CommandConverter;
import uk.gov.justice.services.jmx.command.SystemCommandLocator;
import uk.gov.justice.services.jmx.command.SystemCommandScanner;
import uk.gov.justice.services.jmx.runner.AsynchronousCommandRunner;
import uk.gov.justice.services.jmx.state.observers.SystemCommandStateBean;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

public class SystemCommander implements SystemCommanderMBean {

    @Inject
    private SystemCommandLocator systemCommandLocator;

    @Inject
    private AsynchronousCommandRunner asynchronousCommandRunner;

    @Inject
    private SystemCommandScanner systemCommandScanner;

    @Inject
    private SystemCommandStateBean systemCommandStateBean;

    @Inject
    private CommandConverter commandConverter;

    @Inject
    private SystemCommandVerifier systemCommandVerifier;

    @Inject
    private Logger logger;

    @Override
    public UUID call(final String systemCommandName) {
        logger.info(format("Received System Command '%s'", systemCommandName));
        logger.info(format("Running '%s' in '%s' mode", systemCommandName, CommandRunMode.FORCED));

        return doCall(systemCommandName, empty(), CommandRunMode.FORCED);
    }

    @Override
    public UUID call(final String systemCommandName, final CommandRunMode commandRunMode) {
        logger.info(format("Received System Command '%s'", systemCommandName));
        logger.info(format("Running '%s' in '%s' mode", systemCommandName, commandRunMode));

        return doCall(systemCommandName, empty(), commandRunMode);
    }

    @Override
    public UUID callWithRuntimeId(final String systemCommandName, final UUID commandRuntimeId, final CommandRunMode commandRunMode) {
        logger.info(format("Received System Command '%s' with UUID '%s'", systemCommandName, commandRuntimeId));
        logger.info(format("Running '%s' with UUID '%s' in '%s' mode", systemCommandName, commandRuntimeId, commandRunMode));

        return doCall(systemCommandName, of(commandRuntimeId), commandRunMode);
    }

    @Override
    public List<SystemCommandDetails> listCommands() {
        return systemCommandScanner.findCommands().stream()
                .map(commandConverter::toCommandDetails)
                .collect(toList());
    }

    @Override
    public SystemCommandStatus getCommandStatus(final UUID commandId) {

        return systemCommandStateBean
                .getCommandStatus(commandId)
                .orElseThrow(() -> new CommandNotFoundException(format("No SystemCommand found with id %s", commandId)));
    }

    private UUID doCall(final String systemCommandName, final Optional<UUID> commandRuntimeId, final CommandRunMode commandRunMode) {
        final SystemCommand systemCommand = systemCommandLocator
                .forName(systemCommandName)
                .orElseThrow(() -> new UnrunnableSystemCommandException(format("The system command '%s' is not supported on this context.", systemCommandName)));

        systemCommandVerifier.verify(systemCommand, commandRuntimeId);

        if (commandRunMode.isGuarded()) {
            if (systemCommandStateBean.commandInProgress(systemCommand)) {
                throw new UnrunnableSystemCommandException(format("Cannot run system command '%s'. A previous call to that command is still in progress.", systemCommand.getName()));
            }
        }

        return asynchronousCommandRunner.run(systemCommand, commandRuntimeId);
    }
}
