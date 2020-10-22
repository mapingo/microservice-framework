package uk.gov.justice.services.core.featurecontrol.local;

import static java.util.Optional.empty;

import uk.gov.justice.services.core.featurecontrol.domain.Feature;
import uk.gov.justice.services.core.featurecontrol.domain.FeatureControl;
import uk.gov.justice.services.yaml.YamlParser;

import java.net.URL;
import java.util.Optional;

import javax.inject.Inject;

public class LocalFeatureStore {

    @Inject
    private YamlParser yamlParser;

    @Inject
    private LocalFeatureFileLocator localFeatureFileLocator;

    public Optional<Feature> lookup(final String featureName) {

        final Optional<URL> localFeatureFileLocation = localFeatureFileLocator.findLocalFeatureFileLocation();

        if (localFeatureFileLocation.isPresent()) {

            final FeatureControl featureControlList = yamlParser.parseYamlFrom(
                    localFeatureFileLocation.get(),
                    FeatureControl.class);

            return featureControlList
                    .getFeatures()
                    .stream()
                    .filter(feature -> feature.getFeatureName().equals(featureName))
                    .findFirst();
        }

        return empty();
    }
}
