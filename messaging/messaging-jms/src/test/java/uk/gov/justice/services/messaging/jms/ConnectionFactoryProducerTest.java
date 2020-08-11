package uk.gov.justice.services.messaging.jms;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.messaging.jms.ConnectionFactoryProducer.DEFAULT_CONNECTION_FACTORY_JNDI_PATTERN;
import static uk.gov.justice.services.messaging.jms.ConnectionFactoryProducer.EMPTY_STRING;

import uk.gov.justice.services.cdi.QualifierAnnotationExtractor;
import uk.gov.justice.services.messaging.jms.annotation.ConnectionFactoryName;

import javax.enterprise.inject.spi.InjectionPoint;
import javax.jms.ConnectionFactory;
import javax.naming.InitialContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConnectionFactoryProducerTest {

    @Mock
    private ConnectionFactoryName connectionFactoryNameAnnotation;

    @Mock
    private InitialContext initialContext;

    @Mock
    private ConnectionFactory defaultConnectionFactory;

    @Mock
    private ConnectionFactory connectionFactory;

    @Mock
    private QualifierAnnotationExtractor qualifierAnnotationExtractor;

    @InjectMocks
    private ConnectionFactoryProducer connectionFactoryProducer;

    @Test
    public void shouldCreateDefaultConnectionFactoryWhenNoConnectionFactoryNameQualifierSet() throws Exception {
        when(initialContext.lookup(DEFAULT_CONNECTION_FACTORY_JNDI_PATTERN)).thenReturn(defaultConnectionFactory);
        assertThat(connectionFactoryProducer.getConnectionFactory(), is(defaultConnectionFactory));
    }

    @Test
    public void shouldCreateDefaultConnectionFactoryWhenConnectionFactoryNameQualifierSetWithEmptyString() throws Exception {
        final InjectionPoint injectionPoint = mock(InjectionPoint.class);

        when(connectionFactoryNameAnnotation.value()).thenReturn(EMPTY_STRING);
        when(qualifierAnnotationExtractor.getFrom(injectionPoint, ConnectionFactoryName.class)).thenReturn(connectionFactoryNameAnnotation);
        when(initialContext.lookup(DEFAULT_CONNECTION_FACTORY_JNDI_PATTERN)).thenReturn(defaultConnectionFactory);

        assertThat(connectionFactoryProducer.getConnectionFactory(injectionPoint), is(defaultConnectionFactory));
    }

    @Test
    public void shouldCreateDefaultConnectionFactoryWhenConnectionFactoryNameQualifierSet() throws Exception {

        final String connectionFactoryName = "connectionFactoryName";
        final InjectionPoint injectionPoint = mock(InjectionPoint.class);

        when(connectionFactoryNameAnnotation.value()).thenReturn(connectionFactoryName);
        when(qualifierAnnotationExtractor.getFrom(injectionPoint, ConnectionFactoryName.class)).thenReturn(connectionFactoryNameAnnotation);
        when(initialContext.lookup(connectionFactoryName)).thenReturn(connectionFactory);

        assertThat(connectionFactoryProducer.getConnectionFactory(injectionPoint), is(connectionFactory));
    }
}