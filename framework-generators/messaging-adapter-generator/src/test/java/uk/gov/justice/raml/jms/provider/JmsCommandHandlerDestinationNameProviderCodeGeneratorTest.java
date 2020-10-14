package uk.gov.justice.raml.jms.provider;

import static com.squareup.javapoet.ClassName.get;
import static com.squareup.javapoet.JavaFile.builder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.raml.jms.core.ClassNameFactory.JMS_HANDLER_DESTINATION_NAME_PROVIDER;
import static uk.gov.justice.services.generators.test.utils.builder.ResourceBuilder.resource;
import static uk.gov.justice.services.test.utils.core.compiler.JavaCompilerUtility.javaCompilerUtil;

import uk.gov.justice.raml.jms.core.ClassNameFactory;
import uk.gov.justice.services.messaging.jms.JmsCommandHandlerDestinationNameProvider;

import java.io.File;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.raml.model.Resource;

@RunWith(MockitoJUnitRunner.class)
public class JmsCommandHandlerDestinationNameProviderCodeGeneratorTest {

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    @InjectMocks
    private JmsCommandHandlerDestinationNameProviderCodeGenerator jmsCommandHandlerDestinationNameProviderCodeGenerator;

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldCreateJmsCommandHandlerDestinationNameProvider() throws Exception {

        final String packageName = "uk.gov.justice.api.interceptor.provider";
        final String simpleName = "MyJmsCommandHandlerDestinationNameProvider";
        final String destination = "structure.controller.command";

        final ClassName jmsCommandHandlerDestinationNameProviderClassName = get(packageName, simpleName);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);

        when(classNameFactory.classNameFor(JMS_HANDLER_DESTINATION_NAME_PROVIDER)).thenReturn(jmsCommandHandlerDestinationNameProviderClassName);

        final Resource resource = resource().withRelativeUri("/" + destination).build();
        final TypeSpec typeSpec = jmsCommandHandlerDestinationNameProviderCodeGenerator.generate(resource, classNameFactory);

        final File outputDirectory = temporaryFolder.newFolder("test-generation");
        builder(packageName, typeSpec)
                .build()
                .writeTo(outputDirectory);

        final Class<?> compiledClass = javaCompilerUtil().compiledClassOf(
                outputDirectory,
                temporaryFolder.newFolder(getClass().getSimpleName()),
                packageName,
                simpleName);

        final Object newInstance = compiledClass.newInstance();

        assertThat(newInstance instanceof JmsCommandHandlerDestinationNameProvider, is(true));

        final JmsCommandHandlerDestinationNameProvider jmsCommandHandlerDestinationNameProvider = (JmsCommandHandlerDestinationNameProvider) newInstance;

        assertThat(jmsCommandHandlerDestinationNameProvider.destinationName(), is(destination));
    }
}