package uk.gov.justice.services.messaging.jms.cdi;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.messaging.jms.annotation.ConnectionFactoryName;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JmsConnectionFactoryJndiNameProviderTest {

    @Mock
    private JmsConnectionConfig jmsConnectionConfig;

    @InjectMocks
    private JmsConnectionFactoryJndiNameProvider jmsConnectionFactoryJndiNameProvider;

    @Test
    public void shouldGetTheDefaultName() throws Exception {
        assertThat(jmsConnectionFactoryJndiNameProvider.defaultConnectionFactoryJndiName(), is("java:comp/DefaultJMSConnectionFactory"));
    }

    @Test
    public void shouldReturnTheDefaultNameIfTheNameFromAnnotationIsEmpty() throws Exception {

        final ConnectionFactoryName connectionFactoryName = mock(ConnectionFactoryName.class);

        when(connectionFactoryName.value()).thenReturn("");
        final String actualName = jmsConnectionFactoryJndiNameProvider
                .determineConnectionFactoryName(connectionFactoryName);

        assertThat(actualName,is("java:comp/DefaultJMSConnectionFactory"));
    }

    @Test
    public void shouldReturnTheDefaultNameIfTheNameFromAnnotationIsNull() throws Exception {

        final ConnectionFactoryName connectionFactoryName = mock(ConnectionFactoryName.class);

        when(connectionFactoryName.value()).thenReturn(null);

        final String actualName = jmsConnectionFactoryJndiNameProvider
                .determineConnectionFactoryName(connectionFactoryName);

        assertThat(actualName,is("java:comp/DefaultJMSConnectionFactory"));
    }

    @Test
    public void shouldReturnTheDefaultNameIfTheNameFromAnnotationExistsButNotUsingAuditMessageBroker() throws Exception {

        final ConnectionFactoryName connectionFactoryName = mock(ConnectionFactoryName.class);

        when(connectionFactoryName.value()).thenReturn("java:jboss/SomeJMSConnectionFactory");
        when(jmsConnectionConfig.shouldUseSeparateAuditMessageBroker()).thenReturn(false);

        final String actualName = jmsConnectionFactoryJndiNameProvider
                .determineConnectionFactoryName(connectionFactoryName);

        assertThat(actualName, is("java:comp/DefaultJMSConnectionFactory"));
    }

    @Test
    public void shouldReturnTheNameFromAnnotationAsTheCorrectNameIfUsingAuditMessageBroker() throws Exception {

        final ConnectionFactoryName connectionFactoryName = mock(ConnectionFactoryName.class);

        when(connectionFactoryName.value()).thenReturn("java:jboss/SomeJMSConnectionFactory");
        when(jmsConnectionConfig.shouldUseSeparateAuditMessageBroker()).thenReturn(true);

        final String actualName = jmsConnectionFactoryJndiNameProvider
                .determineConnectionFactoryName(connectionFactoryName);

        assertThat(actualName, is("java:jboss/SomeJMSConnectionFactory"));
    }
}
