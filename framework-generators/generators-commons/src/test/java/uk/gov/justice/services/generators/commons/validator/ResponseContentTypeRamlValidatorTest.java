package uk.gov.justice.services.generators.commons.validator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.raml.model.ActionType.GET;
import static org.raml.model.ActionType.POST;
import static uk.gov.justice.services.generators.test.utils.builder.HttpActionBuilder.httpActionWithDefaultMapping;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.raml;
import static uk.gov.justice.services.generators.test.utils.builder.ResourceBuilder.resource;

import org.junit.Test;

public class ResponseContentTypeRamlValidatorTest {

    private RamlValidator validator = new ResponseContentTypeRamlValidator(GET);

    @Test
    public void shouldPassIfResponseContentTypeContainsAVendorSpecificJsonMediaType() throws Exception {

        validator.validate(
                raml().with(
                        resource("/some/path")
                                .with(httpActionWithDefaultMapping(GET).withResponseTypes("application/vnd.user+json"))
                ).build());
    }

    @Test
    public void shouldIgnoreInvalidResponseContentTypeInNonGETActions() throws Exception {

        validator.validate(
                raml()
                        .with(resource("/some/path")
                                .with(httpActionWithDefaultMapping(GET).withResponseTypes("application/xml")))
                        .with(resource("/some/path")
                                .with(httpActionWithDefaultMapping(POST).withResponseTypes("application/json")))
                        .build());
    }

    @Test
    public void shouldThrowExceptionIfResponseContentTypeNotSet() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                validator.validate(
                        raml().with(
                                resource("/some/path")
                                        .with(httpActionWithDefaultMapping(GET))
                        ).build())
        );

        assertThat(ramlValidationException.getMessage(), is("Response type not set"));
    }
}
