package uk.gov.justice.services.core.envelope;


import static java.util.Optional.of;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.core.dispatcher.DispatcherConfiguration;
import uk.gov.justice.services.core.mapping.MediaType;
import uk.gov.justice.services.core.mapping.NameToMediaTypeConverter;
import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RequestResponseEnvelopeValidatorTest {

    @Mock
    private EnvelopeValidator envelopeValidator;

    @Mock
    private NameToMediaTypeConverter nameToMediaTypeConverter;

    @Mock
    private MediaTypeProvider mediaTypeProvider;

    @Mock
    private EnvelopeInspector envelopeInspector;

    @Mock
    private DispatcherConfiguration dispatcherConfiguration;

    @InjectMocks
    private RequestResponseEnvelopeValidator requestResponseEnvelopeValidator;

    @Test
    public void shouldValidateRequestEnvelope() throws Exception {

        final String actionName = "example.action-name";

        final JsonEnvelope jsonEnvelope = mock(JsonEnvelope.class);
        final MediaType mediaType = mock(MediaType.class);

        when(envelopeInspector.getActionNameFor(jsonEnvelope)).thenReturn(actionName);
        when(nameToMediaTypeConverter.convert(actionName)).thenReturn(mediaType);

        requestResponseEnvelopeValidator.validateRequest(jsonEnvelope);

        verify(envelopeValidator).validate(jsonEnvelope, actionName, of(mediaType));
    }

    @Test
    public void shouldValidateResponseEnvelope() throws Exception {

        final String actionName = "example.action-name";

        final JsonEnvelope jsonEnvelope = mock(JsonEnvelope.class);
        final Optional<MediaType> mediaType = of(mock(MediaType.class));

        when(envelopeInspector.getActionNameFor(jsonEnvelope)).thenReturn(actionName);
        when(mediaTypeProvider.getResponseMediaType(actionName)).thenReturn(mediaType);
        when(dispatcherConfiguration.shouldValidateRestResponseJson()).thenReturn(true);

        requestResponseEnvelopeValidator.validateResponse(jsonEnvelope);

        verify(envelopeValidator).validate(jsonEnvelope, actionName, mediaType);
    }

    @Test
    public void shouldNoValidateResponseEnvelopeIfValidationDisabled() throws Exception {

        final String actionName = "example.action-name";

        final JsonEnvelope jsonEnvelope = mock(JsonEnvelope.class);
        final Optional<MediaType> mediaType = of(mock(MediaType.class));

        when(dispatcherConfiguration.shouldValidateRestResponseJson()).thenReturn(false);

        requestResponseEnvelopeValidator.validateResponse(jsonEnvelope);

        verify(envelopeValidator, never()).validate(jsonEnvelope, actionName, mediaType);
    }
}
