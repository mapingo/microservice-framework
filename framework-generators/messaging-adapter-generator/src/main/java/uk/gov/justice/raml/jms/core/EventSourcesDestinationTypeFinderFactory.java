package uk.gov.justice.raml.jms.core;

import static org.slf4j.LoggerFactory.getLogger;

import uk.gov.justice.services.generators.subscription.parser.EventSourcesFileParserFactory;
import uk.gov.justice.services.generators.subscription.parser.JmsUriToDestinationConverter;
import uk.gov.justice.subscription.jms.core.JmsUriToDestinationTypeConverter;


public class EventSourcesDestinationTypeFinderFactory {

    public EventSourcesDestinationTypeFinder create() {
        return new EventSourcesDestinationTypeFinder(
                new JmsUriToDestinationTypeConverter(),
                new JmsUriToDestinationConverter(),
                new EventSourcesFileParserFactory(),
                getLogger(EventSourcesDestinationTypeFinder.class)
        );
    }
}
