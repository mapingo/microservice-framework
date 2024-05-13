package uk.gov.justice.services.integrationtest.utils.jms;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import static java.lang.String.format;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;

class JmsSessionFactory implements AutoCloseable {

    private final ActiveMQConnectionFactory activeMQConnectionFactory;

    private Session session;
    private Connection connection;
    
    JmsSessionFactory(final ActiveMQConnectionFactory activeMQConnectionFactory) {
        this.activeMQConnectionFactory = activeMQConnectionFactory;
    }

    Session create(final String queueUri) {

        try {
            activeMQConnectionFactory.setBrokerURL(queueUri);
            connection = activeMQConnectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, AUTO_ACKNOWLEDGE);
            return session;
        } catch (final JMSException e) {
            throw new JmsMessagingClientException(format("Failed to create JMS session for queue uri '%s'", queueUri), e);
        }
    }

    @Override
    public void close() {
        doClose(session);
        doClose(connection);
        doClose(activeMQConnectionFactory);
    }

    private void doClose(final AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final Exception ignored) {
                // do nothing
            }
        }
    }
}
