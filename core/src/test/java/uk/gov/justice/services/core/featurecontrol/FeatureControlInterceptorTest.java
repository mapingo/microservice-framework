package uk.gov.justice.services.core.featurecontrol;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.core.annotation.ServiceComponentLocation.LOCAL;

import uk.gov.justice.services.common.exception.ForbiddenRequestException;
import uk.gov.justice.services.core.handler.HandlerMethod;
import uk.gov.justice.services.core.handler.registry.HandlerRegistry;
import uk.gov.justice.services.core.handler.registry.HandlerRegistryCache;
import uk.gov.justice.services.core.interceptor.InterceptorChain;
import uk.gov.justice.services.core.interceptor.InterceptorContext;
import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.messaging.Metadata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;


@RunWith(MockitoJUnitRunner.class)
public class FeatureControlInterceptorTest {

    @Mock
    private FeatureControlGuard featureControlGuard;

    @Mock
    private HandlerRegistryCache handlerRegistryCache;

    @Mock
    private Logger logger;

    @InjectMocks
    private FeatureControlInterceptor featureControlInterceptor;

    @Test
    public void shouldProcessNextInInterceptorChainIfAllFeaturesAreEnabled() throws Exception {

        final String actionName = "action-name";
        final String componentName = "COMMAND_API";

        final HandlerRegistry handlerRegistry = mock(HandlerRegistry.class);
        final InterceptorContext interceptorContext = mock(InterceptorContext.class);
        final InterceptorChain interceptorChain = mock(InterceptorChain.class);

        final JsonEnvelope inputEnvelope = mock(JsonEnvelope.class);
        final Metadata metadata = mock(Metadata.class);
        final HandlerMethod handlerMethod = mock(HandlerMethod.class);

        when(interceptorContext.inputEnvelope()).thenReturn(inputEnvelope);
        when(inputEnvelope.metadata()).thenReturn(metadata);
        when(interceptorContext.getComponentName()).thenReturn(componentName);
        when(metadata.name()).thenReturn(actionName);

        when(handlerRegistryCache.handlerRegistryFor(componentName, LOCAL)).thenReturn(handlerRegistry);
        when(handlerRegistry.get(actionName)).thenReturn(handlerMethod);
        when(handlerMethod.getFeatureNames()).thenReturn(asList("feature-1", "feature-2", "feature-3"));

        when(featureControlGuard.isFeatureEnabled("feature-1")).thenReturn(true);
        when(featureControlGuard.isFeatureEnabled("feature-2")).thenReturn(true);
        when(featureControlGuard.isFeatureEnabled("feature-3")).thenReturn(true);

        featureControlInterceptor.process(interceptorContext, interceptorChain);

        verify(interceptorChain).processNext(interceptorContext);
        verify(logger).info("'feature-1' feature is enabled 'true' for action 'action-name' in COMMAND_API");
        verify(logger).info("'feature-2' feature is enabled 'true' for action 'action-name' in COMMAND_API");
        verify(logger).info("'feature-3' feature is enabled 'true' for action 'action-name' in COMMAND_API");

    }
    @Test
    public void shouldFailIfAnyFeatureIsDisabled() throws Exception {

        final String actionName = "action-name";
        final String componentName = "COMMAND_API";

        final HandlerRegistry handlerRegistry = mock(HandlerRegistry.class);
        final InterceptorContext interceptorContext = mock(InterceptorContext.class);
        final InterceptorChain interceptorChain = mock(InterceptorChain.class);

        final JsonEnvelope inputEnvelope = mock(JsonEnvelope.class);
        final Metadata metadata = mock(Metadata.class);
        final HandlerMethod handlerMethod = mock(HandlerMethod.class);

        when(interceptorContext.getComponentName()).thenReturn(componentName);
        when(interceptorContext.inputEnvelope()).thenReturn(inputEnvelope);
        when(inputEnvelope.metadata()).thenReturn(metadata);
        when(metadata.name()).thenReturn(actionName);

        when(handlerRegistryCache.handlerRegistryFor(componentName, LOCAL)).thenReturn(handlerRegistry);
        when(handlerRegistry.get(actionName)).thenReturn(handlerMethod);
        when(handlerMethod.getFeatureNames()).thenReturn(asList("feature-1", "feature-2", "feature-3"));

        when(featureControlGuard.isFeatureEnabled("feature-1")).thenReturn(true);
        when(featureControlGuard.isFeatureEnabled("feature-2")).thenReturn(true);
        when(featureControlGuard.isFeatureEnabled("feature-3")).thenReturn(false);

        try {
            featureControlInterceptor.process(interceptorContext, interceptorChain);
            fail();
        } catch (final DisabledFeatureException expected) {
            assertThat(expected, is(instanceOf(ForbiddenRequestException.class)));
            assertThat(expected.getMessage(), is("The feature 'feature-3' is disabled for the action 'action-name' in COMMAND_API"));
        }

        verify(logger).info("'feature-1' feature is enabled 'true' for action 'action-name' in COMMAND_API");
        verify(logger).info("'feature-2' feature is enabled 'true' for action 'action-name' in COMMAND_API");
        verify(logger).info("'feature-3' feature is enabled 'false' for action 'action-name' in COMMAND_API");

        verifyNoInteractions(interceptorChain);
    }
}