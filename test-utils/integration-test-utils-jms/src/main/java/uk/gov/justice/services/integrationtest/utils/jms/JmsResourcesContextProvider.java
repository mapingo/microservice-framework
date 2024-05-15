package uk.gov.justice.services.integrationtest.utils.jms;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import uk.gov.justice.services.test.utils.core.messaging.TopicFactory;

class JmsResourcesContextProvider implements CloseableResource {

    private static JmsResourcesContext jmsResourcesContext;

    JmsResourcesContext get() {
        if(jmsResourcesContext == null) {
            jmsResourcesContext = new JmsResourcesContext(createJmsMessageConsumerPool(), createJmsMessageProducerFactory());
        }

        return jmsResourcesContext;
    }

    private JmsMessageConsumerPool createJmsMessageConsumerPool() {
        return new JmsMessageConsumerPool(new JmsMessageConsumerFactory(new JmsSessionFactory(new ActiveMQConnectionFactory())),
                new TopicFactory(), new JmsMessageReader());
    }

    private JmsMessageProducerFactory createJmsMessageProducerFactory() {
        return new JmsMessageProducerFactory(new JmsSessionFactory(new ActiveMQConnectionFactory()), new TopicFactory());
    }

    @Override
    public void close() {
        if(jmsResourcesContext != null) {
            jmsResourcesContext.close();
            jmsResourcesContext = null;
        }
    }
}
