package uk.gov.justice.services.healthcheck.servlet;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.of;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;

import uk.gov.justice.services.common.converter.ObjectToJsonObjectConverter;
import uk.gov.justice.services.healthcheck.run.HealthcheckRunDetails;
import uk.gov.justice.services.healthcheck.run.HealthcheckRunResults;
import uk.gov.justice.services.test.utils.framework.api.JsonObjectConvertersFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HealthcheckToJsonConverterTest {

    @Spy
    private ObjectToJsonObjectConverter jsonObjectConvertersFactory = new JsonObjectConvertersFactory()
                .objectToJsonObjectConverter();

    @InjectMocks
    private HealthcheckToJsonConverter healthcheckToJsonConverter;

    @Test
    public void shouldConvertHealthcheckRunResultsToJsonPrettyPrinted() throws Exception {

        final HealthcheckRunResults healthcheckResults = createHealthcheckResults();

        final String json = healthcheckToJsonConverter.toJson(healthcheckResults);

        with(json)
                .assertThat("$.contextName", is("some-context"))
                .assertThat("$.dateRun", is("2022-02-23T11:23:23.000Z"))
                .assertThat("$.allHealthchecksPassed", is(false))
                .assertThat("$.results", is("ERROR: 1 out of 2 healthchecks failed"))
                .assertThat("$.totalNumberOfHealthchecksRun", is(2))
                .assertThat("$.numberOfSuccesses", is(1))
                .assertThat("$.numberOfFailures", is(1))
                .assertThat("$.healthcheckRunDetails[0].healthcheckName", is("event-store-database-online"))
                .assertThat("$.healthcheckRunDetails[0].description", is("Checks connectivity to the event store database"))
                .assertThat("$.healthcheckRunDetails[0].passed", is(true))
                .assertThat("$.healthcheckRunDetails[1].healthcheckName", is("view-store-database-online"))
                .assertThat("$.healthcheckRunDetails[1].description", is("Checks connectivity to the view store database"))
                .assertThat("$.healthcheckRunDetails[1].passed", is(false))
                .assertThat("$.healthcheckRunDetails[1].errorMessage", is("Socket timeout"))
                ;
    }

    private HealthcheckRunResults createHealthcheckResults() {

        return new HealthcheckRunResults(
                "some-context",
                of(2022, 2, 23, 11, 23, 23, 0, UTC),
                false,
                "ERROR: 1 out of 2 healthchecks failed",
                2,
                1,
                1,
                "fred", asList(
                        new HealthcheckRunDetails(
                                "event-store-database-online",
                                "Checks connectivity to the event store database",
                                true,
                                null
                        ),
                        new HealthcheckRunDetails(
                                "view-store-database-online",
                                "Checks connectivity to the view store database",
                                false,
                                "Socket timeout"
                        ))
        );
    }
}