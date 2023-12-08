package uk.gov.justice.services.jmx.system.command.client;

import uk.gov.justice.services.jmx.system.command.client.connection.JMXConnectorFactory;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;
import uk.gov.justice.services.jmx.system.command.client.connection.MBeanConnector;

public class SystemCommanderClientFactory {

    private final MBeanConnector mBeanConnector;
    private final JMXConnectorFactory jmxConnectorFactory;

    public SystemCommanderClientFactory(
            final MBeanConnector mBeanConnector,
            final JMXConnectorFactory jmxConnectorFactory) {
        this.mBeanConnector = mBeanConnector;
        this.jmxConnectorFactory = jmxConnectorFactory;
    }

    public SystemCommanderClient create(final JmxParameters jmxParameters) {

        return new SystemCommanderClient(
                jmxConnectorFactory.createJmxConnector(jmxParameters),
                mBeanConnector);
    }
}
