package uk.gov.justice.raml.maven.lintchecker.rules.it;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.maven.test.utils.BetterAbstractMojoTestCase;
import uk.gov.justice.raml.maven.lintchecker.rules.LintCheckMojo;

import java.io.File;
import java.net.URL;

import org.apache.maven.plugin.MojoExecutionException;

public class ActionsHaveHandlersMojoTest extends BetterAbstractMojoTestCase {

    public void testShouldFailTest() throws Exception {

        final URL pomFileUrl = getClass().getClassLoader().getResource("it/pom.xml");
        assertThat(pomFileUrl, is(notNullValue()));
        final File pom = new File(pomFileUrl.toURI());
        assertNotNull( pom );
        assertTrue( pom.exists() );

        final LintCheckMojo myMojo = (LintCheckMojo) lookupConfiguredMojo(pom, "raml-lint-check");

        assertNotNull( myMojo );

        try {
            myMojo.execute();
            fail();
        } catch (MojoExecutionException e) {
            assertThat(e.getCause().toString(), containsString("without matching actions in raml"));
        }
    }
}
