package uk.gov.justice.services.core.extension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the {@link EventFoundEvent} class.
 */
@ExtendWith(MockitoExtension.class)
public class EventFoundEventTest {

    private final static String EVENT_NAME = "test";

    private final static Class<?> CLASS = Object.class;

    private EventFoundEvent event;

    @BeforeEach
    public void setup() {
        event = new EventFoundEvent(CLASS, EVENT_NAME);
    }

    @Test
    public void shouldReturnEventName() {
        assertThat(event.getEventName(), equalTo(EVENT_NAME));
    }

    @Test
    public void shouldReturnClass() {
        assertThat(event.getClazz(), equalTo(CLASS));
    }
}
