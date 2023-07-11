package uk.gov.justice.services.core.envelope;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EnvelopeValidationExceptionHandlerProducerTest {

    @InjectMocks
    private EnvelopeValidationExceptionHandlerProducer producer;

    @Test
    public void shouldProduceRethrowingHandler() {
        producer.handlerClass = "uk.gov.justice.services.core.envelope.RethrowingValidationExceptionHandler";
        assertThat(producer.envelopeValidationExceptionHandler(), instanceOf(RethrowingValidationExceptionHandler.class));
    }

    @Test
    public void shouldProduceLoggingHandler() {
        producer.handlerClass = "uk.gov.justice.services.core.envelope.LoggingValidationExceptionHandler";
        assertThat(producer.envelopeValidationExceptionHandler(), instanceOf(LoggingValidationExceptionHandler.class));
    }

    @Test
    public void shouldThrowExceptionIfClassDoesNotExist() {
        producer.handlerClass = "uk.gov.justice.services.core.envelope.NonExisting";

        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                producer.envelopeValidationExceptionHandler()
        );

        assertThat(illegalArgumentException.getMessage(), is("Could not instantiate validation exception handler."));
    }
}