package uk.gov.justice.services.jmx.api.mbean;

import uk.gov.justice.services.jmx.api.command.SystemCommandDetails;
import uk.gov.justice.services.jmx.api.domain.SystemCommandStatus;
import uk.gov.justice.services.jmx.api.parameters.JmxCommandRuntimeParameters;

import java.util.List;
import java.util.UUID;

public interface SystemCommanderMBean {

    UUID call(final String systemCommandName, final UUID commandRuntimeId, final String commandRuntimeString, final boolean guarded);
    List<SystemCommandDetails> listCommands();
    SystemCommandStatus getCommandStatus(final UUID commandId);
}
