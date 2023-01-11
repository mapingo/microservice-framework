package uk.gov.justice.services.core.envelope;

import static java.util.Optional.of;

import uk.gov.justice.services.core.dispatcher.DispatcherConfiguration;
import uk.gov.justice.services.core.mapping.MediaType;
import uk.gov.justice.services.core.mapping.NameToMediaTypeConverter;
import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.Optional;

public class RequestResponseEnvelopeValidator {

    private final EnvelopeValidator envelopeValidator;
    private final NameToMediaTypeConverter nameToMediaTypeConverter;
    private final MediaTypeProvider mediaTypeProvider;
    private final EnvelopeInspector envelopeInspector;
    private final DispatcherConfiguration dispatcherConfiguration;

    public RequestResponseEnvelopeValidator(
            final EnvelopeValidator envelopeValidator,
            final NameToMediaTypeConverter nameToMediaTypeConverter,
            final MediaTypeProvider mediaTypeProvider,
            final EnvelopeInspector envelopeInspector,
            final DispatcherConfiguration dispatcherConfiguration) {
        this.envelopeValidator = envelopeValidator;
        this.nameToMediaTypeConverter = nameToMediaTypeConverter;
        this.mediaTypeProvider = mediaTypeProvider;
        this.envelopeInspector = envelopeInspector;
        this.dispatcherConfiguration = dispatcherConfiguration;
    }

    public void validateRequest(final JsonEnvelope jsonEnvelope) {

        final String actionName = envelopeInspector.getActionNameFor(jsonEnvelope);
        final MediaType mediaType = nameToMediaTypeConverter.convert(actionName);

        envelopeValidator.validate(jsonEnvelope, actionName, of(mediaType));
    }

    public void validateResponse(final JsonEnvelope jsonEnvelope) {

        if(dispatcherConfiguration.shouldValidateRestResponseJson()) {
            final String actionName = envelopeInspector.getActionNameFor(jsonEnvelope);
            final Optional<MediaType> mediaType = mediaTypeProvider.getResponseMediaType(actionName);

            envelopeValidator.validate(jsonEnvelope, actionName, mediaType);
        }
    }
}
