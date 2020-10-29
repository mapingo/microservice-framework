package uk.gov.justice.services.core.featurecontrol.local;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.io.File;
import java.net.URL;
import java.util.Optional;

import javax.inject.Inject;

public class WildflyDeploymentDirLocalFeatureFileLocator {

    @Inject
    private WildflyDeploymentDirectoryLocator wildflyDeploymentDirectoryLocator;

    @Inject
    private FileToUrlConverter fileToUrlConverter;

    public Optional<URL> findLocalFeatureFileLocation(final String featureControlFileName) {

        final File deploymentDirectory = wildflyDeploymentDirectoryLocator.getDeploymentDirectory().toFile();

        if (deploymentDirectory.exists()) {

            final File file = new File(deploymentDirectory, featureControlFileName);

            if (file.exists()) {
                return of(fileToUrlConverter.toUrl(file));
            }
        }

        return empty();
    }
}
