package uk.gov.justice.services.adapters.rest.generator;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.raml.model.ActionType.GET;
import static org.raml.model.ActionType.HEAD;
import static org.raml.model.ActionType.OPTIONS;
import static org.raml.model.ActionType.POST;
import static org.raml.model.ActionType.TRACE;
import static uk.gov.justice.services.generators.commons.mapping.ActionMapping.MAPPING_BOUNDARY;
import static uk.gov.justice.services.generators.commons.mapping.ActionMapping.MAPPING_SEPARATOR;
import static uk.gov.justice.services.generators.commons.mapping.ActionMapping.NAME_KEY;
import static uk.gov.justice.services.generators.commons.mapping.ActionMapping.REQUEST_TYPE_KEY;
import static uk.gov.justice.services.generators.test.utils.builder.HttpActionBuilder.defaultGetAction;
import static uk.gov.justice.services.generators.test.utils.builder.HttpActionBuilder.httpAction;
import static uk.gov.justice.services.generators.test.utils.builder.HttpActionBuilder.httpActionWithDefaultMapping;
import static uk.gov.justice.services.generators.test.utils.builder.MappingBuilder.mapping;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.restRamlWithDefaults;
import static uk.gov.justice.services.generators.test.utils.builder.ResourceBuilder.resource;
import static uk.gov.justice.services.generators.test.utils.config.GeneratorConfigUtil.configurationWithBasePackage;

import uk.gov.justice.services.generators.commons.config.CommonGeneratorProperties;
import uk.gov.justice.services.generators.commons.validator.RamlValidationException;

import org.junit.Test;
import org.raml.model.Raml;

public class RestAdapterGenerator_ActionMapperErrorHandlingTest extends BaseRestAdapterGeneratorTest {

    @Test
    public void shouldThrowExceptionIfMappingInDescriptionFieldSyntacticallyIncorrect() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        restRamlWithDefaults()
                                .with(resource("/user")
                                        .with(defaultGetAction()
                                                .withDescription("........ aaa incorrect mapping")
                                        )

                                ).build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("Invalid action mapping in RAML file"));
    }

    @Test
    public void shouldThrowExceptionIfMappingEmpty() throws Exception {
        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        restRamlWithDefaults()
                                .with(resource("/user")
                                        .with(defaultGetAction()
                                                .withDescription("...\n...\n")
                                        )

                                ).build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(),  is("Invalid action mapping in RAML file"));
    }

    @Test
    public void shouldThrowExceptionIfMappingNull() throws Exception {
        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        restRamlWithDefaults()
                                .with(resource("/user")
                                        .with(httpAction()
                                                .withHttpActionType(GET)
                                                .withResponseTypes("application/vnd.ctx.query.defquery+json")
                                        )
                                ).build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("Invalid action mapping in RAML file"));
    }


    @Test
    public void shouldThrowExceptionIfNameNotInMapping() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        restRamlWithDefaults()
                                .with(resource("/user")
                                        .with(defaultGetAction()
                                                .withDescription(MAPPING_BOUNDARY + "\n" +
                                                        MAPPING_SEPARATOR + "\n" +
                                                        REQUEST_TYPE_KEY + ": application/vnd.structure.command.test-cmd+json\n" +
                                                        MAPPING_BOUNDARY + "\n")
                                        )

                                ).build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("Invalid RAML file. Action name not defined in mapping"));
    }

    @Test
    public void shouldThrowExceptionIfNoMediaTypeSetInMapping() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        restRamlWithDefaults()
                                .with(resource("/user")
                                        .with(defaultGetAction()
                                                .withDescription(MAPPING_BOUNDARY + "\n" +
                                                        MAPPING_SEPARATOR + "\n" +
                                                        NAME_KEY + ": nameABC\n" +
                                                        MAPPING_BOUNDARY + "\n")
                                        )

                                ).build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("Invalid RAML file. Media type not defined in mapping"));

    }

    @Test
    public void shouldThrowExceptionIfMediaTypeNotMappedInPOSTHttpAction() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        restRamlWithDefaults()
                                .with(resource("/case")
                                        .with(httpActionWithDefaultMapping(POST)
                                                .with(mapping()
                                                        .withName("contextC.someAction")
                                                        .withRequestType("application/vnd.ctx.command.somemediatype1+json"))
                                                .withMediaTypeWithDefaultSchema("application/vnd.ctx.command.somemediatype1+json")
                                                .withMediaTypeWithDefaultSchema("application/vnd.ctx.command.somemediatype2+json")
                                        )

                                ).build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("Invalid RAML file. Media type(s) not mapped to an action: [application/vnd.ctx.command.somemediatype2+json]"));
    }

    @Test
    public void shouldThrowExceptionIfMediaTypeNotMappedInGETHttpAction() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        restRamlWithDefaults().with(
                                resource("/user")
                                        .with(httpActionWithDefaultMapping(GET)
                                                .with(mapping()
                                                        .withName("contextA.someAction")
                                                        .withResponseType("application/vnd.mediatype1+json"))
                                                .with(mapping()
                                                        .withName("contextA.someOtherAction")
                                                        .withResponseType("application/vnd.mediatype2+json"))
                                                .withResponseTypes(
                                                        "application/vnd.mediatype1+json",
                                                        "application/vnd.mediatype2+json",
                                                        "application/vnd.mediatype3+json"))
                        ).build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("Invalid RAML file. Media type(s) not mapped to an action: [application/vnd.mediatype3+json]"));
    }

    @Test
    public void shouldThrowExceptionIfActionTypeIsHEAD() throws Exception {
        final Raml raml = restRamlWithDefaults()
                .with(resource("/some/path")
                        .with(httpActionWithDefaultMapping(HEAD, "application/vnd.default+json"))
                ).build();

        final IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () ->
                new ActionMappingGenerator().generateFor(raml)
        );

        assertThat(illegalStateException.getMessage(), is("Http Method of type HEAD is not supported by the Action Mapper"));
    }

    @Test
    public void shouldThrowExceptionIfActionTypeIsOPTIONS() throws Exception {

        final Raml raml = restRamlWithDefaults()
                .with(resource("/some/path")
                        .with(httpActionWithDefaultMapping(OPTIONS, "application/vnd.default+json"))
                ).build();

        final IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () ->
                new ActionMappingGenerator().generateFor(raml)
        );

        assertThat(illegalStateException.getMessage(), is("Http Method of type OPTIONS is not supported by the Action Mapper"));
    }

    @Test
    public void shouldThrowExceptionIfActionTypeIsTRACE() throws Exception {

        final Raml raml = restRamlWithDefaults()
                .with(resource("/some/path")
                        .with(httpActionWithDefaultMapping(TRACE, "application/vnd.default+json"))
                ).build();

        final IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () ->
                new ActionMappingGenerator().generateFor(raml)
        );

        assertThat(illegalStateException.getMessage(), is("Http Method of type TRACE is not supported by the Action Mapper"));
    }
}
