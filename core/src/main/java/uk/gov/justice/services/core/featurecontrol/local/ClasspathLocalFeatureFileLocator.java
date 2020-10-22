package uk.gov.justice.services.core.featurecontrol.local;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.net.URL;
import java.util.Optional;

public class ClasspathLocalFeatureFileLocator {

    public Optional<URL> findLocalFeatureFileLocation(final String featureControlFileName) {

        final URL url = getClass().getClassLoader().getResource(featureControlFileName);

        if (url != null) {
            return of(url);
        }

        return empty();
    }
}
