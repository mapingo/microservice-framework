package uk.gov.justice.services.metrics.interceptor;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.core.interceptor.InterceptorChain;
import uk.gov.justice.services.core.interceptor.InterceptorContext;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TotalActionMetricsInterceptorTest {

    @Mock
    private MetricRegistry metricsRegistry;

    @Mock
    private InterceptorChain interceptorChain;

    @Mock
    private Timer timer;

    @Mock
    private Timer.Context timerContext;

    @Mock
    private InterceptorContext interceptorContext;

    @InjectMocks
    private TotalActionMetricsInterceptor interceptor;

    @Test
    public void shouldGetTimerFromRegistryByContextName() {

        when(interceptorContext.getComponentName()).thenReturn("someCtxNameABC");
        when(metricsRegistry.timer("someCtxNameABC.action.total")).thenReturn(timer);
        when(timer.time()).thenReturn(timerContext);

        interceptor.process(interceptorContext, interceptorChain);

        verify(metricsRegistry).timer("someCtxNameABC.action.total");

    }

    @Test
    public void shouldStartAndStopTimer() throws Exception {

        when(metricsRegistry.timer(anyString())).thenReturn(timer);
        when(timer.time()).thenReturn(timerContext);

        interceptor.process(interceptorContext, interceptorChain);

        final InOrder inOrder = inOrder(timer, interceptorChain, timerContext);
        inOrder.verify(timer).time();
        inOrder.verify(interceptorChain).processNext(interceptorContext);
        inOrder.verify(timerContext).stop();

    }
}