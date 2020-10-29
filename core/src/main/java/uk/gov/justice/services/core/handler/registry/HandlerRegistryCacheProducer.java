package uk.gov.justice.services.core.handler.registry;

import uk.gov.justice.services.common.util.LazyValue;
import uk.gov.justice.services.core.featurecontrol.FeatureControlAnnotationFinder;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HandlerRegistryCacheProducer {

    private LazyValue lazyValue = new LazyValue();

    @Inject
    private FeatureControlAnnotationFinder featureControlAnnotationFinder;

    @Produces
    public HandlerRegistryCache handlerRegistryCache() {

        return lazyValue.createIfAbsent(() -> new HandlerRegistryCache(featureControlAnnotationFinder));
    }
}
