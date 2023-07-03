package uk.gov.justice.services.core.envelope;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class LoggingValidationExceptionHandlerTest {

    @Mock
    private Logger logger;

    @InjectMocks
    LoggingValidationExceptionHandler exceptionHandler;

    @Test
    public void shouldLogException() throws Exception {

        final EnvelopeValidationException exception = new EnvelopeValidationException("");
        exceptionHandler.handle(exception);

        verify(logger).warn("Message validation failed", exception);

    }
}