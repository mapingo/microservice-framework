package uk.gov.justice.services.core.featurecontrol;

import uk.gov.justice.services.core.annotation.FeatureControl;
import uk.gov.justice.services.core.annotation.Handles;

public class SomeHandler {

    @Handles("some-context.command.do-something-cool")
    @FeatureControl("feature-1")
    @FeatureControl("feature-2")
    public void someHandlerMethodWithFeatures() {

    }

    @FeatureControl("feature-1")
    @Handles("some-context.command.do-something-else-cool")
    public void someHandlerMethodWithSingleFeature() {

    }

    @Handles("some-context.command.do-something-cool-again")
    public void someHandlerMethodWithoutFeatures() {

    }
}



    