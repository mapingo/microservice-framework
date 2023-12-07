package uk.gov.justice.services.jmx.system.command.client;

import uk.gov.justice.framework.command.client.startup.ObjectFactory;
import uk.gov.justice.services.jmx.system.command.client.connection.JMXConnectorFactory;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;
import uk.gov.justice.services.jmx.system.command.client.connection.MBeanConnector;

import javax.management.remote.JMXConnector;

public class TestSystemCommanderClientFactory {

    private final ObjectFactory objectFactory;

    public TestSystemCommanderClientFactory() {
        this(new ObjectFactory());
    }

    public TestSystemCommanderClientFactory(final ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public SystemCommanderClient create(final JmxParameters jmxParameters) {

        final JMXConnectorFactory jmxConnectorFactory = objectFactory.jmxConnectorFactory();
        final MBeanConnector mBeanConnector = objectFactory.mBeanConnector();


        final JMXConnector jmxConnector = jmxConnectorFactory.createJmxConnector(jmxParameters);

        return new SystemCommanderClient(jmxConnector, mBeanConnector);
    }
}
