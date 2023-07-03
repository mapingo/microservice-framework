package uk.gov.justice.services.generators.commons.validator;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.justice.services.generators.test.utils.builder.HttpActionBuilder.httpAction;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.raml;
import static uk.gov.justice.services.generators.test.utils.builder.ResourceBuilder.resource;

import org.junit.jupiter.api.Test;

public class ContainsActionsRamlValidatorTest {

    private RamlValidator validator = new ContainsActionsRamlValidator();

    @Test
    public void shouldPassIfThereIsAnActionInRaml() {
        validator.validate(raml().with(resource().with(httpAction())).build());
    }

    @Test
    public void shouldThrowExceptionIfNoActionsInRaml() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                validator.validate(raml().with(resource()).build())

        );

        assertThat(ramlValidationException.getMessage(), is("No actions to process"));
    }
}
