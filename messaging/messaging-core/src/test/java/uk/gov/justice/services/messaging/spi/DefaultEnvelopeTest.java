package uk.gov.justice.services.messaging.spi;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.messaging.Envelope.envelopeFrom;

import uk.gov.justice.services.messaging.Metadata;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the {@link DefaultJsonEnvelope} class.
 */
@ExtendWith(MockitoExtension.class)
public class DefaultEnvelopeTest {

    @Mock
    private Metadata metadata;

    @Mock
    private Object payload;

    @Test
    public void shouldReturnMetadata() {
        assertThat(envelopeFrom(metadata, payload).metadata(), equalTo(metadata));
    }

    @Test
    public void shouldReturnPayload() {
        assertThat(envelopeFrom(metadata, payload).payload(), equalTo(payload));
    }
}