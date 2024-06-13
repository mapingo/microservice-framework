package uk.gov.justice.services.integrationtest.utils.jms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultJmsMessageProducerClientProviderTest {

    @Mock
    private JmsResourcesContext jmsResourcesContext;

    @Test
    void shouldGetMessageProducerClientAndReturnMessageProducerClient() {
        final DefaultJmsMessageProducerClient defaultJmsMessageProducerClient = mock(DefaultJmsMessageProducerClient.class);
        final JmsMessageClientFactory jmsMessageClientFactory = mock(JmsMessageClientFactory.class);
        when(jmsResourcesContext.getJmsMessageClientFactory()).thenReturn(jmsMessageClientFactory);
        when(jmsMessageClientFactory.createJmsMessageProducerClient()).thenReturn(defaultJmsMessageProducerClient);

        final JmsMessageProducerClient result = new JmsMessageProducerClientProvider("jms.topic.public.event", jmsResourcesContext)
                .getMessageProducerClient();

        assertThat(result, is(defaultJmsMessageProducerClient));
    }
}