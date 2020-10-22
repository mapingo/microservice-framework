package uk.gov.justice.services.core.featurecontrol.local;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.core.featurecontrol.local.LocalFeatureFileLocator.FEATURE_CONTROL_FILE_NAME;

import java.net.URL;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocalFeatureFileLocatorTest {

    @Mock
    private ClasspathLocalFeatureFileLocator classpathLocalFeatureFileLocator;

    @Mock
    private WildflyDeploymentDirLocalFeatureFileLocator wildflyDeploymentDirLocalFeatureFileLocator;

    @InjectMocks
    private LocalFeatureFileLocator localFeatureFileLocator;

    @Test
    public void shouldReturnTheFileLocationFromTheClasspathIfItExists() throws Exception {

        final URL classpathUrl = aRealUrlBecauseWeCannotMockTheDamnThings();
        when(classpathLocalFeatureFileLocator.findLocalFeatureFileLocation(FEATURE_CONTROL_FILE_NAME)).thenReturn(of(classpathUrl));

        final Optional<URL> localFeatureFileLocation = localFeatureFileLocator.findLocalFeatureFileLocation();

        if (localFeatureFileLocation.isPresent()) {
            assertThat(localFeatureFileLocation.get(), is(classpathUrl));
        } else {
            fail();
        }

        verifyZeroInteractions(wildflyDeploymentDirLocalFeatureFileLocator);
    }

    @Test
    public void shouldReturnTheFileLocationFromWildflyDeploymentDirectoryIfTheClasspathFileDoesNotExists() throws Exception {

        final Optional<URL> classpathUrl = empty();

        final URL wildflyDeploymentDirUrl = aRealUrlBecauseWeCannotMockTheDamnThings();
        when(classpathLocalFeatureFileLocator.findLocalFeatureFileLocation(FEATURE_CONTROL_FILE_NAME)).thenReturn(classpathUrl);
        when(wildflyDeploymentDirLocalFeatureFileLocator.findLocalFeatureFileLocation(FEATURE_CONTROL_FILE_NAME)).thenReturn(of(wildflyDeploymentDirUrl));
        final Optional<URL> localFeatureFileLocation = localFeatureFileLocator.findLocalFeatureFileLocation();

        if (localFeatureFileLocation.isPresent()) {
            assertThat(localFeatureFileLocation.get(), is(wildflyDeploymentDirUrl));
        } else {
            fail();
        }
    }

    @Test
    public void shouldReturnEmptyIfNotFoundOnClasspathNorWildflyDeploymentDirectory() throws Exception {

        when(classpathLocalFeatureFileLocator.findLocalFeatureFileLocation(FEATURE_CONTROL_FILE_NAME)).thenReturn(empty());
        when(wildflyDeploymentDirLocalFeatureFileLocator.findLocalFeatureFileLocation(FEATURE_CONTROL_FILE_NAME)).thenReturn(empty());

        assertThat(localFeatureFileLocator.findLocalFeatureFileLocation().isPresent(), is(false));

    }

    private URL aRealUrlBecauseWeCannotMockTheDamnThings() {
        final URL url = getClass().getClassLoader().getResource("feature-control.yaml");
        assertThat(url, is(notNullValue()));
        return url;
    }
}