package uk.gov.justice.services.core.handler.registry.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;

public class InvalidHandlerExceptionTest {

    @Test
    public void shouldCreateInstanceOfInvalidHandlerExceptionnWithMessage() throws Exception {
        final InvalidHandlerException exception = new InvalidHandlerException("Test message");
        assertThat(exception.getMessage(), is("Test message"));
        assertThat(exception, instanceOf(RuntimeException.class));
    }
}