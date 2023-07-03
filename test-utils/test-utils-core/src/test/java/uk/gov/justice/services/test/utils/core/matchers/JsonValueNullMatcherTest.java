package uk.gov.justice.services.test.utils.core.matchers;

import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.json.JsonObject;
import javax.json.JsonValue;

import org.junit.jupiter.api.Test;

public class JsonValueNullMatcherTest {

    @Test
    public void shouldMatchJsonValueNull() throws Exception {
        assertThat(JsonValue.NULL, JsonValueNullMatcher.isJsonValueNull());
    }

    @Test
    public void shouldNotMatchJsonObject() throws Exception {
        final JsonObject jsonObject = createObjectBuilder()
                .add("someId", "idValue")
                .build();

        assertThrows(AssertionError.class, () -> assertThat(jsonObject, JsonValueNullMatcher.isJsonValueNull()));
    }
}