package uk.gov.justice.services.generators.commons.validator;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.raml.model.ActionType.DELETE;
import static org.raml.model.ActionType.GET;
import static org.raml.model.ActionType.PATCH;
import static org.raml.model.ActionType.POST;
import static org.raml.model.ActionType.PUT;
import static uk.gov.justice.services.generators.test.utils.builder.HttpActionBuilder.httpActionWithDefaultMapping;
import static uk.gov.justice.services.generators.test.utils.builder.MappingBuilder.mapping;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.restRamlWithDefaults;
import static uk.gov.justice.services.generators.test.utils.builder.ResourceBuilder.resource;

import org.junit.jupiter.api.Test;

public class ActionMappingRamlValidatorTest {

    private RamlValidator validator = new ActionMappingRamlValidator();

    @Test
    public void shouldValidateIfThereAreNoActions() throws Exception {
        validator.validate(restRamlWithDefaults().with(resource("/case")).build());
    }

    @Test
    public void shouldThrowExceptionIfMediaTypeOfPOSTRequestNotInMapping() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                validator.validate(
                        restRamlWithDefaults()
                                .with(resource("/case")
                                        .with(httpActionWithDefaultMapping(POST)
                                                .with(mapping()
                                                        .withName("context.someAction")
                                                        .withRequestType("application/vnd.somemediatype1+json"))
                                                .withMediaTypeWithDefaultSchema("application/vnd.somemediatype1+json")
                                                .withMediaTypeWithDefaultSchema("application/vnd.somemediatype2+json")
                                        )

                                ).build())
        );

        assertThat(ramlValidationException.getMessage(), is("Invalid RAML file. Media type(s) not mapped to an action: [application/vnd.somemediatype2+json]"));
    }

    @Test
    public void shouldThrowExceptionIfMediaTypeOfPUTRequestNotInMapping() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                validator.validate(
                        restRamlWithDefaults()
                                .with(resource("/case")
                                        .with(httpActionWithDefaultMapping(PUT)
                                                .with(mapping()
                                                        .withName("context.someAction")
                                                        .withRequestType("application/vnd.somemediatype1+json"))
                                                .withMediaTypeWithDefaultSchema("application/vnd.somemediatype1+json")
                                                .withMediaTypeWithDefaultSchema("application/vnd.somemediatype2+json")
                                        )

                                ).build())
        );

        assertThat(ramlValidationException.getMessage(), is("Invalid RAML file. Media type(s) not mapped to an action: [application/vnd.somemediatype2+json]"));
    }

    @Test
    public void shouldThrowExceptionIfMediaTypeOfPATCHRequestNotInMapping() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                validator.validate(
                        restRamlWithDefaults()
                                .with(resource("/case")
                                        .with(httpActionWithDefaultMapping(PATCH)
                                                .with(mapping()
                                                        .withName("context.someAction")
                                                        .withRequestType("application/vnd.somemediatype1+json"))
                                                .withMediaTypeWithDefaultSchema("application/vnd.somemediatype1+json")
                                                .withMediaTypeWithDefaultSchema("application/vnd.somemediatype2+json")
                                        )

                                ).build())
        );

        assertThat(ramlValidationException.getMessage(), is("Invalid RAML file. Media type(s) not mapped to an action: [application/vnd.somemediatype2+json]"));
    }

    @Test
    public void shouldThrowExceptionIfMediaTypeOfDELETERequestNotInMapping() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                validator.validate(
                        restRamlWithDefaults()
                                .with(resource("/case")
                                        .with(httpActionWithDefaultMapping(DELETE)
                                                .with(mapping()
                                                        .withName("context.someAction")
                                                        .withRequestType("application/vnd.somemediatype1+json"))
                                                .withMediaTypeWithDefaultSchema("application/vnd.somemediatype1+json")
                                                .withMediaTypeWithDefaultSchema("application/vnd.somemediatype2+json")
                                        )

                                ).build())
        );
        assertThat(ramlValidationException.getMessage(), is("Invalid RAML file. Media type(s) not mapped to an action: [application/vnd.somemediatype2+json]"));
    }

    @Test
    public void shouldThrowExceptionIfMediaTypeOfGETRequestNotInMapping() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                validator.validate(
                        restRamlWithDefaults()
                                .with(resource("/case")
                                        .with(httpActionWithDefaultMapping(GET)
                                                .with(mapping()
                                                        .withName("context.someAction")
                                                        .withResponseType("application/vnd.somemediatype2+json"))
                                                .withResponseTypes(
                                                        "application/vnd.somemediatype1+json",
                                                        "application/vnd.somemediatype2+json"))
                                ).build())
        );
        assertThat(ramlValidationException.getMessage(), is("Invalid RAML file. Media type(s) not mapped to an action: [application/vnd.somemediatype1+json]"));
    }
}