package uk.gov.justice.services.messaging.jms;

import static java.lang.String.format;

import uk.gov.justice.services.cdi.QualifierAnnotationExtractor;
import uk.gov.justice.services.jdbc.persistence.JdbcRepositoryException;
import uk.gov.justice.services.messaging.jms.annotation.ConnectionFactoryName;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@ApplicationScoped
@Default
@Priority(200)
public class ConnectionFactoryProducer {
    public static final String DEFAULT_CONNECTION_FACTORY_JNDI_PATTERN = "java:comp/DefaultJMSConnectionFactory";
    public static final String EMPTY_STRING = "";

    @Inject
    private InitialContext initialContext;

    @Inject
    private QualifierAnnotationExtractor qualifierAnnotationExtractor;


    private final Map<String, ConnectionFactory> connectionFactories = new ConcurrentHashMap<>();

    @Produces
    public ConnectionFactory getConnectionFactory() {
        return connectionFactories.computeIfAbsent(DEFAULT_CONNECTION_FACTORY_JNDI_PATTERN, this::connectionFactory);
    }

    @Produces
    @ConnectionFactoryName
    public ConnectionFactory getConnectionFactory(final InjectionPoint injectionPoint) {
        final String connectionFactoryName = qualifierAnnotationExtractor.getFrom(injectionPoint, ConnectionFactoryName.class).value();
        if (connectionFactoryName.equalsIgnoreCase(EMPTY_STRING)){
            return connectionFactories.computeIfAbsent(DEFAULT_CONNECTION_FACTORY_JNDI_PATTERN, this::connectionFactory);
        }
        return connectionFactories.computeIfAbsent(connectionFactoryName, this::connectionFactory);
    }

    private ConnectionFactory connectionFactory(final String jndiName) {
        try {
            ConnectionFactory connectionFactory = (ConnectionFactory) initialContext
                    .lookup(jndiName);
            return connectionFactory;
        } catch (final NamingException e) {
            throw new JndiException(format("Failed to lookup ConnectionFactory using jndi name '%s'", jndiName), e);
        }
    }
}
