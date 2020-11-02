package uk.gov.justice.services.core.featurecontrol.remote;

import uk.gov.justice.services.core.featurecontrol.domain.Feature;

import java.util.List;

public interface AzureFeatureFetcher {

    List<Feature> fetchFeatures();
}
