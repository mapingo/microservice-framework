package uk.gov.justice.services.jmx.system.command.client.connection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.jmx.ObjectNameFactory;
import uk.gov.justice.services.jmx.command.SystemCommanderMBean;
import uk.gov.justice.services.jmx.system.command.client.MBeanClientException;

import java.io.IOException;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MBeanConnectorTest {

    @Mock
    private ObjectNameFactory objectNameFactory;

    @Mock
    private RemoteMBeanFactory remoteMBeanFactory;

    @InjectMocks
    private MBeanConnector mBeanConnector;

    @Test
    public void shouldConnectToARemoteInstanceOfTheJmxBean() throws Exception {

        final String domain = "domain";
        final String typeName = "type name";
        final Class<SystemCommanderMBean> mBeanInterface = SystemCommanderMBean.class;

        final JMXConnector jmxConnector =  mock(JMXConnector.class);
        final ObjectName objectName = mock(ObjectName.class);
        final MBeanServerConnection connection = mock(MBeanServerConnection.class);
        final SystemCommanderMBean systemCommanderMBean = mock(SystemCommanderMBean.class);

        when(objectNameFactory.create(domain, "type", typeName)).thenReturn(objectName);
        when(jmxConnector.getMBeanServerConnection()).thenReturn(connection);
        when(remoteMBeanFactory.createRemote(connection, objectName, mBeanInterface)).thenReturn(systemCommanderMBean);

        assertThat(mBeanConnector.connect(domain, typeName, mBeanInterface, jmxConnector), is(systemCommanderMBean));
    }

    @Test
    public void shouldThrowExceptionIfConnectingToTheMBeanFails() throws Exception {

        final IOException ioException = new IOException("Ooops");

        final String domain = "domain";
        final String typeName = "type name";
        final Class<SystemCommanderMBean> mBeanInterface = SystemCommanderMBean.class;

        final JMXConnector jmxConnector =  mock(JMXConnector.class);
        final ObjectName objectName = mock(ObjectName.class);

        when(objectNameFactory.create(domain, "type", typeName)).thenReturn(objectName);
        when(jmxConnector.getMBeanServerConnection()).thenThrow(ioException);

        try {
            mBeanConnector.connect(domain, typeName, mBeanInterface, jmxConnector);
            fail();
        } catch (final MBeanClientException expected) {
            assertThat(expected.getCause(), is(ioException));
            assertThat(expected.getMessage(), is("Failed to get remote connection to MBean 'SystemCommanderMBean'"));
        }
    }
}