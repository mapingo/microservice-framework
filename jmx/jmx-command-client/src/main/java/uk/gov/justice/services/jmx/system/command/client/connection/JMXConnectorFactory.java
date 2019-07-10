package uk.gov.justice.services.jmx.system.command.client.connection;

import static java.lang.String.format;

import uk.gov.justice.services.jmx.system.command.client.MBeanClientException;

import java.io.IOException;
import java.util.Map;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;

public class JMXConnectorFactory {

    private final JmxUrlFactory jmxUrlFactory;
    private final ConnectorWrapper connectorWrapper;
    private final EnvironmentFactory environmentFactory;

    public JMXConnectorFactory(
            final JmxUrlFactory jmxUrlFactory,
            final ConnectorWrapper connectorWrapper,
            final EnvironmentFactory environmentFactory) {
        this.jmxUrlFactory = jmxUrlFactory;
        this.connectorWrapper = connectorWrapper;
        this.environmentFactory = environmentFactory;
    }

    public JMXConnector createJmxConnector(final JmxParametersBuilder jmxParametersBuilder) {

        final JmxParameters jmxParameters = jmxParametersBuilder.build();
        final Map<String, Object> environment = environmentFactory.create(jmxParameters);
        final String host = jmxParameters.getHost();
        final int port = jmxParameters.getPort();

        final JMXServiceURL serviceURL = jmxUrlFactory.createUrl(host, port);

        try {
            return connectorWrapper.connect(serviceURL, environment);
        } catch (final IOException e) {
            throw new MBeanClientException(format("Failed to connect to JMX using url '%s'", serviceURL), e);
        }
    }
}