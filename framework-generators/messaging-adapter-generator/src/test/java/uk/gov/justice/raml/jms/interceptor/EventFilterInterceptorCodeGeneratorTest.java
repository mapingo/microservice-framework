package uk.gov.justice.raml.jms.interceptor;

import static com.squareup.javapoet.ClassName.get;
import static com.squareup.javapoet.JavaFile.builder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.raml.jms.core.ClassNameFactory.EVENT_FILTER;
import static uk.gov.justice.raml.jms.core.ClassNameFactory.EVENT_FILTER_INTERCEPTOR;
import static uk.gov.justice.services.test.utils.core.compiler.JavaCompilerUtility.javaCompilerUtil;

import org.junit.jupiter.api.io.TempDir;
import uk.gov.justice.raml.jms.core.ClassNameFactory;
import uk.gov.justice.services.core.interceptor.Interceptor;
import uk.gov.justice.services.core.interceptor.InterceptorChain;
import uk.gov.justice.services.core.interceptor.InterceptorContext;
import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.messaging.Metadata;

import java.io.File;
import java.lang.reflect.Field;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EventFilterInterceptorCodeGeneratorTest {

    @TempDir
    public File temporaryFolder;

    @InjectMocks
    private EventFilterInterceptorCodeGenerator eventFilterInterceptorCodeGenerator;

    @Test
    public void shouldGenerateAWorkingEventFilterInterceptorThatUsesACustomEventFilter() throws Exception {

        final String packageName = "uk.gov.justice.api.interceptor.filter";
        final String simpleName = "MyCustomEventFilterInterceptor";

        final ClassName eventFilterInterceptorClassName = get(packageName, simpleName);
        final ClassName eventFilterClassName = get(MyCustomEventFilter.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);

        when(classNameFactory.classNameFor(EVENT_FILTER_INTERCEPTOR)).thenReturn(eventFilterInterceptorClassName);
        when(classNameFactory.classNameFor(EVENT_FILTER)).thenReturn(eventFilterClassName);

        final TypeSpec typeSpec = eventFilterInterceptorCodeGenerator.generate(
                classNameFactory);

        final File outputDirectory = new File(temporaryFolder, "test-generation");
        builder(packageName, typeSpec)
                .build()
                .writeTo(outputDirectory);

        File dir = new File(temporaryFolder, getClass().getSimpleName());
        dir.mkdirs();
        final Class<?> compiledClass = javaCompilerUtil().compiledClassOf(
                outputDirectory,
                dir,
                packageName,
                simpleName);

        nowTestTheGeneratedClass(compiledClass);
        nowTestTheFailureCase(compiledClass);
    }

    private void nowTestTheGeneratedClass(final Class<?> generatedClass) throws Exception {

        final String eventName = "an.event.name";

        final Interceptor interceptor = buildTheClassForTest(generatedClass, new MyCustomEventFilter(eventName));

        final InterceptorContext interceptorContext_1 = mock(InterceptorContext.class, "interceptorContext_1");
        final InterceptorContext interceptorContext_2 = mock(InterceptorContext.class, "interceptorContext_2");

        final InterceptorChain interceptorChain = mock(InterceptorChain.class);
        final JsonEnvelope jsonEnvelope = mock(JsonEnvelope.class);
        final Metadata metadata = mock(Metadata.class);


        when(interceptorContext_1.inputEnvelope()).thenReturn(jsonEnvelope);
        when(jsonEnvelope.metadata()).thenReturn(metadata);
        when(metadata.name()).thenReturn(eventName);
        when(interceptorChain.processNext(interceptorContext_1)).thenReturn(interceptorContext_2);

        assertThat(interceptor.process(interceptorContext_1, interceptorChain), is(interceptorContext_2));
    }

    private void nowTestTheFailureCase(final Class<?> generatedClass) throws Exception {

        final String eventName = "an.event.name";
        final String aDifferentEventName = "a.different.event.name";

        final Interceptor interceptor = buildTheClassForTest(generatedClass, new MyCustomEventFilter(eventName));

        final InterceptorContext interceptorContext_1 = mock(InterceptorContext.class, "interceptorContext_1");

        final InterceptorChain interceptorChain = mock(InterceptorChain.class);
        final JsonEnvelope jsonEnvelope = mock(JsonEnvelope.class);
        final Metadata metadata = mock(Metadata.class);


        when(interceptorContext_1.inputEnvelope()).thenReturn(jsonEnvelope);
        when(jsonEnvelope.metadata()).thenReturn(metadata);
        when(metadata.name()).thenReturn(aDifferentEventName);

        assertThat(interceptor.process(interceptorContext_1, interceptorChain), is(interceptorContext_1));
    }

    private Interceptor buildTheClassForTest(final Class<?> generatedClass, final MyCustomEventFilter myCustomEventFilter) throws Exception {
        final Object myCustomEventFilterInterceptor = generatedClass.newInstance();

        final Field eventFilterField = generatedClass.getDeclaredField("eventFilter");
        eventFilterField.setAccessible(true);

        eventFilterField.set(myCustomEventFilterInterceptor, myCustomEventFilter);

        return (Interceptor) myCustomEventFilterInterceptor;
    }
}
