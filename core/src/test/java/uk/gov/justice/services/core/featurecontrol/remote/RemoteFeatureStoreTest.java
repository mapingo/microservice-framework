package uk.gov.justice.services.core.featurecontrol.remote;

import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.core.featurecontrol.domain.Feature;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class RemoteFeatureStoreTest {

    @Mock
    private CachingFeatureProviderTimerBean cachingFeatureProviderTimerBean;

    @Mock
    private NonCachingFeatureProvider nonCachingFeatureProvider;

    @Mock
    private FeatureControlConfiguration featureControlConfiguration;

    @InjectMocks
    private RemoteFeatureStore remoteFeatureStore;

    @Test
    public void shouldLookupFeaturesInTheCacheIfEnabled() throws Exception {

        final String featureName = "some-feature";
        final Optional<Feature> feature = of(mock(Feature.class));

        when(featureControlConfiguration.isFeatureControlCacheEnabled()).thenReturn(true);
        when(cachingFeatureProviderTimerBean.lookup(featureName)).thenReturn(feature);

        assertThat(remoteFeatureStore.lookup(featureName), is(feature));

        verifyNoInteractions(nonCachingFeatureProvider);
    }

    @Test
    public void shouldLookupFeaturesDirectlyIfTheCacheIsDisabled() throws Exception {

        final String featureName = "some-feature";
        final Optional<Feature> feature = of(mock(Feature.class));

        when(featureControlConfiguration.isFeatureControlCacheEnabled()).thenReturn(false);
        when(nonCachingFeatureProvider.lookup(featureName)).thenReturn(feature);

        assertThat(remoteFeatureStore.lookup(featureName), is(feature));

        verifyNoInteractions(cachingFeatureProviderTimerBean);
    }
}