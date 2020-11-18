package uk.gov.justice.services.core.featurecontrol.remote;

import uk.gov.justice.services.core.featurecontrol.FeatureFetcher;
import uk.gov.justice.services.core.featurecontrol.domain.Feature;

import java.util.Optional;

import javax.inject.Inject;

public class NonCachingFeatureProvider {

    @Inject
    private FeatureFetcher featureFetcher;

    public Optional<Feature> lookup(final String featureName) {
        return featureFetcher.fetchFeatures()
                .stream()
                .filter(feature -> featureName.equals(feature.getFeatureName()))
                .findFirst();
    }
}
