package uk.gov.justice.services.core.featurecontrol.local;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.net.URL;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;

public class ClasspathLocalFeatureFileLocator {

    @Inject
    private Logger logger;

    public Optional<URL> findLocalFeatureFileLocation(final String featureControlFileName) {

        final URL url = getClass().getClassLoader().getResource(featureControlFileName);

        if (url != null) {
            logger.warn(format("Feature control file found on classpath: '%s'", url));
            return of(url);
        }

        return empty();
    }
}
