package uk.gov.justice.services.integrationtest.utils.jms;

import uk.gov.justice.services.integrationtest.utils.jms.converters.ToJsonEnvelopeMessageConverter;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToJsonPathMessageConverter;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToStringMessageConverter;

class JmsMessageClientFactory {

    private final JmsMessageProducerFactory jmsMessageProducerFactory;
    private final ToStringMessageConverter toStringMessageConverter;
    private final ToJsonEnvelopeMessageConverter toJsonEnvelopeMessageConverter;
    private final ToJsonPathMessageConverter toJsonPathMessageConverter;
    private final JmsMessageReader jmsMessageReader;
    private final JmsMessageConsumerPool jmsMessageConsumerPool;

    JmsMessageClientFactory(final JmsMessageProducerFactory jmsMessageProducerFactory,
                            final ToStringMessageConverter toStringMessageConverter,
                            final ToJsonEnvelopeMessageConverter toJsonEnvelopeMessageConverter,
                            final ToJsonPathMessageConverter toJsonPathMessageConverter,
                            final JmsMessageReader jmsMessageReader,
                            final JmsMessageConsumerPool jmsMessageConsumerPool) {
        this.jmsMessageProducerFactory = jmsMessageProducerFactory;
        this.toStringMessageConverter = toStringMessageConverter;
        this.toJsonEnvelopeMessageConverter = toJsonEnvelopeMessageConverter;
        this.toJsonPathMessageConverter = toJsonPathMessageConverter;
        this.jmsMessageReader = jmsMessageReader;
        this.jmsMessageConsumerPool = jmsMessageConsumerPool;
    }

    DefaultJmsMessageProducerClient createJmsMessageProducerClient() {
        return new DefaultJmsMessageProducerClient(jmsMessageProducerFactory);
    }

    DefaultJmsMessageConsumerClient createJmsMessageConsumerClient() {
        return new DefaultJmsMessageConsumerClient(jmsMessageConsumerPool,
                jmsMessageReader,
                toStringMessageConverter,
                toJsonEnvelopeMessageConverter,
                toJsonPathMessageConverter);
    }
}
