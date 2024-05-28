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
class DefaultJmsMessageConsumerClientProviderTest {

    @Mock
    private JmsResourcesContext jmsResourcesContext;

    @Test
    void shouldStartConsumerAndReturnMessageConsumerClient() {
        final DefaultJmsMessageConsumerClient defaultJmsMessageConsumerClient = mock(DefaultJmsMessageConsumerClient.class);
        final JmsMessageClientFactory jmsMessageClientFactory = mock(JmsMessageClientFactory.class);
        when(jmsResourcesContext.getJmsMessageClientFactory()).thenReturn(jmsMessageClientFactory);
        when(jmsMessageClientFactory.createJmsMessageConsumerClient()).thenReturn(defaultJmsMessageConsumerClient);

        final DefaultJmsMessageConsumerClient result = new JmsMessageConsumerClientProvider("jms.topic.public.event", jmsResourcesContext)
                .withEventNames("event1").getMessageConsumerClient();

        assertThat(result, is(defaultJmsMessageConsumerClient));
        verify(defaultJmsMessageConsumerClient).startConsumer("jms.topic.public.event", List.of("event1"));
    }

    @Test
    void shouldThrowErrorWhenEventNameIsNull() {
        final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class,
                () -> new JmsMessageConsumerClientProvider("topicName", jmsResourcesContext)
                .withEventNames(null).getMessageConsumerClient());

        assertThat(e.getMessage(), is("eventName must be supplied"));
    }

    @Test
    void shouldThrowErrorWhenEventNameIsEmpty() {
        final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class,
                () -> new JmsMessageConsumerClientProvider("topicName", jmsResourcesContext)
                .withEventNames("").getMessageConsumerClient());

        assertThat(e.getMessage(), is("eventName must be supplied"));
    }
}