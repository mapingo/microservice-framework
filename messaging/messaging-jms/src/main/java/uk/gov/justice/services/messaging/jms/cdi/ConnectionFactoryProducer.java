package uk.gov.justice.services.messaging.jms.cdi;

import uk.gov.justice.services.cdi.QualifierAnnotationExtractor;
import uk.gov.justice.services.messaging.jms.annotation.ConnectionFactoryName;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;

@ApplicationScoped
@Default
@Priority(200)
public class ConnectionFactoryProducer {

    @Inject
    private QualifierAnnotationExtractor qualifierAnnotationExtractor;

    @Inject
    private JmsConnectionFactoryJndiNameProvider jmsConnectionFactoryJndiNameProvider;

    @Inject
    private JmsConnectionFactoryCache jmsConnectionFactoryCache;

    @Produces
    public ConnectionFactory connectionFactory() {
        final String defaultConnectionFactoryName = jmsConnectionFactoryJndiNameProvider
                .defaultConnectionFactoryJndiName();

        return jmsConnectionFactoryCache.getConnectionFactory(defaultConnectionFactoryName);
    }

    @Produces
    @ConnectionFactoryName
    public ConnectionFactory connectionFactory(final InjectionPoint injectionPoint) {
        final ConnectionFactoryName connectionFactoryName = qualifierAnnotationExtractor
                .getFrom(injectionPoint, ConnectionFactoryName.class);

        final String connectionFactoryJndiName = jmsConnectionFactoryJndiNameProvider
                .determineConnectionFactoryName(connectionFactoryName);

        return jmsConnectionFactoryCache.getConnectionFactory(connectionFactoryJndiName);
    }
}
