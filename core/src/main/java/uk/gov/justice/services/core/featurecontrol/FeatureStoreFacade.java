package uk.gov.justice.services.core.featurecontrol;

import uk.gov.justice.services.core.featurecontrol.domain.Feature;
import uk.gov.justice.services.core.featurecontrol.local.LocalFeatureStore;
import uk.gov.justice.services.core.featurecontrol.lookup.FeatureStore;
import uk.gov.justice.services.core.featurecontrol.remote.FeatureStoreTimerBean;

import java.util.Optional;

import javax.inject.Inject;

public class FeatureStoreFacade implements FeatureStore {

    @Inject
    private LocalFeatureStore localFeatureStore;

    @Inject
    private FeatureStoreTimerBean featureStoreTimerBean;

    @Override
    public Optional<Feature> lookup(final String featureName) {

        final Optional<Feature> feature = localFeatureStore.lookup(featureName);;

        if (feature.isPresent()) {
            return feature;
        }

        return featureStoreTimerBean.lookup(featureName);
    }
}
