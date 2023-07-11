package uk.gov.justice.services.generators.commons.helper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class PathToUrlResolverTest {

    @Test
    public void shouldResolvePathToUrl() {
        final Path baseDir = Paths.get("/yaml");
        final Path path = Paths.get("unified-search-descriptor.yaml");

        final URL url = new PathToUrlResolver().resolveToUrl(baseDir, path);

        assertThat(url.toString(), is("file:/yaml/unified-search-descriptor.yaml"));
    }

    @Test
    public void shouldThrowUnifiedSearchExceptionWhenResolutionFailsForPathToUrl() {

        final PathToUrlResolver pathToUrlResolver = spy(PathToUrlResolver.class);
        final Path basedir = mock(Path.class);
        final Path url = mock(Path.class);

        given(basedir.resolve(url)).willAnswer(invocation -> {
            throw new MalformedURLException("oops");
        });

        final FileParserException fileParserException = assertThrows(FileParserException.class, () ->
                pathToUrlResolver.resolveToUrl(basedir, url)
        );

        assertThat(fileParserException.getMessage(), is("Cannot resolve path as URL null"));
    }
}