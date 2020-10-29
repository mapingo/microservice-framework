package uk.gov.justice.services.core.featurecontrol;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import uk.gov.justice.services.core.annotation.FeatureControl;
import uk.gov.justice.services.core.annotation.Features;

import java.lang.reflect.Method;
import java.util.List;

public class FeatureControlAnnotationFinder {

    /**
     * Gets a list of any {@link FeatureControl} annotations on the method
     *
     * @param handlerMethod A handler method of a service component class
     *
     * @return A list of any {@link FeatureControl} annotations
     */
    public List<String> findAnnotatedFeatures(final Method handlerMethod) {

        final FeatureControl featureControl = handlerMethod.getAnnotation(FeatureControl.class);

        if (featureControl != null) {
            return singletonList(featureControl.value());
        }

        final Features features = handlerMethod.getAnnotation(Features.class);

        if (features != null) {
            return stream(features.value())
                    .map(FeatureControl::value)
                    .collect(toList());
        }

        return emptyList();
    }
}
