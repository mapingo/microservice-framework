package uk.gov.justice.services.jmx.system.command.client.connection;

import static java.lang.String.format;

import uk.gov.justice.services.jmx.api.name.CommandMBeanNameProvider;
import uk.gov.justice.services.jmx.system.command.client.MBeanClientConnectionException;
import uk.gov.justice.services.jmx.system.command.client.MBeanClientException;

import java.io.IOException;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;

public class MBeanConnector {

    private final CommandMBeanNameProvider commandMBeanNameProvider;
    private final RemoteMBeanFactory remoteMBeanFactory;

    public MBeanConnector(
            final CommandMBeanNameProvider commandMBeanNameProvider,
            final RemoteMBeanFactory remoteMBeanFactory) {
        this.commandMBeanNameProvider = commandMBeanNameProvider;
        this.remoteMBeanFactory = remoteMBeanFactory;
    }

    public <T> T connect(final String contextName, final Class<T> mBeanInterface, final JMXConnector jmxConnector) {
        try {
            final ObjectName objectName = commandMBeanNameProvider.create(contextName);
            final MBeanServerConnection connection = jmxConnector.getMBeanServerConnection();

            if(connection.isRegistered(objectName)) {
                return remoteMBeanFactory.createRemote(connection, objectName, mBeanInterface);
            }

            throw new MBeanClientException(format("No JMX bean found with name '%s'. Is your context name of '%s' correct?", objectName.getKeyProperty("type"), contextName));

        } catch (final IOException e) {
            throw new MBeanClientConnectionException(format("Failed to get remote connection to MBean '%s'", mBeanInterface.getSimpleName()), e);
        }
    }
}
