package uk.gov.justice.services.clients.unifiedsearch.core;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FileContentsAsStringLoaderTest {

    @InjectMocks
    private FileContentsAsStringLoader fileContentsAsStringLoader;

    @Test
    public void shouldLoadADocumentAsAString() throws Exception {

        final URL url = new File("src/test/resources/transformer/test-spec1.json").toURI().toURL();

        final String json = fileContentsAsStringLoader.readFileContents(url);

        assertThat(json, is(notNullValue()));

        with(json)
                .assertThat("$.operations[0].operation", is("shift1"))
                .assertThat("$.operations[0].spec.cakeId", is("cakeId1"))
                .assertThat("$.operations[0].spec.recipeId", is("recipeId1"))
                .assertThat("$.operations[0].spec.deliveryDate", is("deliveryDate1"));
    }

    @Test
    public void shouldFailIfReadingTheFileThrowsAnIOException() throws Exception {

        final URL url = new File("this/file/does/not/exist.json").toURI().toURL();

        final UnifiedSearchException unifiedSearchException = assertThrows(UnifiedSearchException.class, () ->
                fileContentsAsStringLoader.readFileContents(url)
        );

        assertThat(unifiedSearchException.getMessage(), startsWith("Failed to read file contents from 'file:"));
        assertThat(unifiedSearchException.getMessage(), endsWith("this/file/does/not/exist.json'"));
    }
}

