package uk.gov.justice.services.core.featurecontrol;

import uk.gov.justice.services.core.featurecontrol.domain.Feature;
import uk.gov.justice.services.core.featurecontrol.local.LocalFeatureStore;
import uk.gov.justice.services.core.featurecontrol.lookup.FeatureStore;

import java.util.Optional;

import javax.inject.Inject;

public class DefaultFeatureStore implements FeatureStore {

    @Inject
    private LocalFeatureStore localFeatureStore;

    @Override
    public Optional<Feature> lookup(final String featureName) {
        return localFeatureStore.lookup(featureName);
    }
}
