package uk.gov.justice.services.metrics.servlet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

import javax.servlet.annotation.WebListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the{@link HealthCheckServletContextListener} class.
 */
public class HealthCheckServletContextListenerTest {

    private HealthCheckServletContextListener listener;

    @BeforeEach
    public void setup() {
        listener = new HealthCheckServletContextListener();
    }

    @Test
    public void shouldReturnAHealthCheckRegistry() throws Exception {
        assertThat(listener.getHealthCheckRegistry(), notNullValue());
    }

    @Test
    public void shouldBeAWebListener() {
        WebListener annotation = listener.getClass().getAnnotation(WebListener.class);
        assertThat(annotation, notNullValue());
    }
}