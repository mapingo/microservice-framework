package uk.gov.justice.services.core.envelope;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Test;


public class RethrowingValidationExceptionHandlerTest {

    @Test
    public void shouldThrowHandlerException() throws Exception {

        final EnvelopeValidationException envelopeValidationException = assertThrows(EnvelopeValidationException.class, () ->
                new RethrowingValidationExceptionHandler().handle(new EnvelopeValidationException("some message"))
        );

        assertThat(envelopeValidationException.getMessage(), is("some message"));
    }
}
