package uk.gov.justice.raml.jms.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.subscription.jms.core.JmsEndpointGenerationObjects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JmsEndpointGeneratorFactoryTest {

    @Mock
    private JmsEndpointGenerationObjects jmsEndpointGenerationObjects;

    @InjectMocks
    private JmsEndpointGeneratorFactory jmsEndpointGeneratorFactory;

    @Test
    public void shouldReturnANewJmsEndpointGenerator() throws Exception {

        final JmsEndpointGenerator jmsEndpointGenerator = mock(JmsEndpointGenerator.class);

        when(jmsEndpointGenerationObjects.jmsEndpointGenerator()).thenReturn(jmsEndpointGenerator);

        assertThat(jmsEndpointGeneratorFactory.create(), is(sameInstance(jmsEndpointGenerator)));
    }
}
