package uk.gov.justice.services.integrationtest.utils.jms;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JmsResourcesContextTest {

    @Mock
    private  JmsMessageConsumerPool jmsMessageConsumerPool;
    @Mock
    private JmsMessageProducerFactory jmsMessageProducerFactory;
    @Mock
    private JmsMessageClientFactory jmsMessageClientFactory;
    @Mock
    private StopWatch stopWatch;

    private JmsResourcesContext jmsResourcesContext;
    @BeforeEach
    void setUp() {
        jmsResourcesContext = new JmsResourcesContext(jmsMessageConsumerPool, jmsMessageProducerFactory, jmsMessageClientFactory, stopWatch);
    }

    @Test
    void shouldClearMessages() {
        jmsResourcesContext.clearMessages();

        verify(jmsMessageConsumerPool).clearMessages();
    }

    @Test
    void shouldCloseConsumersAndProducers() {
        jmsResourcesContext.closeConsumersAndProducers();

        verify(jmsMessageConsumerPool).closeConsumers();
        verify(jmsMessageProducerFactory).closeProducers();
    }

    @Test
    void shouldCloseJmsResources() {
        final InOrder inOrder = inOrder(stopWatch, jmsMessageConsumerPool, jmsMessageProducerFactory);

        jmsResourcesContext.close();

        inOrder.verify(stopWatch).start();
        inOrder.verify(jmsMessageConsumerPool).close();
        inOrder.verify(jmsMessageProducerFactory).close();
        inOrder.verify(stopWatch).stop();
        inOrder.verify(stopWatch).getTime(TimeUnit.SECONDS);
    }
}