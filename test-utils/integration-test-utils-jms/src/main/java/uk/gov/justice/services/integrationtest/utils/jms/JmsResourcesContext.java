package uk.gov.justice.services.integrationtest.utils.jms;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.time.StopWatch;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToJsonEnvelopeMessageConverter;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToJsonPathMessageConverter;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToStringMessageConverter;
import uk.gov.justice.services.messaging.DefaultJsonObjectEnvelopeConverter;

import java.util.concurrent.TimeUnit;

public class JmsResourcesContext {

    private final JmsMessageConsumerPool jmsMessageConsumerPool;
    private final JmsMessageProducerFactory jmsMessageProducerFactory;
    private final JmsMessageClientFactory jmsMessageClientFactory;
    private final StopWatch stopWatch;

    JmsResourcesContext(final JmsMessageConsumerPool jmsMessageConsumerPool,
                        final JmsMessageProducerFactory jmsMessageProducerFactory) {
        this(jmsMessageConsumerPool, jmsMessageProducerFactory,
                new JmsMessageClientFactory(jmsMessageProducerFactory,
                        new ToStringMessageConverter(),
                        new ToJsonEnvelopeMessageConverter(new DefaultJsonObjectEnvelopeConverter()),
                        new ToJsonPathMessageConverter(),
                        new JmsMessageReader(),
                        jmsMessageConsumerPool),
                new StopWatch());
    }

    @VisibleForTesting
    JmsResourcesContext(final JmsMessageConsumerPool jmsMessageConsumerPool,
                        final JmsMessageProducerFactory jmsMessageProducerFactory,
                        final JmsMessageClientFactory jmsMessageClientFactory,
                        final StopWatch stopWatch) {
        this.jmsMessageConsumerPool = jmsMessageConsumerPool;
        this.jmsMessageProducerFactory = jmsMessageProducerFactory;
        this.jmsMessageClientFactory = jmsMessageClientFactory;
        this.stopWatch = stopWatch;
    }

    JmsMessageClientFactory getJmsMessageClientFactory() {
        return jmsMessageClientFactory;
    }

    void clearMessages() {
        jmsMessageConsumerPool.clearMessages();
    }

    void closeConsumersAndProducers() {
        jmsMessageConsumerPool.closeConsumers();
        jmsMessageProducerFactory.closeProducers();
    }

     void close() {
         System.out.println("----------Closing JMS resources-------------");

         stopWatch.start();
         jmsMessageConsumerPool.close();
         jmsMessageProducerFactory.close();
         stopWatch.stop();

         System.out.printf("----------JMS resources closed in %s secs\n", stopWatch.getTime(TimeUnit.SECONDS));
    }
}
