package uk.gov.justice.services.healthcheck.run;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.common.configuration.ContextNameProvider;
import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.healthcheck.api.Healthcheck;
import uk.gov.justice.services.healthcheck.api.IgnoredHealthcheckNamesProvider;
import uk.gov.justice.services.healthcheck.registration.HealthcheckRegistry;

import java.time.ZonedDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HealthcheckProcessRunnerTest {

    @Mock
    private HealthcheckRegistry healthcheckRegistry;

    @Mock
    private HealthcheckRunner healthcheckRunner;

    @Mock
    private ResultsCounter resultsCounter;

    @Mock
    private ContextNameProvider contextNameProvider;

    @Mock
    private IgnoredHealthcheckNamesProvider ignoredHealthcheckNamesProvider;

    @Mock
    private UtcClock clock;

    @InjectMocks
    private HealthcheckProcessRunner healthcheckProcessRunner;

    @Test
    public void shouldReturnResultsWithSuccessMessageIfAllHealthchecksSucceeded() throws Exception {

        final ZonedDateTime now = new UtcClock().now();

        final String contextName = "some-context";
        final int numberPassed = 3;
        final int numberFailed = 0;

        final Healthcheck healthcheck_1 = mock(Healthcheck.class);
        final Healthcheck healthcheck_2 = mock(Healthcheck.class);
        final Healthcheck healthcheck_3 = mock(Healthcheck.class);

        final HealthcheckRunDetails healthcheckRunDetails_1 = mock(HealthcheckRunDetails.class);
        final HealthcheckRunDetails healthcheckRunDetails_2 = mock(HealthcheckRunDetails.class);
        final HealthcheckRunDetails healthcheckRunDetails_3 = mock(HealthcheckRunDetails.class);

        final List<HealthcheckRunDetails> healthcheckRunDetails = asList(
                healthcheckRunDetails_1,
                healthcheckRunDetails_2,
                healthcheckRunDetails_3);

        when(contextNameProvider.getContextName()).thenReturn(contextName);
        when(clock.now()).thenReturn(now);
        when(healthcheckRegistry.getHealthChecks()).thenReturn(asList(healthcheck_1, healthcheck_2, healthcheck_3));
        when(healthcheckRunner.runSingleHealthcheck(healthcheck_1)).thenReturn(healthcheckRunDetails_1);
        when(healthcheckRunner.runSingleHealthcheck(healthcheck_2)).thenReturn(healthcheckRunDetails_2);
        when(healthcheckRunner.runSingleHealthcheck(healthcheck_3)).thenReturn(healthcheckRunDetails_3);
        when(resultsCounter.countNumberPassed(healthcheckRunDetails)).thenReturn(numberPassed);
        when(resultsCounter.countNumberFailed(healthcheckRunDetails)).thenReturn(numberFailed);
        when(ignoredHealthcheckNamesProvider.getNamesOfIgnoredHealthChecks()).thenReturn(emptyList());

        final HealthcheckRunResults healthcheckRunResults = healthcheckProcessRunner.runAllHealthchecks();

        assertThat(healthcheckRunResults.getContextName(), is(contextName));
        assertThat(healthcheckRunResults.getDateRun(), is(now));
        assertThat(healthcheckRunResults.isAllHealthchecksPassed(), is(true));
        assertThat(healthcheckRunResults.getTotalNumberOfHealthchecksRun(), is(3));
        assertThat(healthcheckRunResults.getNumberOfSuccesses(), is(3));
        assertThat(healthcheckRunResults.getNumberOfFailures(), is(0));
        assertThat(healthcheckRunResults.getIgnoredHealthchecks(), is("None"));

        assertThat(healthcheckRunResults.getResults(), is("SUCCESS: all 3 out of 3 healthchecks passed"));

        assertThat(healthcheckRunResults.getHealthcheckRunDetails().size(), is(3));
        assertThat(healthcheckRunResults.getHealthcheckRunDetails().get(0), is(healthcheckRunDetails_1));
        assertThat(healthcheckRunResults.getHealthcheckRunDetails().get(1), is(healthcheckRunDetails_2));
        assertThat(healthcheckRunResults.getHealthcheckRunDetails().get(2), is(healthcheckRunDetails_3));
    }

    @Test
    public void shouldReturnResultsWithFailureMessageIfAnyHealthchecksFailed() throws Exception {

        final ZonedDateTime now = new UtcClock().now();

        final String contextName = "some-context";
        final int numberPassed = 1;
        final int numberFailed = 2;

        final Healthcheck healthcheck_1 = mock(Healthcheck.class);
        final Healthcheck healthcheck_2 = mock(Healthcheck.class);
        final Healthcheck healthcheck_3 = mock(Healthcheck.class);

        final HealthcheckRunDetails healthcheckRunDetails_1 = mock(HealthcheckRunDetails.class);
        final HealthcheckRunDetails healthcheckRunDetails_2 = mock(HealthcheckRunDetails.class);
        final HealthcheckRunDetails healthcheckRunDetails_3 = mock(HealthcheckRunDetails.class);

        final List<HealthcheckRunDetails> healthcheckRunDetails = asList(
                healthcheckRunDetails_1,
                healthcheckRunDetails_2,
                healthcheckRunDetails_3);

        when(contextNameProvider.getContextName()).thenReturn(contextName);
        when(clock.now()).thenReturn(now);
        when(healthcheckRegistry.getHealthChecks()).thenReturn(asList(healthcheck_1, healthcheck_2, healthcheck_3));
        when(healthcheckRunner.runSingleHealthcheck(healthcheck_1)).thenReturn(healthcheckRunDetails_1);
        when(healthcheckRunner.runSingleHealthcheck(healthcheck_2)).thenReturn(healthcheckRunDetails_2);
        when(healthcheckRunner.runSingleHealthcheck(healthcheck_3)).thenReturn(healthcheckRunDetails_3);
        when(resultsCounter.countNumberPassed(healthcheckRunDetails)).thenReturn(numberPassed);
        when(resultsCounter.countNumberFailed(healthcheckRunDetails)).thenReturn(numberFailed);
        when(ignoredHealthcheckNamesProvider.getNamesOfIgnoredHealthChecks()).thenReturn(emptyList());

        final HealthcheckRunResults healthcheckRunResults = healthcheckProcessRunner.runAllHealthchecks();

        assertThat(healthcheckRunResults.getContextName(), is(contextName));
        assertThat(healthcheckRunResults.getDateRun(), is(now));
        assertThat(healthcheckRunResults.isAllHealthchecksPassed(), is(false));
        assertThat(healthcheckRunResults.getTotalNumberOfHealthchecksRun(), is(3));
        assertThat(healthcheckRunResults.getNumberOfSuccesses(), is(numberPassed));
        assertThat(healthcheckRunResults.getNumberOfFailures(), is(numberFailed));
        assertThat(healthcheckRunResults.getIgnoredHealthchecks(), is("None"));

        assertThat(healthcheckRunResults.getResults(), is("ERROR: 2 out of 3 healthchecks failed"));

        assertThat(healthcheckRunResults.getHealthcheckRunDetails().size(), is(3));
        assertThat(healthcheckRunResults.getHealthcheckRunDetails().get(0), is(healthcheckRunDetails_1));
        assertThat(healthcheckRunResults.getHealthcheckRunDetails().get(1), is(healthcheckRunDetails_2));
        assertThat(healthcheckRunResults.getHealthcheckRunDetails().get(2), is(healthcheckRunDetails_3));
    }

    @Test
    public void shouldGenerateTheCorrectListOfAnyIgnoredHealthchecks() throws Exception {

        final ZonedDateTime now = new UtcClock().now();

        final String contextName = "some-context";
        final int numberPassed = 3;
        final int numberFailed = 0;

        final Healthcheck healthcheck_1 = mock(Healthcheck.class);
        final Healthcheck healthcheck_2 = mock(Healthcheck.class);
        final Healthcheck healthcheck_3 = mock(Healthcheck.class);

        final HealthcheckRunDetails healthcheckRunDetails_1 = mock(HealthcheckRunDetails.class);
        final HealthcheckRunDetails healthcheckRunDetails_2 = mock(HealthcheckRunDetails.class);
        final HealthcheckRunDetails healthcheckRunDetails_3 = mock(HealthcheckRunDetails.class);

        final List<HealthcheckRunDetails> healthcheckRunDetails = asList(
                healthcheckRunDetails_1,
                healthcheckRunDetails_2,
                healthcheckRunDetails_3);

        when(contextNameProvider.getContextName()).thenReturn(contextName);
        when(clock.now()).thenReturn(now);
        when(healthcheckRegistry.getHealthChecks()).thenReturn(asList(healthcheck_1, healthcheck_2, healthcheck_3));
        when(healthcheckRunner.runSingleHealthcheck(healthcheck_1)).thenReturn(healthcheckRunDetails_1);
        when(healthcheckRunner.runSingleHealthcheck(healthcheck_2)).thenReturn(healthcheckRunDetails_2);
        when(healthcheckRunner.runSingleHealthcheck(healthcheck_3)).thenReturn(healthcheckRunDetails_3);
        when(resultsCounter.countNumberPassed(healthcheckRunDetails)).thenReturn(numberPassed);
        when(resultsCounter.countNumberFailed(healthcheckRunDetails)).thenReturn(numberFailed);
        when(ignoredHealthcheckNamesProvider.getNamesOfIgnoredHealthChecks()).thenReturn(asList("ignored-healthcheck-1", "ignored-healthcheck-2"));

        final HealthcheckRunResults healthcheckRunResults = healthcheckProcessRunner.runAllHealthchecks();

        assertThat(healthcheckRunResults.getContextName(), is(contextName));
        assertThat(healthcheckRunResults.getDateRun(), is(now));
        assertThat(healthcheckRunResults.isAllHealthchecksPassed(), is(true));
        assertThat(healthcheckRunResults.getTotalNumberOfHealthchecksRun(), is(3));
        assertThat(healthcheckRunResults.getNumberOfSuccesses(), is(3));
        assertThat(healthcheckRunResults.getNumberOfFailures(), is(0));
        assertThat(healthcheckRunResults.getIgnoredHealthchecks(), is("[ignored-healthcheck-1, ignored-healthcheck-2]"));

        assertThat(healthcheckRunResults.getResults(), is("SUCCESS: all 3 out of 3 healthchecks passed"));

        assertThat(healthcheckRunResults.getHealthcheckRunDetails().size(), is(3));
        assertThat(healthcheckRunResults.getHealthcheckRunDetails().get(0), is(healthcheckRunDetails_1));
        assertThat(healthcheckRunResults.getHealthcheckRunDetails().get(1), is(healthcheckRunDetails_2));
        assertThat(healthcheckRunResults.getHealthcheckRunDetails().get(2), is(healthcheckRunDetails_3));
    }
}