package uk.gov.justice.services.messaging.context;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import uk.gov.justice.services.messaging.exception.InvalidNameException;

import org.junit.jupiter.api.Test;

public class ContextNameTest {

    @Test
    public void shouldReturnContextName() {
        assertThat(ContextName.fromName("test-context.command.test-command"), equalTo("test-context"));
    }

    @Test
    public void shouldThrowExceptionWithInvalidActionName() {
        assertThrows(InvalidNameException.class, () -> ContextName.fromName("test-context-commands-test-command"));
    }

}