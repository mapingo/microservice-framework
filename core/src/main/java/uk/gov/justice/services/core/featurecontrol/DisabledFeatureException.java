package uk.gov.justice.services.core.featurecontrol;

import uk.gov.justice.services.common.exception.ForbiddenRequestException;

public class DisabledFeatureException extends ForbiddenRequestException {

    public DisabledFeatureException(final String message) {
        super(message);
    }
}
