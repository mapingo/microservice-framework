package uk.gov.justice.services.core.featurecontrol.remote;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.core.featurecontrol.FeatureFetcher;
import uk.gov.justice.services.core.featurecontrol.domain.Feature;
import uk.gov.justice.services.ejb.timer.TimerServiceManager;

import javax.ejb.TimerService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class FeatureStoreTimerBeanTest {

    @Mock
    private FeatureFetcher featureFetcher;

    @Mock
    private TimerServiceManager timerServiceManager;

    @Mock
    private TimerService timerService;

    @Mock
    private FeatureRefreshTimerConfig featureRefreshTimerConfig;

    @InjectMocks
    private FeatureStoreTimerBean featureStoreTimerBean;

    @Test
    public void shouldStartTheTimerServiceOnStartup() throws Exception {

        final long timerStartWaitMilliseconds = 934L;
        final long timerIntervalMilliseconds = 987234L;

        when(featureRefreshTimerConfig.getTimerStartWaitMilliseconds()).thenReturn(timerStartWaitMilliseconds);
        when(featureRefreshTimerConfig.getTimerIntervalMilliseconds()).thenReturn(timerIntervalMilliseconds);

        featureStoreTimerBean.startTimerService();

        verify(timerServiceManager).createIntervalTimer(
                "framework.feature-store-refresh.job",
                timerStartWaitMilliseconds,
                timerIntervalMilliseconds,
                timerService);
    }

    @Test
    public void shouldFetchFeaturesAndStore() throws Exception {

        final Feature feature_1 = new Feature("some-feature-1", "some-description-1", true);
        final Feature feature_2 = new Feature("some-feature-2", "some-description-2", true);
        final Feature feature_3 = new Feature("some-feature-3", "some-description-3", true);

        assertThat(featureStoreTimerBean.lookup(feature_1.getFeatureName()).isPresent(), is(false));
        assertThat(featureStoreTimerBean.lookup(feature_2.getFeatureName()).isPresent(), is(false));
        assertThat(featureStoreTimerBean.lookup(feature_3.getFeatureName()).isPresent(), is(false));

        when(featureFetcher.fetchFeatures()).thenReturn(asList(feature_1, feature_2, feature_3));
        featureStoreTimerBean.reloadFeatures();

        final Feature foundFeature_1 = featureStoreTimerBean.lookup(feature_1.getFeatureName())
                .orElseThrow(AssertionError::new);
        final Feature foundFeature_2 = featureStoreTimerBean.lookup(feature_2.getFeatureName())
                .orElseThrow(AssertionError::new);
        final Feature foundFeature_3 = featureStoreTimerBean.lookup(feature_3.getFeatureName())
                .orElseThrow(AssertionError::new);

        assertThat(foundFeature_1, is(feature_1));
        assertThat(foundFeature_2, is(feature_2));
        assertThat(foundFeature_3, is(feature_3));
    }
}