package uk.gov.justice.raml.maven.lintchecker.rules;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import uk.gov.justice.raml.maven.lintchecker.LintCheckConfiguration;
import uk.gov.justice.raml.maven.lintchecker.LintCheckerException;
import uk.gov.justice.raml.maven.lintchecker.rules.configuration.TestConfiguration;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ActionsHaveHandlersTest {

    @Mock
    private LintCheckConfiguration lintCheckConfiguration;

    @Mock
    private MavenProject mavenProject;

    @Mock
    private Log log;

    @BeforeEach
    public void setUp() throws DependencyResolutionRequiredException {
        when(lintCheckConfiguration.getMavenProject()).thenReturn(mavenProject);
        when(lintCheckConfiguration.getLog()).thenReturn(log);
        when(mavenProject.getRuntimeClasspathElements()).thenReturn(asList(new String[]{""}));
    }

    @Test
    public void shouldMatchAllActionsToHandlers() throws LintCheckerException, DependencyResolutionRequiredException {
        final ActionsHaveHandlers actionsHaveHandlers = new ActionsHaveHandlers();
        actionsHaveHandlers.execute(TestConfiguration.testConfig().ramlGET(), lintCheckConfiguration);

    }

    @Test
    public void shouldThrowLintCheckerException() throws LintCheckerException {
        final ActionsHaveHandlers actionsHaveHandlers = new ActionsHaveHandlers();

        assertThrows(LintCheckerException.class, () -> actionsHaveHandlers.execute(TestConfiguration.testConfig().ramlGETmissing(), lintCheckConfiguration));
    }

}