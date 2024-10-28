package uk.gov.justice.raml.jms.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

class EventSourcesDestinationTypeFinderFactoryTest {

    private final EventSourcesDestinationTypeFinderFactory factory = new EventSourcesDestinationTypeFinderFactory();

    @Test
    void shouldCreate() {
        assertThat(factory.create(), is(notNullValue()));
    }
}