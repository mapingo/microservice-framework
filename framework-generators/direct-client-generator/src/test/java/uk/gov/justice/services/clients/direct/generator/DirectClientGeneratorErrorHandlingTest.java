package uk.gov.justice.services.clients.direct.generator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.raml.model.ActionType.GET;
import static org.raml.model.ActionType.HEAD;
import static uk.gov.justice.services.generators.test.utils.builder.HttpActionBuilder.defaultGetAction;
import static uk.gov.justice.services.generators.test.utils.builder.HttpActionBuilder.httpAction;
import static uk.gov.justice.services.generators.test.utils.builder.HttpActionBuilder.httpActionWithDefaultMapping;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.raml;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.restRamlWithDefaults;
import static uk.gov.justice.services.generators.test.utils.builder.ResourceBuilder.resource;
import static uk.gov.justice.services.generators.test.utils.config.GeneratorConfigUtil.configurationWithBasePackage;

import uk.gov.justice.services.generators.commons.config.CommonGeneratorProperties;
import uk.gov.justice.services.generators.commons.validator.RamlValidationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DirectClientGeneratorErrorHandlingTest {

    private static final String BASE_PACKAGE = "org.raml.test";

    @Rule
    public TemporaryFolder outputFolder = new TemporaryFolder();

    private final DirectClientGenerator generator = new DirectClientGenerator();

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
    public void shouldThrowExceptionIfNoResourcesInRaml() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        raml().build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("No resources specified"));
    }

    @Test
    public void shouldThrowExceptionIfNoActionsInRaml() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        raml()
                                .with(resource("/path"))
                                .build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("No actions to process"));
    }

    @Test
    public void shouldThrowExceptionIfResponseTypeNotSetForGETAction() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        restRamlWithDefaults()
                                .with(resource("/path")
                                        .with(httpActionWithDefaultMapping(GET))
                                ).build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("Response type not set"));
    }

    @Test
    public void shouldThrowExceptionForPOST() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        restRamlWithDefaults().withDefaultPostResource().build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("Http action type not supported: POST"));
    }

    @Test
    public void shouldThrowExceptionIfActionTypeIsHEAD() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
        generator.run(
                restRamlWithDefaults()
                        .with(resource("/some/path")
                                .with(httpAction().withHttpActionType(HEAD))
                        ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("Http action type not supported: HEAD"));
    }

}
