package uk.gov.justice.services.core.featurecontrol;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.core.featurecontrol.domain.Feature;
import uk.gov.justice.services.core.featurecontrol.local.LocalFeatureStore;
import uk.gov.justice.services.core.featurecontrol.remote.FeatureStoreTimerBean;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FeatureStoreFacadeTest {

    @Mock
    private LocalFeatureStore localFeatureStore;

    @Mock
    private FeatureStoreTimerBean featureStoreTimerBean;

    @InjectMocks
    private FeatureStoreFacade featureStoreFacade;

    @Test
    public void shouldLookupFeaturesInTheFeatureStoreTimerBean() throws Exception {

        final String featureName = "some-feature";

        final Feature feature = mock(Feature.class);

        when(localFeatureStore.lookup(featureName)).thenReturn(empty());
        when(featureStoreTimerBean.lookup(featureName)).thenReturn(of(feature));

        assertThat(featureStoreFacade.lookup(featureName), is(of(feature)));
    }

    @Test
    public void shouldReturnEmptyIfTheFeatureCannotBeFound() throws Exception {

        final String unknownFeatureName = "some-unknown-feature";

        when(localFeatureStore.lookup(unknownFeatureName)).thenReturn(empty());
        when(featureStoreTimerBean.lookup(unknownFeatureName)).thenReturn(empty());

        assertThat(featureStoreFacade.lookup(unknownFeatureName).isPresent(), is(false));
    }

    @Test
    public void shouldOverrideTheFeatureWithALocalOneIfFound() throws Exception {

        final String featureName = "some-feature";

        final Feature feature = mock(Feature.class);

        when(localFeatureStore.lookup(featureName)).thenReturn(of(feature));

        assertThat(featureStoreFacade.lookup(featureName), is(of(feature)));

        verifyZeroInteractions(featureStoreTimerBean);
    }
}