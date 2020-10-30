package uk.gov.justice.services.core.featurecontrol.local;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.core.featurecontrol.domain.Feature;
import uk.gov.justice.services.yaml.YamlParser;

import java.net.URL;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;


@RunWith(MockitoJUnitRunner.class)
public class LocalFeatureStoreTest {

    @Spy
    private YamlParser yamlParser = new YamlParser();

    @Mock
    private LocalFeatureFileLocator localFeatureFileLocator;

    @Mock
    private Logger logger;

    @InjectMocks
    private LocalFeatureStore localFeatureStore;

    @Test
    public void shouldGetAFeatureFromALocalFile() throws Exception {

        final String featureName = "enabled-feature-2";
        final URL localFeatureFileLocation = getClass().getClassLoader().getResource("feature-control.yaml");

        when(localFeatureFileLocator.findLocalFeatureFileLocation()).thenReturn(ofNullable(localFeatureFileLocation));

        final Optional<Feature> feature = localFeatureStore.lookup(featureName);

        if (feature.isPresent()) {
            assertThat(feature.get().getFeatureName(), is(featureName));
            assertThat(feature.get().getDescription(), is("enabled-description-2"));
            assertThat(feature.get().isEnabled(), is(true));

        } else {
            fail();
        }

        verify(logger).warn("Loading FeatureControl list from local file: '" + localFeatureFileLocation + "'");
    }

    @Test
    public void shouldReturnEmptyIfTheFeatureIsNotSpecifiedInTheLocalFile() throws Exception {

        final String featureName = "missing-feature";
        final URL localFeatureFileLocation = getClass().getClassLoader().getResource("feature-control.yaml");

        when(localFeatureFileLocator.findLocalFeatureFileLocation()).thenReturn(ofNullable(localFeatureFileLocation));

        assertThat(localFeatureStore.lookup(featureName).isPresent(), is(false));

        verify(logger).warn("Loading FeatureControl list from local file: '" + localFeatureFileLocation + "'");
    }

    @Test
    public void shouldReturnEmptyIfNoLocalFeatureFileFound() throws Exception {

        final String featureName = "enabled-feature-2";
        
        when(localFeatureFileLocator.findLocalFeatureFileLocation()).thenReturn(empty());

        assertThat(localFeatureStore.lookup(featureName).isPresent(), is(false));
    }
}