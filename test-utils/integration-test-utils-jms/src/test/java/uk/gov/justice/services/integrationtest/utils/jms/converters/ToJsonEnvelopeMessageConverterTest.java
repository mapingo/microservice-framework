package uk.gov.justice.services.integrationtest.utils.jms.converters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.messaging.JsonObjectEnvelopeConverter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ToJsonEnvelopeMessageConverterTest {

    @Mock
    private JsonObjectEnvelopeConverter jsonEnvelopeConverter;

    @InjectMocks
    private ToJsonEnvelopeMessageConverter toJsonEnvelopeMessageConverter;

    @Test
    void verifyConvert() {
        final JsonEnvelope jsonEnvelope = mock(JsonEnvelope.class);
        when(jsonEnvelopeConverter.asEnvelope(anyString())).thenReturn(jsonEnvelope);

        final JsonEnvelope result = toJsonEnvelopeMessageConverter.convert("message");
        assertThat(result, is(jsonEnvelope));
    }
}