package uk.gov.justice.services.integrationtest.utils.jms.converters;

import uk.gov.justice.services.messaging.DefaultJsonObjectEnvelopeConverter;
import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.messaging.JsonObjectEnvelopeConverter;

public class ToJsonEnvelopeMessageConverter implements MessageConverter<JsonEnvelope> {

    private final JsonObjectEnvelopeConverter jsonEnvelopeConverter;

    public ToJsonEnvelopeMessageConverter(JsonObjectEnvelopeConverter jsonEnvelopeConverter) {
        this.jsonEnvelopeConverter = jsonEnvelopeConverter;
    }

    @Override
    public JsonEnvelope convert(String message) {
        return jsonEnvelopeConverter.asEnvelope(message);
    }
}
