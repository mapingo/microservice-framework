package uk.gov.justice.services.integrationtest.utils.jms;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToJsonEnvelopeMessageConverter;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToJsonPathMessageConverter;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToStringMessageConverter;
import uk.gov.justice.services.messaging.DefaultJsonObjectEnvelopeConverter;
import uk.gov.justice.services.test.utils.core.messaging.TopicFactory;

class JmsSingletonResourceProvider {

    //This must be singleton for junit extension to access same resources created by Tests
    private static final JmsMessageConsumerPool jmsMessageConsumerPool = createJmsMessageConsumerPool();

    //This must be singleton for junit extension to access same resources created by Tests
    private static final JmsMessageProducerFactory jmsMessageProducerFactory = createJmsMessageProducerFactory();

    //This singletonness is for efficiency to avoid creating whole chain for each client instance creation
    private static final JmsMessageClientFactory jmsMessageClientFactory = jmsMessageClientFactory();

    JmsMessageConsumerPool getJmsMessageConsumerPool() {
        return jmsMessageConsumerPool;
    }

    JmsMessageProducerFactory getJmsMessageProducerFactory() {
        return jmsMessageProducerFactory;
    }

    JmsMessageClientFactory getJmsMessageClientFactory() {
        return jmsMessageClientFactory;
    }

    private static JmsMessageConsumerPool createJmsMessageConsumerPool() {
        return new JmsMessageConsumerPool(new JmsMessageConsumerFactory(new JmsSessionFactory(new ActiveMQConnectionFactory())),
                new TopicFactory(), new JmsMessageReader());
    }

    private static JmsMessageProducerFactory createJmsMessageProducerFactory() {
        return new JmsMessageProducerFactory(new JmsSessionFactory(new ActiveMQConnectionFactory()), new TopicFactory());
    }

    private static JmsMessageClientFactory jmsMessageClientFactory() {
        return new JmsMessageClientFactory(jmsMessageProducerFactory,
                new ToStringMessageConverter(),
                new ToJsonEnvelopeMessageConverter(new DefaultJsonObjectEnvelopeConverter()),
                new ToJsonPathMessageConverter(),
                new JmsMessageReader(),
                jmsMessageConsumerPool);
    }
}
