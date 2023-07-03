package uk.gov.justice.services.generators.commons.validator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.raml.model.ActionType.GET;
import static org.raml.model.ActionType.POST;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.raml;
import static uk.gov.justice.services.generators.test.utils.builder.ResourceBuilder.resource;

import org.junit.jupiter.api.Test;

public class SupportedActionTypesRamlValidatorTest {

    @Test
    public void shouldNotPassIfActionTypeSupported() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                new SupportedActionTypesRamlValidator(GET)
                        .validate(raml().with(resource().withDefaultPostAction()).build())
        );
        assertThat(ramlValidationException.getMessage(), is("Http action type not supported: POST"));
    }

    @Test
    public void shouldPassIfActionTypeSuported() throws Exception {

        new SupportedActionTypesRamlValidator(GET, POST)
                .validate(raml().with(resource().withDefaultPostAction()).build());
    }
}