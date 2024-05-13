package uk.gov.justice.services.integrationtest.utils.jms;

import org.junit.jupiter.api.Test;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToJsonEnvelopeMessageConverter;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToJsonPathMessageConverter;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToStringMessageConverter;
import uk.gov.justice.services.test.utils.core.messaging.TopicFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

class JmsSingletonResourceProviderTest {

    private final JmsSingletonResourceProvider jmsSingletonResourceProvider = new JmsSingletonResourceProvider();

    @Test
    void shouldCreateJmsMessageConsumerPool() {
        final JmsMessageConsumerPool result = jmsSingletonResourceProvider.getJmsMessageConsumerPool();

        assertNotNull(result);
        assertNotNull(getValueOfField(result, "jmsMessageConsumerFactory", JmsMessageConsumerFactory.class));
        assertNotNull(getValueOfField(result, "topicFactory", TopicFactory.class));
        assertNotNull(getValueOfField(result, "jmsMessageReader", JmsMessageReader.class));
    }

    @Test
    void shouldCreateJmsMessageProducerFactory() {
        final JmsMessageProducerFactory result = jmsSingletonResourceProvider.getJmsMessageProducerFactory();

        assertNotNull(result);
        assertNotNull(getValueOfField(result, "jmsSessionFactory", JmsSessionFactory.class));
        assertNotNull(getValueOfField(result, "topicFactory", TopicFactory.class));
    }

    @Test
    void shouldCreateJmsMessageClientFactory() {
        final JmsMessageClientFactory result = jmsSingletonResourceProvider.getJmsMessageClientFactory();

        assertNotNull(result);
        assertNotNull(getValueOfField(result, "jmsMessageProducerFactory", JmsMessageProducerFactory.class));
        assertNotNull(getValueOfField(result, "toStringMessageConverter", ToStringMessageConverter.class));
        assertNotNull(getValueOfField(result, "toJsonEnvelopeMessageConverter", ToJsonEnvelopeMessageConverter.class));
        assertNotNull(getValueOfField(result, "toJsonPathMessageConverter", ToJsonPathMessageConverter.class));
        assertNotNull(getValueOfField(result, "jmsMessageReader", JmsMessageReader.class));
        assertNotNull(getValueOfField(result, "jmsMessageConsumerPool", JmsMessageConsumerPool.class));
    }
}