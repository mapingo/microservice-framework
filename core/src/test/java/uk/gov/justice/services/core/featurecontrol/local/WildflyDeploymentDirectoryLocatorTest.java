package uk.gov.justice.services.core.featurecontrol.local;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static uk.gov.justice.services.core.featurecontrol.local.WildflyDeploymentDirectoryLocator.JBOSS_SERVER_BASE_DIR;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WildflyDeploymentDirectoryLocatorTest {

    private static final String SOME_PATH_TO_WILDFLY = "/some/path/to/wildfly";

    @AfterClass
    public static void removeJbossBaseDirSystemProperty() {
        System.clearProperty(JBOSS_SERVER_BASE_DIR);
        assertThat(System.getProperty(JBOSS_SERVER_BASE_DIR), is(nullValue()));
    }

    @InjectMocks
    private WildflyDeploymentDirectoryLocator wildflyDeploymentDirectoryLocator;

    @Test
    public void shouldLookupTheWildflyDeploymentsDirectory() throws Exception {

        System.setProperty(JBOSS_SERVER_BASE_DIR, SOME_PATH_TO_WILDFLY);
        assertThat(wildflyDeploymentDirectoryLocator.getDeploymentDirectory().toString(), is(SOME_PATH_TO_WILDFLY + "/deployments"));
    }

    @Test
    public void shouldFailIfTheJbossBaseDirSystemPropertyNotSpecified() throws Exception {

        try {
            wildflyDeploymentDirectoryLocator.getDeploymentDirectory();
            fail();
        } catch (final MissingJbossServerBaseDirPropertyException expected) {
            assertThat(expected.getMessage(), is("Cannot find wildfly base dir: System property 'jboss.server.base.dir' not specified"));
        }
    }
}