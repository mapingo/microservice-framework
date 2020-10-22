package uk.gov.justice.services.core.featurecontrol.local;

import static java.lang.String.format;

import java.io.File;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;

public class FileToUrlConverter {

    public URL toUrl(final File file) {

        try {
            return file.toURI().toURL();
        } catch (final MalformedURLException e) {
            throw new UncheckedIOException(format("Failed to convert file '%s' to URL", file.getAbsolutePath()), e);
        }
    }
}
