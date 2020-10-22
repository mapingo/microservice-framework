package uk.gov.justice.services.core.handler.registry;

import static org.slf4j.LoggerFactory.getLogger;

import uk.gov.justice.services.core.annotation.Component;
import uk.gov.justice.services.core.annotation.ServiceComponentLocation;
import uk.gov.justice.services.core.featurecontrol.FeatureControlAnnotationFinder;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

/**
 * Cache of all {@link HandlerRegistry}s keyed by their {@link Component} name and the
 * {@link ServiceComponentLocation}. This class is a Singleton. This is manged by its producer
 * {@link HandlerRegistryCacheProducer}
 */
public class HandlerRegistryCache {

    private FeatureControlAnnotationFinder featureControlAnnotationFinder;

    public HandlerRegistryCache(final FeatureControlAnnotationFinder featureControlAnnotationFinder) {
        this.featureControlAnnotationFinder = featureControlAnnotationFinder;
    }

    private final Map<HandlerRegistryKey, HandlerRegistry> handlerRegistryCache = new ConcurrentHashMap<>();

    public HandlerRegistry handlerRegistryFor(
            final String serviceComponent,
            final ServiceComponentLocation serviceComponentLocation) {

        return handlerRegistryCache.computeIfAbsent(
                new HandlerRegistryKey(serviceComponent, serviceComponentLocation),
                this::newHandlerRegistry
        );
    }

    private HandlerRegistry newHandlerRegistry(final HandlerRegistryKey handlerRegistryKey) {
        return new HandlerRegistry(
                getLogger(HandlerRegistry.class),
                featureControlAnnotationFinder);
    }

    private static class HandlerRegistryKey {

        final String serviceComponent;
        final ServiceComponentLocation serviceComponentLocation;

        public HandlerRegistryKey(
                final String serviceComponent,
                final ServiceComponentLocation serviceComponentLocation) {
            this.serviceComponent = serviceComponent;
            this.serviceComponentLocation = serviceComponentLocation;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (!(o instanceof HandlerRegistryKey)) return false;
            final HandlerRegistryKey that = (HandlerRegistryKey) o;
            return Objects.equals(serviceComponent, that.serviceComponent) &&
                    serviceComponentLocation == that.serviceComponentLocation;
        }

        @Override
        public int hashCode() {
            return Objects.hash(serviceComponent, serviceComponentLocation);
        }
    }
}
