package uk.gov.justice.services.adapter.rest.parameter;

import org.owasp.encoder.Encode;

public class HttpParameterEncoder {

    public String encodeForHtmlAttribute(final String httpParameter) {
        return Encode.forHtmlAttribute(httpParameter);
    }

    public String encodeForJavaScript(final String httpParameter) {
        return Encode.forJavaScript(httpParameter);
    }
}
