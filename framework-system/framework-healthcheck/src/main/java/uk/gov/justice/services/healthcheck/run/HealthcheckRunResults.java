package uk.gov.justice.services.healthcheck.run;

import java.time.ZonedDateTime;
import java.util.List;

import com.google.common.base.Objects;

public class HealthcheckRunResults {

    private final String contextName;
    private final ZonedDateTime dateRun;
    private final boolean allHealthchecksPassed;
    private final String results;
    private final int totalNumberOfHealthchecksRun;
    private final int numberOfSuccesses;
    private final int numberOfFailures;
    private final String ignoredHealthchecks;
    private final List<HealthcheckRunDetails> healthcheckRunDetails;

    public HealthcheckRunResults(
            final String contextName,
            final ZonedDateTime dateRun,
            final boolean allHealthchecksPassed,
            final String results,
            final int totalNumberOfHealthchecksRun,
            final int numberOfSuccesses,
            final int numberOfFailures,
            final String ignoredHealthchecks,
            final List<HealthcheckRunDetails> healthcheckRunDetails) {
        this.contextName = contextName;
        this.dateRun = dateRun;
        this.allHealthchecksPassed = allHealthchecksPassed;
        this.results = results;
        this.totalNumberOfHealthchecksRun = totalNumberOfHealthchecksRun;
        this.numberOfSuccesses = numberOfSuccesses;
        this.numberOfFailures = numberOfFailures;
        this.ignoredHealthchecks = ignoredHealthchecks;
        this.healthcheckRunDetails = healthcheckRunDetails;
    }

    public String getContextName() {
        return contextName;
    }

    public ZonedDateTime getDateRun() {
        return dateRun;
    }

    public boolean isAllHealthchecksPassed() {
        return allHealthchecksPassed;
    }

    public String getResults() {
        return results;
    }

    public int getTotalNumberOfHealthchecksRun() {
        return totalNumberOfHealthchecksRun;
    }

    public int getNumberOfSuccesses() {
        return numberOfSuccesses;
    }

    public int getNumberOfFailures() {
        return numberOfFailures;
    }

    public String getIgnoredHealthchecks() {
        return ignoredHealthchecks;
    }

    public List<HealthcheckRunDetails> getHealthcheckRunDetails() {
        return healthcheckRunDetails;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof HealthcheckRunResults)) return false;
        final HealthcheckRunResults that = (HealthcheckRunResults) o;
        return allHealthchecksPassed == that.allHealthchecksPassed && totalNumberOfHealthchecksRun == that.totalNumberOfHealthchecksRun && numberOfSuccesses == that.numberOfSuccesses && numberOfFailures == that.numberOfFailures && Objects.equal(contextName, that.contextName) && Objects.equal(dateRun, that.dateRun) && Objects.equal(results, that.results) && Objects.equal(ignoredHealthchecks, that.ignoredHealthchecks) && Objects.equal(healthcheckRunDetails, that.healthcheckRunDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(contextName, dateRun, allHealthchecksPassed, results, totalNumberOfHealthchecksRun, numberOfSuccesses, numberOfFailures, ignoredHealthchecks, healthcheckRunDetails);
    }

    @Override
    public String toString() {
        return "HealthcheckRunResults{" +
                "contextName='" + contextName + '\'' +
                ", dateRun=" + dateRun +
                ", allHealthchecksPassed=" + allHealthchecksPassed +
                ", results='" + results + '\'' +
                ", totalNumberOfHealthchecksRun=" + totalNumberOfHealthchecksRun +
                ", numberOfSuccesses=" + numberOfSuccesses +
                ", numberOfFailures=" + numberOfFailures +
                ", ignoredHealthchecks='" + ignoredHealthchecks + '\'' +
                ", healthcheckRunDetails=" + healthcheckRunDetails +
                '}';
    }
}
