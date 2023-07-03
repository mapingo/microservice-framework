package uk.gov.justice.services.generators.commons.validator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.raml;
import static uk.gov.justice.services.generators.test.utils.builder.ResourceBuilder.resource;

import org.junit.jupiter.api.Test;
import org.raml.model.Raml;

public class ContainsResourcesRamlValidatorTest {

    private RamlValidator validator = new ContainsResourcesRamlValidator();

    @Test
    public void shouldThrowExceptionIfNoResourcesInRaml() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                validator.validate(new Raml())
        );

        assertThat(ramlValidationException.getMessage(), is("No resources specified"));
    }

    @Test
    public void shouldPassIfThereIsAResourceDefinedInRaml() {
        validator.validate(raml().with(resource()).build());
    }
}
