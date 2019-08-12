package uk.gov.justice.services.jmx.command;

import static java.lang.String.format;

import uk.gov.justice.services.jmx.api.SystemCommandException;
import uk.gov.justice.services.jmx.api.command.SystemCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

@ApplicationScoped
public class SystemCommandStore {

    private Map<String, SystemCommandHandlerProxy> handlers = new HashMap<>();

    @Inject
    private Logger logger;

    public SystemCommandHandlerProxy findCommandProxy(final SystemCommand systemCommand) {

        final SystemCommandHandlerProxy systemCommandHandlerProxy = handlers.get(
                systemCommand.getName()
        );

        if (systemCommandHandlerProxy != null) {
            return systemCommandHandlerProxy;
        }

        final String message = format(
                "Failed to find SystemCommandHandler for command '%s'",
                systemCommand.getName());

        throw new SystemCommandException(message);
    }

    public void store(final List<SystemCommandHandlerProxy> systemCommandProxies) {
        systemCommandProxies.forEach(this::store);
    }

    private void store(final SystemCommandHandlerProxy systemCommandHandler) {

        logger.info(format("Registering class %s as system command handler for '%s'", systemCommandHandler.getInstance().getClass().getSimpleName(), systemCommandHandler.getCommandName()));

        handlers.put(systemCommandHandler.getCommandName(), systemCommandHandler);
    }
}
