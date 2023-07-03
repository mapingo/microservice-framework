package uk.gov.justice.services.clients.unifiedsearch.core;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URL;

import org.junit.jupiter.api.Test;

public class UnifiedSearchFileFinderTest {

    @Test
    public void shouldFindTransformerPaths() throws Exception {

        final UnifiedSearchFileFinder unifiedSearchFileFinder = new UnifiedSearchFileFinder();
        final URL url = unifiedSearchFileFinder.getTransformerPaths("test-spec1.json");

        assertThat(url.toString(), endsWith("/transformer/test-spec1.json"));
    }

    @Test
    public void shouldThrowExceptionWhenTransformerNotFound() throws Exception {

        final UnifiedSearchFileFinder unifiedSearchFileFinder = new UnifiedSearchFileFinder();
        final UnifiedSearchException unifiedSearchException = assertThrows(UnifiedSearchException.class, () ->
                unifiedSearchFileFinder.getTransformerPaths("non-existent-spec.json")
        );

        assertThat(unifiedSearchException.getMessage(), is("Unable to find file on classpath: non-existent-spec.json"));
    }
}