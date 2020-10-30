package uk.gov.justice.services.core.featurecontrol.local;

public class MissingJbossServerBaseDirPropertyException extends RuntimeException{

    public MissingJbossServerBaseDirPropertyException(final String message) {
        super(message);
    }
}
