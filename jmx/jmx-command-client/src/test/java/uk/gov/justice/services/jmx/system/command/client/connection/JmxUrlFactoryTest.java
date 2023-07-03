package uk.gov.justice.services.jmx.system.command.client.connection;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import uk.gov.justice.services.jmx.system.command.client.MBeanClientConnectionException;

import java.net.MalformedURLException;

import javax.management.remote.JMXServiceURL;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JmxUrlFactoryTest {

    @InjectMocks
    private JmxUrlFactory jmxUrlFactory;

    @Test
    public void shouldCreateACorrectlyFormedJmxUrl() throws Exception {

        final String host = "localhost";
        final int port = 9009;

        final JMXServiceURL jmxServiceURL = jmxUrlFactory.createUrl(host, port);

        assertThat(jmxServiceURL.toString(), is("service:jmx:remote+http://localhost:9009"));
    }

    @Test
    public void shouldThrowExceptionOfHostnameIsMalformed() throws Exception {

        final String dodglyHostName = "{}\\";
        final int port = 9009;

        try {
            jmxUrlFactory.createUrl(dodglyHostName, port);
            fail();
        } catch (final MBeanClientConnectionException expected) {
            assertThat(expected.getMessage(), is("Failed to create JMX service url using host '{}\\' and port 9009"));
            assertThat(expected.getCause(), is(instanceOf(MalformedURLException.class)));
        }
    }
}
