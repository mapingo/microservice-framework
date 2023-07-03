package uk.gov.justice.services.metrics.servlet;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codahale.metrics.MetricRegistry;
import org.junit.jupiter.api.Test;

public class MetricsServletContextListenerTest {

    @Test
    public void shouldReturnMetricsRegistry() throws Exception {

        MetricRegistry registry = new MetricRegistry();
        MetricsServletContextListener listener = new MetricsServletContextListener();
        listener.metricRegistry = registry;

        assertThat(listener.getMetricRegistry(), is(registry));
    }
}