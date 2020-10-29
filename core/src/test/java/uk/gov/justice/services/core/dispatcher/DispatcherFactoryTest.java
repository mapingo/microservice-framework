package uk.gov.justice.services.core.dispatcher;

import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.core.annotation.ServiceComponentLocation.LOCAL;

import uk.gov.justice.services.core.annotation.ServiceComponentLocation;
import uk.gov.justice.services.core.handler.registry.HandlerRegistry;
import uk.gov.justice.services.core.handler.registry.HandlerRegistryCache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DispatcherFactoryTest {

    private static final String HANDLER_REGISTRY_FIELD_NAME = "handlerRegistry";
    private static final String LOGGER_FIELD_NAME = "logger";

    @Mock
    private HandlerRegistryCache handlerRegistryCache;

    @InjectMocks
    private DispatcherFactory dispatcherFactory;

    @Test
    public void shouldCreateNewDispatcher() throws Exception {
        final String serviceComponent = "COMMAND_API";
        final ServiceComponentLocation location = LOCAL;

        final HandlerRegistry handlerRegistry = mock(HandlerRegistry.class);

        when(handlerRegistryCache.handlerRegistryFor(serviceComponent, location)).thenReturn(handlerRegistry);

        assertThat(dispatcherFactory.createNew(serviceComponent, location), instanceOf(Dispatcher.class));
    }

    @Test
    public void shouldCreateANewHandlerRegistryForEachDispatcherInstance() throws Exception {

        final String serviceComponentName_1 = "COMMAND_API";
        final String serviceComponentName_2 = "COMMAND_HANDLER";
        final ServiceComponentLocation location = LOCAL;

        final HandlerRegistry handlerRegistry_1 = mock(HandlerRegistry.class);
        final HandlerRegistry handlerRegistry_2 = mock(HandlerRegistry.class);

        when(handlerRegistryCache.handlerRegistryFor(serviceComponentName_1, location)).thenReturn(handlerRegistry_1);
        when(handlerRegistryCache.handlerRegistryFor(serviceComponentName_2, location)).thenReturn(handlerRegistry_2);

        final Dispatcher dispatcher1 = dispatcherFactory.createNew(serviceComponentName_1, location);
        final Dispatcher dispatcher2 = dispatcherFactory.createNew(serviceComponentName_2, location);

        assertThat(getHandlerRegistryFrom(dispatcher1), is(sameInstance(handlerRegistry_1)));
        assertThat(getHandlerRegistryFrom(dispatcher2), is(sameInstance(handlerRegistry_2)));
    }

    private HandlerRegistry getHandlerRegistryFrom(final Dispatcher dispatcher) throws IllegalAccessException {
        return (HandlerRegistry) readField(dispatcher, HANDLER_REGISTRY_FIELD_NAME, true);
    }
}