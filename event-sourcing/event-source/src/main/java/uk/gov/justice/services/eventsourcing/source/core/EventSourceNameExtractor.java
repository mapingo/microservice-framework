package uk.gov.justice.services.eventsourcing.source.core;

import static uk.gov.justice.services.eventsourcing.source.core.annotation.EventSourceName.DEFAULT_EVENT_SOURCE_NAME;

import uk.gov.justice.services.eventsourcing.source.core.annotation.EventSourceName;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.InjectionPoint;

@ApplicationScoped
public class EventSourceNameExtractor {

    public String getEventSourceNameFromQualifier(final InjectionPoint injectionPoint) {

        return injectionPoint.getQualifiers().stream()
                .filter(annotation -> annotation.annotationType().isAssignableFrom(EventSourceName.class))
                .map(annotation -> ((EventSourceName) annotation).value())
                .findFirst()
                .orElse(DEFAULT_EVENT_SOURCE_NAME);
    }
}