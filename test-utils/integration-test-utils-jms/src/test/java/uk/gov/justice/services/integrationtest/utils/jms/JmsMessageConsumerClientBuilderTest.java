package uk.gov.justice.services.integrationtest.utils.jms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JmsMessageConsumerClientBuilderTest {

    @Mock
    private JmsSingletonResourceProvider jmsSingletonResourceProvider;

    @Test
    void shouldStartConsumerAndReturnMessageConsumerClient() {
        final JmsMessageConsumerClient jmsMessageConsumerClient = mock(JmsMessageConsumerClient.class);
        final JmsMessageClientFactory jmsMessageClientFactory = mock(JmsMessageClientFactory.class);
        when(jmsSingletonResourceProvider.getJmsMessageClientFactory()).thenReturn(jmsMessageClientFactory);
        when(jmsMessageClientFactory.createJmsMessageConsumerClient()).thenReturn(jmsMessageConsumerClient);

        final JmsMessageConsumerClient result = new JmsMessageConsumerClientBuilder("jms.topic.public.event", jmsSingletonResourceProvider)
                .withEventNames("event1").build();

        assertThat(result, is(jmsMessageConsumerClient));
        verify(jmsMessageConsumerClient).startConsumer("jms.topic.public.event", List.of("event1"));
    }

    @Test
    void shouldThrowErrorWhenEventNameIsNull() {
        final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class,
                () -> new JmsMessageConsumerClientBuilder("topicName", jmsSingletonResourceProvider)
                .withEventNames(null).build());

        assertThat(e.getMessage(), is("eventName must be supplied"));
    }

    @Test
    void shouldThrowErrorWhenEventNameIsEmpty() {
        final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class,
                () -> new JmsMessageConsumerClientBuilder("topicName", jmsSingletonResourceProvider)
                .withEventNames("").build());

        assertThat(e.getMessage(), is("eventName must be supplied"));
    }
}