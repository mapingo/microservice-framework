package uk.gov.justice.services.core.featurecontrol.remote;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class DummyFeatureFetcherTest {

    @Mock
    private Logger logger;

    @InjectMocks
    private DummyFeatureFetcher dummyFeatureFetcher;

    @Test
    public void shouldReturnJustAnEmptyListOfFeatures() throws Exception {

        assertThat(dummyFeatureFetcher.fetchFeatures().isEmpty(), is(true));

        verify(logger).warn("Using dummy implementation of FeatureFetcher. Returning an empty list of Features by default");
    }
}