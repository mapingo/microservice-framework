package uk.gov.justice.services.messaging.jms;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import uk.gov.justice.services.messaging.JsonEnvelope;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultJmsEnvelopeSenderTest {

    @Mock
    private JmsSender jmsSender;

    @InjectMocks
    private DefaultJmsEnvelopeSender jmsEnvelopeSender;

    @Test
    public void shouldPublishValidEnvelopeToDestination() throws Exception {

        final String destinationName = "destination name";

        final JsonEnvelope jsonEnvelope = mock(JsonEnvelope.class);

        jmsEnvelopeSender.send(jsonEnvelope, destinationName);

        verify(jmsSender).send(jsonEnvelope, destinationName);
    }
}
