package uk.gov.justice.services.core.featurecontrol.remote;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

import uk.gov.justice.services.core.featurecontrol.FeatureFetcher;
import uk.gov.justice.services.core.featurecontrol.domain.Feature;

import java.util.List;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;

/**
 *  Dummy implementation of the remote FeatureFetcher to allow it to be overridden in higher environments
 */
@Singleton
@Alternative
@Priority(100)
public class DummyFeatureFetcher implements FeatureFetcher {

    @Inject
    private Logger logger;

    /**
     * Always returns an empty list of features
     *
     * @return An empty List of {@link Feature}s
     */
    @Override
    public List<Feature> fetchFeatures() {

        logger.warn(format("Using dummy implementation of %s. Returning an empty list of Features by default", FeatureFetcher.class.getSimpleName()));
        return emptyList();
    }
}
