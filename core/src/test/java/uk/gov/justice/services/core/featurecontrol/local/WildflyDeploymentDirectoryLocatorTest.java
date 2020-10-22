package uk.gov.justice.services.core.featurecontrol.local;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WildflyDeploymentDirectoryLocatorTest {

    private static final String SOME_PATH_TO_WILDFLY = "/some/path/to/wildfly";

    @BeforeClass
    public static void addJbossBaseDirSystemProperty() {
        System.setProperty("jboss.server.base.dir", SOME_PATH_TO_WILDFLY);
    }

    @AfterClass
    public static void removeJbossBaseDirSystemProperty() {
        System.clearProperty("jboss.server.base.dir");

        assertThat(System.getProperty("jboss.server.base.dir"), is(nullValue()));
    }

    @InjectMocks
    private WildflyDeploymentDirectoryLocator wildflyDeploymentDirectoryLocator;

    @Test
    public void shouldLookupTheWildflyDeploymentsDirectory() throws Exception {

        assertThat(wildflyDeploymentDirectoryLocator.getDeploymentDirectory().toString(), is(SOME_PATH_TO_WILDFLY + "/deployments"));
    }
}