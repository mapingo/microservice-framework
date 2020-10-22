package uk.gov.justice.services.core.featurecontrol;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.core.featurecontrol.domain.Feature;
import uk.gov.justice.services.core.featurecontrol.local.LocalFeatureStore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DefaultFeatureStoreTest {

    @Mock
    private LocalFeatureStore localFeatureStore;

    @InjectMocks
    private DefaultFeatureStore defaultFeatureStore;

    @Test
    public void shouldOnlyCallTheLocalFeatureStoreForNow() throws Exception {

        final String featureName = "some-feature";
        final String unknownFeatureName = "some-unknown-feature";


        final Feature feature = mock(Feature.class);

        when(localFeatureStore.lookup(featureName)).thenReturn(of(feature));
        when(localFeatureStore.lookup(unknownFeatureName)).thenReturn(empty());

        assertThat(defaultFeatureStore.lookup(featureName), is(of(feature)));
        assertThat(defaultFeatureStore.lookup(unknownFeatureName), is(empty()));
    }
}