package uk.gov.justice.services.jmx.system.command.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.services.jmx.system.command.client.connection.JMXConnectorFactory;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxParameters;
import uk.gov.justice.services.jmx.system.command.client.connection.MBeanConnector;

import javax.management.remote.JMXConnector;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

@ExtendWith(MockitoExtension.class)
public class TestSystemCommanderClientFactoryTest {

    @Mock
    private ConnectorObjectFactory connectorObjectFactory;

    @InjectMocks
    private TestSystemCommanderClientFactory testSystemCommanderClientFactory;

    @Test
    public void shouldCreateCorrectlyConfiguredInstanceOfSystemCommanderClient() throws Exception {

        final JMXConnectorFactory jmxConnectorFactory = mock(JMXConnectorFactory.class);
        final MBeanConnector mBeanConnector = mock(MBeanConnector.class);
        final JmxParameters jmxParameters = mock(JmxParameters.class);
        final JMXConnector jmxConnector = mock(JMXConnector.class);

        when(connectorObjectFactory.jmxConnectorFactory()).thenReturn(jmxConnectorFactory);
        when(connectorObjectFactory.mBeanConnector()).thenReturn(mBeanConnector);
        when(jmxConnectorFactory.createJmxConnector(jmxParameters)).thenReturn(jmxConnector);

        final SystemCommanderClient systemCommanderClient = testSystemCommanderClientFactory.create(jmxParameters);

        assertThat(getValueOfField(systemCommanderClient, "jmxConnector", JMXConnector.class), is(jmxConnector));
        assertThat(getValueOfField(systemCommanderClient, "mBeanConnector", MBeanConnector.class), is(mBeanConnector));
    }
}