package uk.gov.justice.services.adapters.rest.generator;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.raml.model.ActionType.GET;
import static org.raml.model.ActionType.PATCH;
import static org.raml.model.ActionType.POST;
import static uk.gov.justice.services.generators.test.utils.builder.HttpActionBuilder.httpActionWithDefaultMapping;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.raml;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.restRamlWithDefaults;
import static uk.gov.justice.services.generators.test.utils.builder.ResourceBuilder.resource;
import static uk.gov.justice.services.generators.test.utils.config.GeneratorConfigUtil.configurationWithBasePackage;

import uk.gov.justice.services.generators.commons.config.CommonGeneratorProperties;
import uk.gov.justice.services.generators.commons.validator.RamlValidationException;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class RestAdapterGeneratorErrorHandlingTest {
    private static final String BASE_PACKAGE = "uk.test";

    @TempDir
    public File outputFolder;

    private RestAdapterGenerator generator = new RestAdapterGenerator();

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
    public void shouldThrowExceptionIfRequestTypeNotSetForPOSTAction() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        restRamlWithDefaults().with(
                                resource("/path")
                                        .with(httpActionWithDefaultMapping(POST))
                        ).build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("Request type not set"));
    }

    @Test
    public void shouldThrowExceptionIfResponseTypeNotSetForGETAction() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        restRamlWithDefaults().with(
                                resource("/path")
                                        .with(httpActionWithDefaultMapping(GET))
                        ).build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("Response type not set"));
    }

    @Test
    public void shouldThrowExceptionIfRequestTypeNotSetForPATCHAction() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        restRamlWithDefaults().with(
                                resource("/path")
                                        .with(httpActionWithDefaultMapping(PATCH))
                        ).build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("Request type not set"));
    }

    @Test
    public void shouldThrowExceptionIfRequestTypeNotSetForPUTAction() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        restRamlWithDefaults().with(
                                resource("/path")
                                        .with(httpActionWithDefaultMapping(PATCH))
                        ).build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("Request type not set"));
    }

    @Test
    public void shouldThrowExceptionIfRequestTypeNotSetForDELETEAction() throws Exception {

        final RamlValidationException ramlValidationException = assertThrows(RamlValidationException.class, () ->
                generator.run(
                        restRamlWithDefaults().with(
                                resource("/path")
                                        .with(httpActionWithDefaultMapping(PATCH))
                        ).build(),
                        configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()))
        );

        assertThat(ramlValidationException.getMessage(), is("Request type not set"));
    }
}
