package uk.gov.justice.services.generators.commons.validator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.raml.model.ActionType.POST;
import static org.raml.model.ParamType.STRING;
import static uk.gov.justice.services.generators.test.utils.builder.HttpActionBuilder.httpAction;
import static uk.gov.justice.services.generators.test.utils.builder.MimeTypeBuilder.multipartMimeType;
import static uk.gov.justice.services.generators.test.utils.builder.MimeTypeBuilder.multipartWithFileFormParameter;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.restRamlWithDefaults;
import static uk.gov.justice.services.generators.test.utils.builder.ResourceBuilder.resource;

import org.junit.Test;
import org.raml.model.Raml;

public class MultipartHasFormParametersTest {

    private RamlValidator validator = new MultipartHasFormParameters();

    @Test
    public void shouldPassIfMultipartContainsCorrectFormParameter() throws Exception {
        final Raml raml = restRamlWithDefaults()
                .with(resource("/some/path")
                        .with(httpAction()
                                .withHttpActionType(POST)
                                .withMediaTypeWithoutSchema(multipartWithFileFormParameter("photoId")))
                ).build();

        validator.validate(raml);
    }

    @Test
    public void shouldFailIfMultipartHasNoFormParameters() throws Exception {

        final Raml raml = restRamlWithDefaults()
                .with(resource("/some/path")
                        .with(httpAction()
                                .withHttpActionType(POST)
                                .withMediaTypeWithoutSchema(multipartMimeType()))
                ).build();

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                validator.validate(raml)
        );

        assertThat(ramlValidationException.getMessage(), is("Multipart form must contain form parameters"));
    }

    @Test
    public void shouldFailIfMultipartHasFormParameterWithIncorrectType() throws Exception {

        final Raml raml = restRamlWithDefaults()
                .with(resource("/some/path")
                        .with(httpAction()
                                .withHttpActionType(POST)
                                .withMediaTypeWithoutSchema(multipartMimeType()
                                        .withFormParameter("photoId", STRING, true)))
                ).build();

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                validator.validate(raml)
        );

        assertThat(ramlValidationException.getMessage(), is("Multipart form parameter is expected to be of type FILE, instead was STRING"));
    }

    @Test
    public void shouldPassIfNoMultipartPresent() throws Exception {
        final Raml raml = restRamlWithDefaults()
                .with(resource("/some/path")
                        .withDefaultPostAction()
                ).build();

        validator.validate(raml);
    }
}