package uk.gov.justice.services.healthcheck.run;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class ResultsCounterTest {

    @InjectMocks
    private ResultsCounter resultsCounter;

    @Test
    public void shouldCountNumberOfPassedAndFailedHealthchecks() throws Exception {

        final HealthcheckRunDetails healthcheckRunDetails_1 = mock(HealthcheckRunDetails.class);
        final HealthcheckRunDetails healthcheckRunDetails_2 = mock(HealthcheckRunDetails.class);
        final HealthcheckRunDetails healthcheckRunDetails_3 = mock(HealthcheckRunDetails.class);

        when(healthcheckRunDetails_1.isPassed()).thenReturn(true);
        when(healthcheckRunDetails_2.isPassed()).thenReturn(false);
        when(healthcheckRunDetails_3.isPassed()).thenReturn(true);

        final List<HealthcheckRunDetails> healthcheckRunDetails = asList(healthcheckRunDetails_1, healthcheckRunDetails_2, healthcheckRunDetails_3);

        assertThat(resultsCounter.countNumberPassed(healthcheckRunDetails), is(2));
        assertThat(resultsCounter.countNumberFailed(healthcheckRunDetails), is(1));
    }
}