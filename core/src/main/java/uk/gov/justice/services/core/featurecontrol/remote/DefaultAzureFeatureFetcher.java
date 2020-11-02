package uk.gov.justice.services.core.featurecontrol.remote;

import static java.util.Collections.emptyList;

import uk.gov.justice.services.core.featurecontrol.domain.Feature;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

@Default
@ApplicationScoped
public class DefaultAzureFeatureFetcher implements AzureFeatureFetcher {

    @Override
    public List<Feature> fetchFeatures() {

        return emptyList();
    }
}
