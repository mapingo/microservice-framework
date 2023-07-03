package uk.gov.justice.services.core.handler.registry;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.core.annotation.ServiceComponentLocation.LOCAL;
import static uk.gov.justice.services.core.annotation.ServiceComponentLocation.REMOTE;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import uk.gov.justice.services.core.featurecontrol.FeatureControlAnnotationFinder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class HandlerRegistryCacheTest {

    @Mock
    private FeatureControlAnnotationFinder featureControlAnnotationFinder;

    @InjectMocks
    private HandlerRegistryCache handlerRegistryCache;

    @Test
    public void shouldCreateAHandlerRegistry() throws Exception {

        final HandlerRegistry handlerRegistry = handlerRegistryCache.handlerRegistryFor("COMMAND_API", LOCAL);

        assertThat(getValueOfField(handlerRegistry, "logger", Logger.class).getName(), is(HandlerRegistry.class.getName()));
        assertThat(getValueOfField(handlerRegistry, "featureControlAnnotationFinder", FeatureControlAnnotationFinder.class), is(featureControlAnnotationFinder));
    }

    @Test
    public void shouldCreateANewHandlerRegistryForEachComponent() throws Exception {

        final HandlerRegistry commandApiHandlerRegistry = handlerRegistryCache.handlerRegistryFor("COMMAND_API", LOCAL);
        final HandlerRegistry eventListenerHandlerRegistry = handlerRegistryCache.handlerRegistryFor("EVENT_LISTENER", LOCAL);

        assertThat(commandApiHandlerRegistry, is(not(sameInstance(eventListenerHandlerRegistry))));
    }

    @Test
    public void shouldCreateANewHandlerRegistryForTheSameComponentButDifferentLocations() throws Exception {

        final HandlerRegistry localHandlerRegistry = handlerRegistryCache.handlerRegistryFor("COMMAND_API", LOCAL);
        final HandlerRegistry remoteHandlerRegistry = handlerRegistryCache.handlerRegistryFor("COMMAND_API", REMOTE);

        assertThat(localHandlerRegistry, is(not(sameInstance(remoteHandlerRegistry))));
    }

    @Test
    public void shouldCacheTheHandlerRegistryOnceCreated() throws Exception {

        final HandlerRegistry commandApiHandlerRegistry = handlerRegistryCache.handlerRegistryFor("COMMAND_API", LOCAL);
        final HandlerRegistry eventListenerHandlerRegistry = handlerRegistryCache.handlerRegistryFor("EVENT_LISTENER", LOCAL);

        assertThat(handlerRegistryCache.handlerRegistryFor("COMMAND_API", LOCAL), is(sameInstance(commandApiHandlerRegistry)));
        assertThat(handlerRegistryCache.handlerRegistryFor("COMMAND_API", LOCAL), is(sameInstance(commandApiHandlerRegistry)));
        assertThat(handlerRegistryCache.handlerRegistryFor("COMMAND_API", LOCAL), is(sameInstance(commandApiHandlerRegistry)));
        assertThat(handlerRegistryCache.handlerRegistryFor("COMMAND_API", LOCAL), is(sameInstance(commandApiHandlerRegistry)));

        assertThat(handlerRegistryCache.handlerRegistryFor("EVENT_LISTENER", LOCAL), is(sameInstance(eventListenerHandlerRegistry)));
        assertThat(handlerRegistryCache.handlerRegistryFor("EVENT_LISTENER", LOCAL), is(sameInstance(eventListenerHandlerRegistry)));
        assertThat(handlerRegistryCache.handlerRegistryFor("EVENT_LISTENER", LOCAL), is(sameInstance(eventListenerHandlerRegistry)));
        assertThat(handlerRegistryCache.handlerRegistryFor("EVENT_LISTENER", LOCAL), is(sameInstance(eventListenerHandlerRegistry)));
    }
}