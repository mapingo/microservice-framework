package uk.gov.justice.services.clients.direct.generator;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.config.GeneratorPropertiesFactory.generatorProperties;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.raml;
import static uk.gov.justice.services.generators.test.utils.builder.ResourceBuilder.defaultGetResource;
import static uk.gov.justice.services.generators.test.utils.config.GeneratorConfigUtil.configurationWithBasePackage;
import static uk.gov.justice.services.test.utils.core.compiler.JavaCompilerUtility.javaCompilerUtil;
import static uk.gov.justice.services.test.utils.core.messaging.JsonEnvelopeBuilder.envelope;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.firstMethodOf;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import uk.gov.justice.services.adapter.direct.SynchronousDirectAdapter;
import uk.gov.justice.services.adapter.direct.SynchronousDirectAdapterCache;
import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.messaging.logging.DefaultTraceLogger;
import uk.gov.justice.services.test.utils.core.compiler.JavaCompilerUtility;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DirectClientGeneratorMethodBodyTest {

    private static final String BASE_PACKAGE = "org.raml.test";
    private static final JavaCompilerUtility COMPILER = javaCompilerUtil();

    @TempDir
    public File outputFolder;

    @Mock
    private SynchronousDirectAdapter adapter;

    @Mock
    private SynchronousDirectAdapterCache adapterCache;

    private final DirectClientGenerator generator = new DirectClientGenerator();

    @Test
    public void shouldPassEnvelopeToAdapter() throws Exception {

        generator.run(
                raml()
                        .withBaseUri("http://localhost:8080/warname/query/view/service")
                        .with(defaultGetResource())
                        .build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, generatorProperties().withServiceComponentOf("QUERY_API")));

        final Class<?> generatedClientClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "DirectQueryApi2QueryViewServiceClient");

        final JsonEnvelope envelopePassedToClient = envelope().build();

        when(adapterCache.directAdapterForComponent("QUERY_VIEW")).thenReturn(adapter);

        invokeFirstMethod(generatedClientClass, envelopePassedToClient);


        verify(adapter).process(envelopePassedToClient);
    }

    @Test
    public void shouldReturnEnvelopeReturnedByAdapter() throws Exception {

        generator.run(
                raml()
                        .withBaseUri("http://localhost:8080/warname/query/view/service")
                        .with(defaultGetResource())
                        .build(),
                configurationWithBasePackage(BASE_PACKAGE, outputFolder, generatorProperties().withServiceComponentOf("QUERY_API")));

        final Class<?> generatedClientClass = COMPILER.compiledClassOf(
                outputFolder,
                outputFolder,
                BASE_PACKAGE,
                "DirectQueryApi2QueryViewServiceClient");

        final JsonEnvelope envelopeReturnedByAdapter = envelope().build();
        when(adapterCache.directAdapterForComponent("QUERY_VIEW")).thenReturn(adapter);
        when(adapter.process(any(JsonEnvelope.class))).thenReturn(envelopeReturnedByAdapter);

        final Object result = invokeFirstMethod(generatedClientClass, envelope().build());
        assertThat(result, is(envelopeReturnedByAdapter));
    }

    private Object invokeFirstMethod(Class<?> generatedClass, JsonEnvelope envelope) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        final Object directClient = instanceOf(generatedClass);

        final Method method = firstMethodOf(generatedClass).get();
        return method.invoke(directClient, envelope);
    }

    private Object instanceOf(final Class<?> directClientClass) throws InstantiationException, IllegalAccessException {
        final Object resourceObject = directClientClass.newInstance();
        setField(resourceObject, "adapterCache", adapterCache);
        setField(resourceObject, "traceLogger", new DefaultTraceLogger());
        return resourceObject;
    }

}
