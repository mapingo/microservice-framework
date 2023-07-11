package uk.gov.justice.subscription.jms.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SubscriptionJmsEndpointGeneratorFactoryTest {

    @Mock
    private JmsEndpointGenerationObjects jmsEndpointGenerationObjects;

    @InjectMocks
    private SubscriptionJmsEndpointGeneratorFactory subscriptionJmsEndpointGeneratorFactory;

    @Test
    public void shouldCreateANewSubscriptionJmsEndpointGenerator() throws Exception {

        final SubscriptionJmsEndpointGenerator subscriptionJmsEndpointGenerator = mock(SubscriptionJmsEndpointGenerator.class);

        when(jmsEndpointGenerationObjects.subscriptionJmsEndpointGenerator()).thenReturn(subscriptionJmsEndpointGenerator);

        assertThat(subscriptionJmsEndpointGeneratorFactory.create(), is(subscriptionJmsEndpointGenerator));
    }
}
