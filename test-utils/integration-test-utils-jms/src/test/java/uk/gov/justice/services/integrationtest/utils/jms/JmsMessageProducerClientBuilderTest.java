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
class JmsMessageProducerClientBuilderTest {

    @Mock
    private JmsResourcesContext jmsResourcesContext;

    @Test
    void shouldBuildAndReturnMessageProducerClient() {
        final JmsMessageProducerClient jmsMessageProducerClient = mock(JmsMessageProducerClient.class);
        final JmsMessageClientFactory jmsMessageClientFactory = mock(JmsMessageClientFactory.class);
        when(jmsResourcesContext.getJmsMessageClientFactory()).thenReturn(jmsMessageClientFactory);
        when(jmsMessageClientFactory.createJmsMessageProducerClient()).thenReturn(jmsMessageProducerClient);

        final JmsMessageProducerClient result = new JmsMessageProducerClientBuilder("jms.topic.public.event", jmsResourcesContext)
                .build();

        assertThat(result, is(jmsMessageProducerClient));
    }
}