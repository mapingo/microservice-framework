package uk.gov.justice.services.core.json;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the {@link FileBasedJsonSchemaValidator} class.
 */
@ExtendWith(MockitoExtension.class)
public class FileBasedJsonSchemaValidatorTest {

    @Mock
    private JsonSchemaLoader jsonSchemaLoader;

    @Mock
    private PayloadExtractor payloadExtractor;

    @Mock
    private SchemaValidationErrorMessageGenerator schemaValidationErrorMessageGenerator;

    @InjectMocks
    private FileBasedJsonSchemaValidator fileBasedJsonSchemaValidator;

    @Test
    public void shouldLoadASchemaFromTheFileSystemByItsNameAndValidate() throws Exception {

        final String actionName = "example.action-name";
        final String envelopeJson = "{\"envelope\": \"json\"}";

        final JSONObject payload = mock(JSONObject.class);
        final Schema schema = mock(Schema.class);

        when(payloadExtractor.extractPayloadFrom(envelopeJson)).thenReturn(payload);
        when(jsonSchemaLoader.loadSchema(actionName)).thenReturn(schema);

        fileBasedJsonSchemaValidator.validateWithoutSchemaCatalog(envelopeJson, actionName);

        verify(schema).validate(payload);
    }

    @Test
    public void shouldThrowExceptionIfSchemaValidationFails() throws Exception {

        final String actionName = "example.action-name";
        final String envelopeJson = "{\"envelope\": \"json\"}";
        final String errorMessage = "error message";

        final JSONObject payload = mock(JSONObject.class);
        final Schema schema = mock(Schema.class);
        final ValidationException validationException = mock(ValidationException.class);

        when(payloadExtractor.extractPayloadFrom(envelopeJson)).thenReturn(payload);
        when(jsonSchemaLoader.loadSchema(actionName)).thenReturn(schema);
        when(schemaValidationErrorMessageGenerator.generateErrorMessage(validationException)).thenReturn(errorMessage);
        doThrow(validationException).when(schema).validate(payload);

        try {
            fileBasedJsonSchemaValidator.validateWithoutSchemaCatalog(envelopeJson, actionName);
            fail();
        } catch (final JsonSchemaValidationException e) {
            assertThat(e.getMessage(), is(errorMessage));
            assertThat(e.getCause(), is(validationException));
        }
    }
}
