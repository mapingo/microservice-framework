package uk.gov.justice.services.healthcheck.servlet;

import static uk.gov.justice.services.messaging.JsonEnvelopeWriter.writeJsonObject;

import uk.gov.justice.services.common.converter.ObjectToJsonObjectConverter;
import uk.gov.justice.services.healthcheck.run.HealthcheckRunResults;

import javax.inject.Inject;
import javax.json.JsonObject;

public class HealthcheckToJsonConverter {

    @Inject
    private ObjectToJsonObjectConverter objectToJsonObjectConverter;

    public String toJson(final HealthcheckRunResults healthcheckRunResults) {

        final JsonObject jsonObject = objectToJsonObjectConverter.convert(healthcheckRunResults);

        return writeJsonObject(jsonObject);
    }
}
