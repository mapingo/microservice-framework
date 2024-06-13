package uk.gov.justice.services.jmx.system.command.client;

import uk.gov.justice.services.jmx.system.command.client.connection.JMXConnectorFactory;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;
import uk.gov.justice.services.jmx.system.command.client.connection.MBeanConnector;

import javax.management.remote.JMXConnector;

public class TestSystemCommanderClientFactory {

    private final ConnectorObjectFactory connectorObjectFactory;

    public TestSystemCommanderClientFactory() {
        this(new ConnectorObjectFactory());
    }

    public TestSystemCommanderClientFactory(ConnectorObjectFactory connectorObjectFactory) {
        this.connectorObjectFactory = connectorObjectFactory;
    }

    public SystemCommanderClient create(final JmxParameters jmxParameters) {

        final JMXConnectorFactory jmxConnectorFactory = connectorObjectFactory.jmxConnectorFactory();
        final MBeanConnector mBeanConnector = connectorObjectFactory.mBeanConnector();


        final JMXConnector jmxConnector = jmxConnectorFactory.createJmxConnector(jmxParameters);

        return new SystemCommanderClient(jmxConnector, mBeanConnector);
    }
}
