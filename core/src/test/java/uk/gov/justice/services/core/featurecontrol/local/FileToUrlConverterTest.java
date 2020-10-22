package uk.gov.justice.services.core.featurecontrol.local;


import static java.net.URI.create;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class FileToUrlConverterTest {

    @InjectMocks
    private FileToUrlConverter fileToUrlConverter;

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldNameConvertAFileToUrl() throws Exception {

        final String fileName = getClass().getClassLoader().getResource("feature-control.yaml").getFile();

        final URL url = fileToUrlConverter.toUrl(new File(fileName));

        assertThat(url.getPath(), is(fileName));
    }

    @Test
    public void shouldFailIfUriIsMalformed() throws Exception {

        final File file = mock(File.class);

        final String fileName = "/path/to/some/file/or.other";

        final URI uri = create("git://some-file");

        when(file.toURI()).thenReturn(uri);
        when(file.getAbsolutePath()).thenReturn(fileName);

        try {
            fileToUrlConverter.toUrl(file);
            fail();
        } catch (final UncheckedIOException expected) {
            assertThat(expected.getCause(), is(instanceOf(MalformedURLException.class)));
            assertThat(expected.getMessage(), is("Failed to convert file '/path/to/some/file/or.other' to URL"));
        }
    }
}