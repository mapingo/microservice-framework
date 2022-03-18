package uk.gov.justice.services.healthcheck.run;

import java.util.List;

public class ResultsCounter {

    public int countNumberPassed(final List<HealthcheckRunDetails> healthcheckRunDetails) {

        return (int) healthcheckRunDetails.stream()
                .filter(HealthcheckRunDetails::isPassed)
                .count();
    }

    public int countNumberFailed(final List<HealthcheckRunDetails> healthcheckRunDetails) {

        return (int) healthcheckRunDetails.stream()
                .filter(healthcheckRunDetails1 -> ! healthcheckRunDetails1.isPassed())
                .count();
    }
}
