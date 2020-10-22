package uk.gov.justice.services.core.featurecontrol.local;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static uk.gov.justice.services.core.featurecontrol.local.LocalFeatureFileLocator.FEATURE_CONTROL_FILE_NAME;

import java.net.URL;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClasspathLocalFeatureFileLocatorTest {

    @InjectMocks
    private ClasspathLocalFeatureFileLocator classpathLocalFeatureFileLocator;

    @Test
    public void shouldFindTheLocationOfAFeatureControlFileOnTheClasspath() throws Exception {

        final String featureControlFileName = FEATURE_CONTROL_FILE_NAME;
        final Optional<URL> localFeatureFileLocation = classpathLocalFeatureFileLocator.findLocalFeatureFileLocation(featureControlFileName);

        if (localFeatureFileLocation.isPresent()) {
            assertThat(localFeatureFileLocation.get().toString(), endsWith("/" + featureControlFileName));
        } else {
            fail();
        }
    }

    @Test
    public void shouldReturnEmptyIfNoFileFound() throws Exception {

        assertThat(classpathLocalFeatureFileLocator.findLocalFeatureFileLocation("this-file-does-not-exist.yaml").isPresent(), is(false));
    }
}