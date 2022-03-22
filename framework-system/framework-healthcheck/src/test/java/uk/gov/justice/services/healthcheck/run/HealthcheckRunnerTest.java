package uk.gov.justice.services.healthcheck.run;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.healthcheck.api.HealthcheckResult.failure;
import static uk.gov.justice.services.healthcheck.api.HealthcheckResult.success;

import uk.gov.justice.services.healthcheck.api.Healthcheck;
import uk.gov.justice.services.healthcheck.api.HealthcheckResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class HealthcheckRunnerTest {

    @Mock
    private Logger logger;

    @InjectMocks
    private HealthcheckRunner healthcheckRunner;

    @Test
    public void shouldRunSuccessfulHealthcheckAndReturnResults() throws Exception {


        final String healthcheckName = "healthcheckName";
        final String healthcheckDescription = "healthcheckDescription";
        final HealthcheckResult successfulHealthcheckResult = success();

        final Healthcheck healthcheck = mock(Healthcheck.class);

        when(healthcheck.getHealthcheckName()).thenReturn(healthcheckName);
        when(healthcheck.healthcheckDescription()).thenReturn(healthcheckDescription);
        when(healthcheck.runHealthcheck()).thenReturn(successfulHealthcheckResult);

        final HealthcheckRunDetails healthcheckRunDetails = healthcheckRunner.runSingleHealthcheck(healthcheck);

        assertThat(healthcheckRunDetails.getHealthcheckName(), is(healthcheckName));
        assertThat(healthcheckRunDetails.getDescription(), is(healthcheckDescription));
        assertThat(healthcheckRunDetails.isPassed(), is(true));
        assertThat(healthcheckRunDetails.getErrorMessage(), is(nullValue()));
    }

    @Test
    public void shouldRunUnsuccessfulHealthcheckAndReturnResults() throws Exception {

        final String healthcheckName = "healthcheckName";
        final String healthcheckDescription = "healthcheckDescription";
        final String errorMessage = "Well I never did";
        final HealthcheckResult failedHealthcheckResult = failure(errorMessage);

        final Healthcheck healthcheck = mock(Healthcheck.class);

        when(healthcheck.getHealthcheckName()).thenReturn(healthcheckName);
        when(healthcheck.healthcheckDescription()).thenReturn(healthcheckDescription);
        when(healthcheck.runHealthcheck()).thenReturn(failedHealthcheckResult);

        final HealthcheckRunDetails healthcheckRunDetails = healthcheckRunner.runSingleHealthcheck(healthcheck);

        assertThat(healthcheckRunDetails.getHealthcheckName(), is(healthcheckName));
        assertThat(healthcheckRunDetails.getDescription(), is(healthcheckDescription));
        assertThat(healthcheckRunDetails.isPassed(), is(false));
        assertThat(healthcheckRunDetails.getErrorMessage(), is(errorMessage));
    }

    @Test
    public void shouldHandleAllExceptionsLogAndReturnAsFailure() throws Exception {

        final String healthcheckName = "healthcheckName";
        final String healthcheckDescription = "healthcheckDescription";
        final NullPointerException exception = new NullPointerException("No, no, we're all going to die");

        final Healthcheck healthcheck = mock(Healthcheck.class);

        when(healthcheck.getHealthcheckName()).thenReturn(healthcheckName);
        when(healthcheck.healthcheckDescription()).thenReturn(healthcheckDescription);
        when(healthcheck.runHealthcheck()).thenThrow(exception);

        final HealthcheckRunDetails healthcheckRunDetails = healthcheckRunner.runSingleHealthcheck(healthcheck);

        assertThat(healthcheckRunDetails.getHealthcheckName(), is(healthcheckName));
        assertThat(healthcheckRunDetails.getDescription(), is(healthcheckDescription));
        assertThat(healthcheckRunDetails.isPassed(), is(false));
        assertThat(healthcheckRunDetails.getErrorMessage(), is("NullPointerException thrown when running healthcheck. Exception message: 'No, no, we're all going to die'"));

        verify(logger).error("Failed to run healthcheck 'healthcheckName'", exception);
    }
}