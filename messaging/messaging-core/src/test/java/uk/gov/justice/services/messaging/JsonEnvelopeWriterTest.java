package uk.gov.justice.services.messaging;

import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class JsonEnvelopeWriterTest {

    private static final String EXPECTED_JSON =
            "\n{\n" +
                    "    \"aProperty\": \"value a\",\n" +
                    "    \"bProperty\": \"value b\",\n" +
                    "    \"cProperty\": \"value c\",\n" +
                    "    \"anObject\": {\n" +
                    "        \"innerProperty\": \"innerValue\"\n" +
                    "    }\n" +
                    "}";

    @Test
    public void shouldWriteAJsonObjectAsAPrettyPrintedString() throws Exception {

        final JsonObject jsonObject = createObjectBuilder()
                .add("aProperty", "value a")
                .add("bProperty", "value b")
                .add("cProperty", "value c")
                .add("anObject", createObjectBuilder()
                        .add("innerProperty", "innerValue"))
                .build();

        final String json = JsonEnvelopeWriter.writeJsonObject(jsonObject);

        assertThat(json, is(EXPECTED_JSON));
    }
}
