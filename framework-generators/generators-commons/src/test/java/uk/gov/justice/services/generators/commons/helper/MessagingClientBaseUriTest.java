package uk.gov.justice.services.generators.commons.helper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Unit test for the {@link MessagingClientBaseUri} class.
 */
public class MessagingClientBaseUriTest {

    @Test
    public void shouldReturnClassName() {
        assertThat(new MessagingClientBaseUri("message://event/listener/message/system").toClassName(), is("EventListenerMessageSystem"));
    }

    @Test
    public void shouldThrowExceptionForInvalidUri() {
        assertThrows(IllegalArgumentException.class, () -> new MessagingClientBaseUri("blah").toClassName());
    }
}
