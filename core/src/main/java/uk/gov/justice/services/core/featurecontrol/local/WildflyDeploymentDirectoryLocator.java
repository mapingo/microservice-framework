package uk.gov.justice.services.core.featurecontrol.local;

import java.io.File;
import java.nio.file.Path;

public class WildflyDeploymentDirectoryLocator {

    public Path getDeploymentDirectory() {

        final String wildflyBaseDir = System.getProperty("jboss.server.base.dir");

        return new File(wildflyBaseDir, "deployments").toPath();
    }
}
