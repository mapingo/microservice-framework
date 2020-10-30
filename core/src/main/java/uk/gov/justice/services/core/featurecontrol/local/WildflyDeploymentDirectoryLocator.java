package uk.gov.justice.services.core.featurecontrol.local;

import static java.lang.String.format;

import java.io.File;
import java.nio.file.Path;

public class WildflyDeploymentDirectoryLocator {

    public static final String JBOSS_SERVER_BASE_DIR = "jboss.server.base.dir";

    public Path getDeploymentDirectory() {

        final String wildflyBaseDir = System.getProperty(JBOSS_SERVER_BASE_DIR);

        if (wildflyBaseDir != null) {
            return new File(wildflyBaseDir, "deployments").toPath();
        }

        throw new MissingJbossServerBaseDirPropertyException(
                format(
                        "Cannot find wildfly base dir: System property '%s' not specified",
                        JBOSS_SERVER_BASE_DIR));
    }
}
