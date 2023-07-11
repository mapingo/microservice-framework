package uk.gov.justice.raml.jms.validator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.raml;

import uk.gov.justice.services.generators.commons.validator.RamlValidationException;
import uk.gov.justice.services.generators.commons.validator.RamlValidator;

import org.junit.jupiter.api.Test;

public class BaseUriRamlValidatorTest {

    private RamlValidator validator = new BaseUriRamlValidator();

    @Test
    public void shouldThrowExceptionIfBaseUriNotSetForEventListener() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                validator.validate(raml().withBaseUri(null).build())
        );

        assertThat(ramlValidationException.getMessage(), is("Base uri not set"));
    }

    @Test
    public void shouldThrowExceptionIfBaseUriNotValid() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                validator.validate(raml().withBaseUri("message://INVALID/handler/message/service1").build())
        );

        assertThat(ramlValidationException.getMessage(), is("Invalid base uri: message://INVALID/handler/message/service1"));
    }

    @Test
    public void shouldPassWhenCorrectBaseUri() throws Exception {

        validator.validate(raml().withBaseUri("message://command/handler/message/service1").build());
        validator.validate(raml().withBaseUri("message://event/listener/message/people").build());
    }
}
