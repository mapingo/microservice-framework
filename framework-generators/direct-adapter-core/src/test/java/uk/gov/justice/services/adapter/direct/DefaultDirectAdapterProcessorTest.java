package uk.gov.justice.services.adapter.direct;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.test.utils.core.messaging.JsonEnvelopeBuilder.envelope;
import static uk.gov.justice.services.test.utils.core.messaging.MetadataBuilderFactory.metadataWithRandomUUID;

import uk.gov.justice.services.core.interceptor.InterceptorContext;
import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.Optional;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultDirectAdapterProcessorTest {

    @Mock
    private Function<InterceptorContext, Optional<JsonEnvelope>> interceptorChainFunction;

    private DirectAdapterProcessor directAdapterProcessor = new DefaultDirectAdapterProcessor();

    @Test
    public void shouldApplyInterceptorChainFunctionToTheIncomingTheEnvelope() throws Exception {

        when(interceptorChainFunction.apply(any(InterceptorContext.class))).thenReturn(Optional.of(envelope().build()));

        final JsonEnvelope incomingEnvelope = envelope().with(metadataWithRandomUUID("action1")).build();

        directAdapterProcessor.process(incomingEnvelope, interceptorChainFunction);

        ArgumentCaptor<InterceptorContext> interceptorContextCaptor = ArgumentCaptor.forClass(InterceptorContext.class);
        verify(interceptorChainFunction).apply(interceptorContextCaptor.capture());

        assertThat(interceptorContextCaptor.getValue().inputEnvelope(), is(incomingEnvelope));

    }

    @Test
    public void shouldReturnEnvelopeReturnedByInterceptorChainFunction() throws Exception {

        final JsonEnvelope returnedEnvelope = envelope().build();

        when(interceptorChainFunction.apply(any(InterceptorContext.class))).thenReturn(Optional.of(returnedEnvelope));

        final JsonEnvelope result = directAdapterProcessor.process(envelope().with(metadataWithRandomUUID("action2")).build(), interceptorChainFunction);
        assertThat(result, is(returnedEnvelope));
    }

    @Test
    public void shouldThrowExceptionIfInterceptorChainReturnedEmptyEnvelope() throws Exception {

        when(interceptorChainFunction.apply(any(InterceptorContext.class))).thenReturn(Optional.empty());

        final JsonEnvelope jsonEnvelope = envelope().with(metadataWithRandomUUID("action2")).build();

        assertThrows(IllegalStateException.class, () -> directAdapterProcessor.process(jsonEnvelope, interceptorChainFunction));
    }

}