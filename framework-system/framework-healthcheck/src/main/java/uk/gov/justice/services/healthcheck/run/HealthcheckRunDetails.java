package uk.gov.justice.services.healthcheck.run;

import com.google.common.base.Objects;

public class HealthcheckRunDetails {

    private final String healthcheckName;
    private final String description;
    private final boolean passed;
    private final String errorMessage;

    public HealthcheckRunDetails(final String healthcheckName, final String description, final boolean passed, final String errorMessage) {
        this.healthcheckName = healthcheckName;
        this.description = description;
        this.passed = passed;
        this.errorMessage = errorMessage;
    }

    public String getHealthcheckName() {
        return healthcheckName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPassed() {
        return passed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof HealthcheckRunDetails)) return false;
        final HealthcheckRunDetails that = (HealthcheckRunDetails) o;
        return passed == that.passed && Objects.equal(healthcheckName, that.healthcheckName) && Objects.equal(description, that.description) && Objects.equal(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(healthcheckName, description, passed, errorMessage);
    }

    @Override
    public String toString() {
        return "HealthcheckRunDetails{" +
                "healthcheckName='" + healthcheckName + '\'' +
                ", description='" + description + '\'' +
                ", passed=" + passed +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
