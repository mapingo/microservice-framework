package uk.gov.justice.services.core.featurecontrol.local;

import java.net.URL;
import java.util.Optional;

import javax.inject.Inject;

public class LocalFeatureFileLocator {

    public static final String FEATURE_CONTROL_FILE_NAME = "feature-control.yaml";

    @Inject
    private ClasspathLocalFeatureFileLocator classpathLocalFeatureFileLocator;

    @Inject
    private WildflyDeploymentDirLocalFeatureFileLocator wildflyDeploymentDirLocalFeatureFileLocator;

    public Optional<URL> findLocalFeatureFileLocation() {

        final Optional<URL> classpathUrl = classpathLocalFeatureFileLocator.findLocalFeatureFileLocation(FEATURE_CONTROL_FILE_NAME);

        if (classpathUrl.isPresent()) {
            return classpathUrl;
        }

        return wildflyDeploymentDirLocalFeatureFileLocator.findLocalFeatureFileLocation(FEATURE_CONTROL_FILE_NAME);
    }
}
