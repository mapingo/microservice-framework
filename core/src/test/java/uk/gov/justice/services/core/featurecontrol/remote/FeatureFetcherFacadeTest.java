package uk.gov.justice.services.core.featurecontrol.remote;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.core.featurecontrol.FeatureFetcher;
import uk.gov.justice.services.core.featurecontrol.domain.Feature;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class FeatureFetcherFacadeTest {

    @Mock
    private FeatureControlConfiguration featureControlConfiguration;

    @Mock
    private FeatureFetcher featureFetcher;

    @Mock
    private Logger logger;

    @InjectMocks
    private FeatureFetcherFacade featureFetcherFacade;

    @Test
    public void shouldFetchTheRemoteListOfFeaturesIfFeatureControlEnabled() throws Exception {

        final Feature feature = new Feature("some-feature", true);

        final List<Feature> features = singletonList(feature);

        when(featureControlConfiguration.isFeatureControlEnabled()).thenReturn(true);
        when(featureFetcher.fetchFeatures()).thenReturn(features);

        assertThat(featureFetcherFacade.fetchFeatures(), is(sameInstance(features)));

        verify(logger).info("Fetched list of Features from remote feature store: '[Feature{featureName='some-feature', enabled=true}]'");

        verifyNoMoreInteractions(logger);
    }

    @Test
    public void shouldEmptyListOfFeaturesIfFeatureControlDisabled() throws Exception {

        when(featureControlConfiguration.isFeatureControlEnabled()).thenReturn(false);
        assertThat(featureFetcherFacade.fetchFeatures(), is(emptyList()));

        verify(logger).info("Fetched control disabled. No Features fetched from remote feature store");

        verifyNoMoreInteractions(logger);
        verifyZeroInteractions(featureFetcher);
    }
}