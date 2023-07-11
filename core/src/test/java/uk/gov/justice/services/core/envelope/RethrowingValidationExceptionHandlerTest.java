package uk.gov.justice.services.core.envelope;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;


public class RethrowingValidationExceptionHandlerTest {

    @Test
    public void shouldThrowHandlerException() throws Exception {

        final EnvelopeValidationException envelopeValidationException = assertThrows(EnvelopeValidationException.class, () ->
                new RethrowingValidationExceptionHandler().handle(new EnvelopeValidationException("some message"))
        );

        assertThat(envelopeValidationException.getMessage(), is("some message"));
    }
}
