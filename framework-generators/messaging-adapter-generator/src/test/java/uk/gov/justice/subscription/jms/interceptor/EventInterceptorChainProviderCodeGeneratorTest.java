package uk.gov.justice.subscription.jms.interceptor;

import static com.squareup.javapoet.JavaFile.builder;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.test.utils.core.compiler.JavaCompilerUtility.javaCompilerUtil;
import static uk.gov.justice.subscription.jms.core.ClassNameFactory.EVENT_FILTER_INTERCEPTOR;
import static uk.gov.justice.subscription.jms.core.ClassNameFactory.EVENT_INTERCEPTOR_CHAIN_PROVIDER;

import uk.gov.justice.services.core.interceptor.InterceptorChainEntry;
import uk.gov.justice.services.core.interceptor.InterceptorChainEntryProvider;
import uk.gov.justice.subscription.jms.core.ClassNameFactory;

import java.io.File;
import java.util.List;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EventInterceptorChainProviderCodeGeneratorTest {

    @TempDir
    public File temporaryFolder;

    @InjectMocks
    private EventInterceptorChainProviderCodeGenerator eventInterceptorChainProviderCodeGenerator;

    @Test
    public void shouldGenerateAWorkingEventListenerInterceptorChainProviderWithTheCorrectInterceptorChainEntiresAndComponentName() throws Exception {

        final String componentName = "MY_CUSTOM_EVENT_LISTENER";

        final String packageName = "uk.gov.justice.api.interceptor.filter";
        final String simpleName = "MyCustomEventListenerInterceptorChainProvider";

        final ClassName eventListenerInterceptorChainProviderClassName = ClassName.get(packageName, simpleName);
        final ClassName eventFilterInterceptorClassName = ClassName.get(StubEventFilterInterceptor.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);

        when(classNameFactory.classNameFor(EVENT_INTERCEPTOR_CHAIN_PROVIDER)).thenReturn(eventListenerInterceptorChainProviderClassName);
        when(classNameFactory.classNameFor(EVENT_FILTER_INTERCEPTOR)).thenReturn(eventFilterInterceptorClassName);

        final TypeSpec typeSpec = eventInterceptorChainProviderCodeGenerator.generate(
                componentName,
                classNameFactory);

        final File codeGenerationOutputDirectory = new File(temporaryFolder, "test-generation");
        final File compilationOutputDirectory = new File(temporaryFolder, "interceptorChainProvider-generation");
        codeGenerationOutputDirectory.mkdirs();
        compilationOutputDirectory.mkdirs();

        builder(packageName, typeSpec)
                .build()
                .writeTo(codeGenerationOutputDirectory);

        final Class<?> compiledClass = javaCompilerUtil().compiledClassOf(
                codeGenerationOutputDirectory,
                compilationOutputDirectory,
                packageName,
                simpleName);

        final InterceptorChainEntryProvider interceptorChainEntryProvider = (InterceptorChainEntryProvider) compiledClass.newInstance();

        assertThat(interceptorChainEntryProvider.component(), is(componentName));

        final List<InterceptorChainEntry> interceptorChainEntries = interceptorChainEntryProvider.interceptorChainTypes();
        assertThat(interceptorChainEntries, hasItem(new InterceptorChainEntry(2000, StubEventFilterInterceptor.class)));
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "SameParameterValue"})
    private File getDirectory(final File aTemporaryDirectory) {

        if (aTemporaryDirectory.exists()) {
            aTemporaryDirectory.delete();
        }

        aTemporaryDirectory.mkdirs();

        return aTemporaryDirectory;
    }
}
