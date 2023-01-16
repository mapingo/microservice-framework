package uk.gov.justice.services.test.utils.core.messaging;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoInteractions;
import static uk.gov.justice.services.test.utils.core.messaging.MessageProducerClientBuilder.aMessageProducerClient;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageProducerClientBuilderTest {

    @Mock
    private ActiveMQConnectionFactory activeMQConnectionFactory;

    @InjectMocks
    private MessageProducerClientBuilder messageProducerClientBuilder;

    @Test
    public void shouldHaveStaticFactoryMethod() throws Exception {
        assertThat(aMessageProducerClient(), is(notNullValue()));
    }

    @Test
    public void shouldCreateMessageConsumerClientWithNoExtraActiveMQConnectionFactoryProperties() throws Exception {

        final MessageProducerClient messageProducerClient = messageProducerClientBuilder.build();
        assertThat(messageProducerClient, is(notNullValue()));

        verifyNoInteractions(activeMQConnectionFactory);

        final ActiveMQConnectionFactory theActiveMQConnectionFactory = getValueOfField(messageProducerClient, "activeMQConnectionFactory", ActiveMQConnectionFactory.class);


        assertThat(theActiveMQConnectionFactory, is(sameInstance(activeMQConnectionFactory)));
    }

    @Test
    public void shouldSetRetryIntervalOnActiveMQConnectionFactory() throws Exception {

        final int retryInterval = 23;

        final MessageProducerClient messageProducerClient = messageProducerClientBuilder
                .withRetryInterval(retryInterval)
                .build();
        assertThat(messageProducerClient, is(notNullValue()));

        verify(activeMQConnectionFactory).setRetryInterval(retryInterval);
        verifyNoMoreInteractions(activeMQConnectionFactory);
    }

    @Test
    public void shouldSetMaxRetryIntervalOnActiveMQConnectionFactory() throws Exception {

        final int maxRetryInterval = 23;

        final MessageProducerClient messageProducerClient = messageProducerClientBuilder
                .withMaxRetryInterval(maxRetryInterval)
                .build();
        assertThat(messageProducerClient, is(notNullValue()));

        verify(activeMQConnectionFactory).setMaxRetryInterval(maxRetryInterval);
        verifyNoMoreInteractions(activeMQConnectionFactory);
    }

    @Test
    public void shouldSetMaxRetryIntervalMultiplierOnActiveMQConnectionFactory() throws Exception {

        final double retryIntervalMultiplier = 23.1;

        final MessageProducerClient messageProducerClient = messageProducerClientBuilder
                .withRetryIntervalMultiplier(retryIntervalMultiplier)
                .build();
        assertThat(messageProducerClient, is(notNullValue()));

        verify(activeMQConnectionFactory).setRetryIntervalMultiplier(retryIntervalMultiplier);
        verifyNoMoreInteractions(activeMQConnectionFactory);
    }

    @Test
    public void shouldSetReconnectAttemptsOnActiveMQConnectionFactory() throws Exception {

        final int reconnectAttempts = 23;

        final MessageProducerClient messageProducerClient = messageProducerClientBuilder
                .withReconnectAttempts(reconnectAttempts)
                .build();
        assertThat(messageProducerClient, is(notNullValue()));

        verify(activeMQConnectionFactory).setReconnectAttempts(reconnectAttempts);
        verifyNoMoreInteractions(activeMQConnectionFactory);
    }
}