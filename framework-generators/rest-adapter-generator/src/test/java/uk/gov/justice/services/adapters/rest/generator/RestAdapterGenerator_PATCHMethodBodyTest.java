package uk.gov.justice.services.adapters.rest.generator;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.raml.model.ActionType.PATCH;
import static uk.gov.justice.services.core.interceptor.InterceptorContext.interceptorContextWithInput;
import static uk.gov.justice.services.generators.test.utils.builder.HttpActionBuilder.httpActionWithDefaultMapping;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.restRamlWithCommandApiDefaults;
import static uk.gov.justice.services.generators.test.utils.builder.ResourceBuilder.resource;
import static uk.gov.justice.services.generators.test.utils.config.GeneratorConfigUtil.configurationWithBasePackage;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.firstMethodOf;

import uk.gov.justice.services.core.interceptor.InterceptorContext;
import uk.gov.justice.services.generators.commons.config.CommonGeneratorProperties;
import uk.gov.justice.services.messaging.JsonEnvelope;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class RestAdapterGenerator_PATCHMethodBodyTest extends BaseRestAdapterGeneratorTest {

    private static final JsonObject NOT_USED_JSONOBJECT = Json.createObjectBuilder().build();

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnResponseGeneratedByRestProcessor() throws Exception {
        generator.run(
                restRamlWithCommandApiDefaults()
                        .with(resource("/path")
                                .with(httpActionWithDefaultMapping(PATCH).withHttpActionOfDefaultRequestType())
                        ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder.getRoot(),
                outputFolder.getRoot(),
                BASE_PACKAGE,
                "resource",
                "DefaultCommandApiPathResource");

        final Object resourceObject = getInstanceOf(resourceClass);

        final Response processorResponse = Response.ok().build();
        when(restProcessor.process(anyString(), any(Function.class), anyString(), any(Optional.class), any(HttpHeaders.class),
                any(Collection.class))).thenReturn(processorResponse);

        final Method method = firstMethodOf(resourceClass).get();

        final Object result = method.invoke(resourceObject, NOT_USED_JSONOBJECT);

        assertThat(result, is(processorResponse));
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void shouldCallInterceptorChainProcessor() throws Exception {

        generator.run(
                restRamlWithCommandApiDefaults()
                        .with(
                                resource("/path")
                                        .with(httpActionWithDefaultMapping(PATCH).withHttpActionOfDefaultRequestType())
                        ).build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, new CommonGeneratorProperties()));

        final Class<?> resourceClass = COMPILER.compiledClassOf(
                outputFolder.getRoot(),
                outputFolder.getRoot(),
                BASE_PACKAGE,
                "resource",
                "DefaultCommandApiPathResource");

        final Object resourceObject = getInstanceOf(resourceClass);

        final Method method = firstMethodOf(resourceClass).get();

        method.invoke(resourceObject, NOT_USED_JSONOBJECT);

        final ArgumentCaptor<Function> functionCaptor = ArgumentCaptor.forClass(Function.class);
        verify(restProcessor).process(anyString(), functionCaptor.capture(), anyString(), any(Optional.class), any(HttpHeaders.class),
                any(Collection.class));

        final JsonEnvelope envelope = mock(JsonEnvelope.class);
        final InterceptorContext interceptorContext = interceptorContextWithInput(envelope);
        functionCaptor.getValue().apply(interceptorContext);

        verify(interceptorChainProcessor).process(interceptorContext);
    }
}