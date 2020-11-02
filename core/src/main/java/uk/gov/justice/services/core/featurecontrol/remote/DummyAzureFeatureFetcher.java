package uk.gov.justice.services.core.featurecontrol.remote;

import static java.util.Collections.emptyList;

import uk.gov.justice.services.core.featurecontrol.domain.Feature;

import java.util.List;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;

@Singleton
@Alternative
@Priority(100)
public class DummyAzureFeatureFetcher implements AzureFeatureFetcher {

    @Override
    public List<Feature> fetchFeatures() {

        return emptyList();
    }
}
