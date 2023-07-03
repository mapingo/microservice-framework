package uk.gov.justice.services.test.utils.core.matchers;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.withJsonPath;
import static java.util.UUID.randomUUID;
import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.justice.services.messaging.JsonEnvelope.envelopeFrom;
import static uk.gov.justice.services.test.utils.core.messaging.MetadataBuilderFactory.metadataWithRandomUUID;

import java.util.UUID;

import javax.json.JsonValue;

import org.junit.jupiter.api.Test;

public class JsonValueIsJsonMatcherTest {

    private static final UUID ID = randomUUID();
    private static final String NAME = "someName";

    @Test
    public void shouldMatchJsonValueAsJson() throws Exception {
        assertThat(payload(), JsonValueIsJsonMatcher.isJson(allOf(
                withJsonPath("$.someId", equalTo(ID.toString())),
                withJsonPath("$.name", equalTo(NAME))))
        );
    }

    @Test
    public void shouldNotMatchJsonValueAsJsonIfJsonDoesNotMatch() throws Exception {
        assertThrows(AssertionError.class, () ->assertThat(payload(), JsonValueIsJsonMatcher.isJson(allOf(
                withJsonPath("$.someId", equalTo(ID.toString())),
                withJsonPath("$.name", equalTo("will not match"))))
        ));
    }

    @Test
    public void shouldNotMatchJsonValueAsJsonIfJsonValueIsNotJsonObject() throws Exception {
        assertThrows(AssertionError.class, () ->assertThat(jsonEnvelopeWithJsonValueNullPayload(), JsonValueIsJsonMatcher.isJson(allOf(
                withJsonPath("$.someId", equalTo(ID.toString())),
                withJsonPath("$.name", equalTo(NAME))))
        ));
    }

    private JsonValue payload() {
        return createObjectBuilder()
                .add("someId", ID.toString())
                .add("name", NAME)
                .build();
    }

    private JsonValue jsonEnvelopeWithJsonValueNullPayload() {
        return envelopeFrom(metadataWithRandomUUID("event.action").build(), JsonValue.NULL).payload();
    }
}