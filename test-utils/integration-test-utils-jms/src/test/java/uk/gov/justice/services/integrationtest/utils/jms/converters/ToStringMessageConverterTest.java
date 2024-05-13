package uk.gov.justice.services.integrationtest.utils.jms.converters;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ToStringMessageConverterTest {

    private final ToStringMessageConverter toStringMessageConverter = new ToStringMessageConverter();

    @Test
    void verifyConvert() {
        final String message = "message";

        final String result = toStringMessageConverter.convert(message);

        assertThat(result, is(message));
    }
}