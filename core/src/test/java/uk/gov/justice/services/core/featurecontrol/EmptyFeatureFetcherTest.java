package uk.gov.justice.services.core.featurecontrol;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmptyFeatureFetcherTest {

    @InjectMocks
    private EmptyFeatureFetcher emptyFeatureFetcher;

    @Test
    public void shouldReturnAnEmptyListOfFeatures() throws Exception {

        assertThat(emptyFeatureFetcher.fetchFeatures().isEmpty(), is(true));
    }
}