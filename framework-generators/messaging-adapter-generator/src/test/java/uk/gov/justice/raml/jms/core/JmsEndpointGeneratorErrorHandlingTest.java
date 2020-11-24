package uk.gov.justice.raml.jms.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.raml.model.ActionType.POST;
import static uk.gov.justice.services.core.annotation.Component.QUERY_API;
import static uk.gov.justice.services.generators.test.utils.builder.HttpActionBuilder.httpAction;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.raml;
import static uk.gov.justice.services.generators.test.utils.builder.ResourceBuilder.resource;
import static uk.gov.justice.services.generators.test.utils.config.GeneratorConfigUtil.configurationWithBasePackage;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.raml.jms.config.GeneratorPropertiesFactory;
import uk.gov.justice.services.generators.commons.config.CommonGeneratorProperties;
import uk.gov.justice.services.generators.commons.validator.RamlValidationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.raml.model.Raml;

public class JmsEndpointGeneratorErrorHandlingTest {

    private static final String BASE_PACKAGE = "org.raml.test";

    @Rule
    public TemporaryFolder outputFolder = new TemporaryFolder();

    private Generator<Raml> generator = new JmsEndpointGenerator();

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
                                .with(resource()
                                        .withRelativeUri("/structure.controller.command"))
                                .build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("No actions to process"));
    }

    @Test
    public void shouldThrowExceptionIfMediaTypeNotSet() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        raml()
                                .with(resource()
                                        .with(httpAction().withHttpActionType(POST)))
                                .build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("Request type not set"));
    }

    @Test
    public void shouldThrowExceptionWhenBaseUriNotSetWhileGeneratingEventListener() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        raml()
                                .withBaseUri(null)
                                .with(resource()
                                        .withRelativeUri("/structure.event")
                                        .withDefaultPostAction())
                                .build(),
                        configurationWithBasePackage("uk.somepackage", outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("Base uri not set"));
    }

    @Test
    public void shouldThrowExceptionWhenInvalidBaseUriWhileGeneratingEventListener() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        raml()
                                .withBaseUri("message://too/short/uri")
                                .with(resource()
                                        .withRelativeUri("/structure.event")
                                        .withDefaultPostAction())
                                .build(),
                        configurationWithBasePackage("uk.somepackage", outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("Invalid base uri: message://too/short/uri"));
    }

    @Test
    public void shouldThrowExceptionWhenUnsupportedFrameworkComponent() throws Exception {

        final IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () ->
                generator.run(
                        raml()
                                .withBaseUri("message://query/api/message/people")
                                .with(resource()
                                        .withRelativeUri("/structure.event")
                                        .withDefaultPostAction())
                                .build(),
                        configurationWithBasePackage("uk.somepackage", outputFolder, new GeneratorPropertiesFactory().withServiceComponentOf(QUERY_API)))
        );

        assertThat(illegalStateException.getMessage(), is("JMS Endpoint generation is unsupported for framework component type QUERY_API"));
    }
}
