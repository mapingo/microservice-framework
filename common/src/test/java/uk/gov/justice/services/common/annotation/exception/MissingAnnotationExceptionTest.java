package uk.gov.justice.services.common.annotation.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;

import uk.gov.justice.services.core.annotation.exception.MissingAnnotationException;

import org.junit.Test;

public class MissingAnnotationExceptionTest {

    @Test
    public void shouldCreateInstanceOfMissingAnnotationExceptionWithMessage() throws Exception {
        final MissingAnnotationException exception = new MissingAnnotationException("Test message");
        assertThat(exception.getMessage(), is("Test message"));
        assertThat(exception, instanceOf(RuntimeException.class));
    }
}