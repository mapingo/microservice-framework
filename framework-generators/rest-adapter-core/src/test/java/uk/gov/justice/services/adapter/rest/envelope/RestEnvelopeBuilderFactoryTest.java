package uk.gov.justice.services.adapter.rest.envelope;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link RestEnvelopeBuilderFactory} class.
 */
public class RestEnvelopeBuilderFactoryTest {

    private RestEnvelopeBuilderFactory factory;

    @BeforeEach
    public void setup() {
        factory = new RestEnvelopeBuilderFactory();
    }

    @Test
    public void shouldReturnEnvelopeBuilder() throws Exception {
        RestEnvelopeBuilder envelopeBuilder = factory.builder();

        assertThat(envelopeBuilder, not(nullValue()));
    }
}
