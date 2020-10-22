package uk.gov.justice.services.core.handler.registry;

import static java.lang.String.format;
import static uk.gov.justice.services.core.handler.Handlers.handlerMethodsFrom;

import uk.gov.justice.services.core.annotation.Handles;
import uk.gov.justice.services.core.featurecontrol.FeatureControlAnnotationFinder;
import uk.gov.justice.services.core.handler.HandlerMethod;
import uk.gov.justice.services.core.handler.exception.MissingHandlerException;
import uk.gov.justice.services.core.handler.registry.exception.DuplicateHandlerException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

/**
 * Service for storing a map of which command handlers handle which commands.
 */

public class HandlerRegistry {

    private final Map<String, HandlerMethod> handlerMethods;

    private final Logger logger;
    private final FeatureControlAnnotationFinder featureControlAnnotationFinder;

    public HandlerRegistry(final Logger logger, final FeatureControlAnnotationFinder featureControlAnnotationFinder) {
        this.logger = logger;
        this.featureControlAnnotationFinder = featureControlAnnotationFinder;
        handlerMethods = new ConcurrentHashMap<>();
    }

    /**
     * Gets the correct {@link HandlerMethod} for the specified actionName
     *
     * @param actionName
     * @return The mapped {@link HandlerMethod}
     */
    public HandlerMethod get(final String actionName) {
        final HandlerMethod handlerMethod = handlerMethods.getOrDefault(actionName, handlerMethods.get("*"));

        if (handlerMethod != null) {
            return handlerMethod;
        } else {

            logger.error("Handler methods: '" + handlerMethods + "'");
            throw new MissingHandlerException(
                    format("No handler registered to handle action %s", actionName));
        }
    }

    /**
     * Registers a handler instance.
     *
     * @param handlerInstance handler instance to be registered.
     */
    public void register(final Object handlerInstance) {
        handlerMethodsFrom(handlerInstance).forEach(method -> register(handlerInstance, method));
    }

    /**
     * Registers handler instance and method.
     *
     * @param handler handler instance to register.
     * @param method  handler method to register.
     */
    private void register(final Object handler, final Method method) {

        final List<String> featureNames = featureControlAnnotationFinder.findAnnotatedFeatures(method);

        if (featureNames.size() > 0) {
            logger.info(format("FeatureControl found on '%s.%s(...)': %s", method.getDeclaringClass().getSimpleName(), method.getName(), featureNames));
        }

        final HandlerMethod newHandlerMethod = new HandlerMethod(handler, method, method.getReturnType(), featureNames);
        final String name = method.getAnnotation(Handles.class).value();
        if (isDuplicate(newHandlerMethod, name)) {
            throw new DuplicateHandlerException(
                    format("Can't register %s because a command handler method %s has " +
                            "already been registered for %s ", newHandlerMethod, handlerMethods.get(name), name));
        }

        logger.debug("Registering handler {}, {}", name, newHandlerMethod.toString());

        if (newHandlerMethod.isDirect()) {
            handlerMethods.put(name, newHandlerMethod);
        } else {
            handlerMethods.putIfAbsent(name, newHandlerMethod);
        }
    }

    private boolean isDuplicate(final HandlerMethod newHandlerMethod, final String name) {
        return handlerMethods.containsKey(name) && newHandlerMethod.isDirect() == handlerMethods.get(name).isDirect();
    }
}
