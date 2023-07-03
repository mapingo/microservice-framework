package uk.gov.justice.services.core.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.schema.catalog.SchemaCatalogResolver;

import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

/**
 * Unit tests for the {@link JsonSchemaLoader} class.
 */
@ExtendWith(MockitoExtension.class)
public class JsonSchemaLoaderTest {

    @Mock
    private Logger logger;

    @Mock
    private SchemaCatalogResolver schemaCatalogResolver;

    @InjectMocks
    private JsonSchemaLoader loader;

    @Test
    public void shouldReturnSchemaFromClasspath() {
        final Schema expectedSchema = mock(Schema.class);
        when(schemaCatalogResolver.loadSchema(any(JSONObject.class))).thenReturn(expectedSchema);
        final Schema actualSchema = loader.loadSchema("test-schema");

        assertThat(actualSchema, is(expectedSchema));
    }

    @Test
    public void shouldLogSchemaName() throws Exception {
        loader.loadSchema("test-schema");
        verify(logger).trace("Loading schema {}", "/json/schema/test-schema.json");
    }

    @Test
    public void shouldThrowExceptionIfSchemaNotFound() {

        final SchemaLoadingException schemaLoadingException = assertThrows(SchemaLoadingException.class, () ->
                loader.loadSchema("non-existent")
        );

        assertThat(schemaLoadingException.getMessage(), is("Unable to load JSON schema /json/schema/non-existent.json from classpath"));
    }

    @Test
    public void shouldThrowExceptionIfSchemaMalformed() {
        
        final SchemaLoadingException schemaLoadingException = assertThrows(SchemaLoadingException.class, () ->
                loader.loadSchema("malformed-schema")
        );

        assertThat(schemaLoadingException.getMessage(), is("Unable to load JSON schema /json/schema/malformed-schema.json from classpath"));
    }
}
