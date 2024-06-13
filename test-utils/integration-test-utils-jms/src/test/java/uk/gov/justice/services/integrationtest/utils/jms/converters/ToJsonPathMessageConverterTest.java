package uk.gov.justice.services.integrationtest.utils.jms.converters;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ToJsonPathMessageConverterTest {
    private final ToJsonPathMessageConverter toJsonPathMessageConverter = new ToJsonPathMessageConverter();

    @Test
    void verifyConvert() {
        final String message = """
                {
                    "key": "value"
                }                        
                """;

        final JsonPath result = toJsonPathMessageConverter.convert(message);

        assertThat(result.getString("key"), is("value"));
    }
}