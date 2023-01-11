package uk.gov.justice.services.core.dispatcher;


import uk.gov.justice.services.core.envelope.RequestResponseEnvelopeValidator;
import uk.gov.justice.services.core.requester.Requester;
import uk.gov.justice.services.core.sender.Sender;
import uk.gov.justice.services.messaging.Envelope;
import uk.gov.justice.services.messaging.JsonEnvelope;

import javax.json.JsonValue;

public class DispatcherDelegate implements Requester, Sender {

    private final Dispatcher dispatcher;
    private final SystemUserUtil systemUserUtil;
    private final RequestResponseEnvelopeValidator requestResponseEnvelopeValidator;
    private final EnvelopePayloadTypeConverter envelopePayloadTypeConverter;
    private final JsonEnvelopeRepacker jsonEnvelopeRepacker;
    private final DispatcherConfiguration dispatcherConfiguration;

    public DispatcherDelegate(final Dispatcher dispatcher,
                              final SystemUserUtil systemUserUtil,
                              final RequestResponseEnvelopeValidator requestResponseEnvelopeValidator,
                              final EnvelopePayloadTypeConverter envelopePayloadTypeConverter,
                              final JsonEnvelopeRepacker jsonEnvelopeRepacker,
                              final DispatcherConfiguration dispatcherConfiguration) {
        this.dispatcher = dispatcher;
        this.systemUserUtil = systemUserUtil;
        this.requestResponseEnvelopeValidator = requestResponseEnvelopeValidator;
        this.envelopePayloadTypeConverter = envelopePayloadTypeConverter;
        this.jsonEnvelopeRepacker = jsonEnvelopeRepacker;
        this.dispatcherConfiguration = dispatcherConfiguration;
    }

    @Override
    public JsonEnvelope request(final Envelope<?> envelope) {
        return dispatchAndValidateResponse(envelope);
    }

    @Override
    public <T> Envelope<T> request(final Envelope<?> envelope, final Class<T> clazz) {
        final JsonEnvelope response = dispatchAndValidateResponse(envelope);

        return envelopePayloadTypeConverter.convert(response, clazz);
    }

    @Override
    public JsonEnvelope requestAsAdmin(final JsonEnvelope envelope) {

        final JsonEnvelope adminEnvelope = systemUserUtil.asEnvelopeWithSystemUserId(envelope);
        final JsonEnvelope response = dispatcher.dispatch(adminEnvelope);

        if (dispatcherConfiguration.shouldValidateRestResponseJson()) {
            requestResponseEnvelopeValidator.validateResponse(response);
        }

        return response;
    }

    @Override
    public <T> Envelope<T> requestAsAdmin(final Envelope<?> envelope, final Class<T> clazz) {

        final Envelope<JsonValue> convertedEnvelope = envelopePayloadTypeConverter.convert(
                envelope,
                JsonValue.class);
        final JsonEnvelope repackedEnvelope = jsonEnvelopeRepacker.repack(convertedEnvelope);
        final JsonEnvelope adminEnvelope = systemUserUtil.asEnvelopeWithSystemUserId(repackedEnvelope);
        final JsonEnvelope responseEnvelope = dispatcher.dispatch(adminEnvelope);

        if (dispatcherConfiguration.shouldValidateRestResponseJson()) {
            requestResponseEnvelopeValidator.validateResponse(responseEnvelope);
        }

        return envelopePayloadTypeConverter.convert(responseEnvelope, clazz);
    }

    @Override
    public void send(final Envelope<?> envelope) {

        final Envelope<JsonValue> convertedEnvelope = envelopePayloadTypeConverter.convert(
                envelope,
                JsonValue.class);

        final JsonEnvelope repackedEnvelope = jsonEnvelopeRepacker.repack(convertedEnvelope);

        requestResponseEnvelopeValidator.validateRequest(repackedEnvelope);
        dispatcher.dispatch(repackedEnvelope);
    }

    @Override
    public void sendAsAdmin(final JsonEnvelope envelope) {

        requestResponseEnvelopeValidator.validateRequest(envelope);

        final JsonEnvelope adminEnvelope = systemUserUtil.asEnvelopeWithSystemUserId(envelope);

        dispatcher.dispatch(adminEnvelope);
    }

    @Override
    public void sendAsAdmin(final Envelope<?> envelope) {

        final Envelope<JsonValue> convertedEnvelope = envelopePayloadTypeConverter.convert(envelope, JsonValue.class);
        final JsonEnvelope repackedEnvelope = jsonEnvelopeRepacker.repack(convertedEnvelope);

        requestResponseEnvelopeValidator.validateRequest(repackedEnvelope);

        final JsonEnvelope adminEnvelope = systemUserUtil.asEnvelopeWithSystemUserId(repackedEnvelope);
        dispatcher.dispatch(adminEnvelope);
    }

    private JsonEnvelope dispatchAndValidateResponse(final Envelope<?> envelope) {

        final Envelope<JsonValue> convertedEnvelope = envelopePayloadTypeConverter.convert(
                envelope,
                JsonValue.class);
        
        final JsonEnvelope repackedEnvelope = jsonEnvelopeRepacker.repack(convertedEnvelope);

        final JsonEnvelope response = dispatcher.dispatch(repackedEnvelope);

        if (dispatcherConfiguration.shouldValidateRestResponseJson()) {
            requestResponseEnvelopeValidator.validateResponse(response);
        }

        return response;
    }
}
