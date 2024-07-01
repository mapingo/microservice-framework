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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raml.model.Resource;

@ExtendWith(MockitoExtension.class)
public class JmsCommandHandlerDestinationNameProviderCodeGeneratorTest {

    @TempDir
    public File temporaryFolder;

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

        final File outputDirectory = new File(temporaryFolder, "test-generation");
        outputDirectory.mkdirs();
        builder(packageName, typeSpec)
                .build()
                .writeTo(outputDirectory);

        File compilationOutputDir = new File(temporaryFolder, getClass().getSimpleName());
        compilationOutputDir.mkdirs();
        final Class<?> compiledClass = javaCompilerUtil().compiledClassOf(
                outputDirectory,
                compilationOutputDir,
                packageName,
                simpleName);

        final Object newInstance = compiledClass.newInstance();

        assertThat(newInstance instanceof JmsCommandHandlerDestinationNameProvider, is(true));

        final JmsCommandHandlerDestinationNameProvider jmsCommandHandlerDestinationNameProvider = (JmsCommandHandlerDestinationNameProvider) newInstance;

        assertThat(jmsCommandHandlerDestinationNameProvider.destinationName(), is(destination));
    }
}