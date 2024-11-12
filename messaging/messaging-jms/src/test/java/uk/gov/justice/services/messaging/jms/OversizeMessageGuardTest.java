package uk.gov.justice.services.messaging.jms;

import static java.util.Optional.of;
import static java.util.UUID.fromString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class OversizeMessageGuardTest {

    @Mock
    private JmsMessagingConfiguration jmsMessagingConfiguration;

    @Mock
    private Logger logger;

    @InjectMocks
    private OversizeMessageGuard oversizeMessageGuard;

    @Test
    public void shouldLogAnErrorIfTheSizeOfTheEnvelopeMessageIsGreaterThanTheMessageSizeThreshold() throws Exception {

        final String envelopeName = "some.event.name";
        final UUID envelopeId = fromString("22c9aa4f-1365-45a2-9572-bb6fa88cd20b");
        final Optional<UUID> streamId = of(fromString("d4445eaa-77b7-4990-a17f-ac9b84052944"));
        final int oversizeMessageThresholdBytes = 43;
        final String largeEnvelope = """
                {
                    "some": "json"
                    "envelope": true
                }
                """;

        when(jmsMessagingConfiguration.getOversizeMessageThresholdBytes()).thenReturn(oversizeMessageThresholdBytes);

        oversizeMessageGuard.checkSizeOf(largeEnvelope, envelopeName, envelopeId, streamId);

        verify(logger).error("OVERSIZED_MESSAGE: 44 bytes. " +
                "The size of message 'some.event.name', " +
                "with id '22c9aa4f-1365-45a2-9572-bb6fa88cd20b' " +
                "and streamId 'd4445eaa-77b7-4990-a17f-ac9b84052944' is 44 bytes. " +
                "This exceeds the message size threshold of 43 bytes.");
    }

    @Test
    public void shouldDoNothingIfTheSizeOfTheEnvelopeMessageIsLessThanOrEqualToTheMessageSizeThreshold() throws Exception {

        final String envelopeName = "some.event.name";
        final UUID envelopeId = fromString("22c9aa4f-1365-45a2-9572-bb6fa88cd20b");
        final Optional<UUID> streamId = of(fromString("d4445eaa-77b7-4990-a17f-ac9b84052944"));
        final int oversizeMessageThresholdBytes = 44;
        final String largeEnvelope = """
                {
                    "some": "json"
                    "envelope": true
                }
                """;
        when(jmsMessagingConfiguration.getOversizeMessageThresholdBytes()).thenReturn(oversizeMessageThresholdBytes);

        oversizeMessageGuard.checkSizeOf(largeEnvelope, envelopeName, envelopeId, streamId);

        verifyNoInteractions(logger);
    }
}