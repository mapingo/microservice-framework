package uk.gov.justice.services.clients.unifiedsearch.core;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URL;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class UnifiedSearchFileFinderTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();


    @Test
    public void shouldFindTransformerPaths() throws Exception {

        final UnifiedSearchFileFinder unifiedSearchFileFinder = new UnifiedSearchFileFinder();
        final URL url = unifiedSearchFileFinder.getTransformerPaths("test-spec1.json");

        assertThat(url.toString(), endsWith("/transformer/test-spec1.json"));
    }

    @Test
    public void shouldThrowExceptionWhenTransformerNotFound() throws Exception {

        exception.expect(UnifiedSearchException.class);
        exception.expectMessage("Unable to find file on classpath: non-existent-spec.json");

        final UnifiedSearchFileFinder unifiedSearchFileFinder = new UnifiedSearchFileFinder();
        unifiedSearchFileFinder.getTransformerPaths("non-existent-spec.json");
    }
}