package uk.gov.justice.services.core.featurecontrol.local;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static uk.gov.justice.services.core.featurecontrol.local.LocalFeatureFileLocator.FEATURE_CONTROL_FILE_NAME;

import java.net.URL;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class ClasspathLocalFeatureFileLocatorTest {

    @Mock
    private Logger logger;

    @InjectMocks
    private ClasspathLocalFeatureFileLocator classpathLocalFeatureFileLocator;

    @Test
    public void shouldFindTheLocationOfAFeatureControlFileOnTheClasspath() throws Exception {

        final Optional<URL> localFeatureFileLocation = classpathLocalFeatureFileLocator.findLocalFeatureFileLocation(FEATURE_CONTROL_FILE_NAME);

        if(localFeatureFileLocation.isPresent()) {
            assertThat(localFeatureFileLocation.get().toString(), endsWith("/" + FEATURE_CONTROL_FILE_NAME));
            verify(logger).warn("Feature control file found on classpath: '" + localFeatureFileLocation.get() + "'");
        } else {
            fail();
        }
    }

    @Test
    public void shouldReturnEmptyIfNoFileFound() throws Exception {

        assertThat(classpathLocalFeatureFileLocator.findLocalFeatureFileLocation("this-file-does-not-exist.yaml").isPresent(), is(false));
    }
}