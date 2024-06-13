package uk.gov.justice.services.integrationtest.utils.jms.converters;

import io.restassured.path.json.JsonPath;

public class ToJsonPathMessageConverter implements MessageConverter<JsonPath> {

    @Override
    public JsonPath convert(String message) {
        return new JsonPath(message);
    }
}
