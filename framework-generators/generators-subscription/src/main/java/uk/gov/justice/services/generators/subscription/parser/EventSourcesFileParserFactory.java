package uk.gov.justice.services.generators.subscription.parser;

import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;
import uk.gov.justice.services.generators.commons.helper.PathToUrlResolver;
import uk.gov.justice.services.yaml.YamlFileValidator;
import uk.gov.justice.services.yaml.YamlParser;
import uk.gov.justice.services.yaml.YamlSchemaLoader;
import uk.gov.justice.services.yaml.YamlToJsonObjectConverter;
import uk.gov.justice.subscription.EventSourcesParser;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A factory class for creating EventSourcesFileParser
 */
public class EventSourcesFileParserFactory {

    public EventSourcesFileParser create() {

        final YamlParser yamlParser = new YamlParser();
        final YamlSchemaLoader yamlSchemaLoader = new YamlSchemaLoader();
        final ObjectMapper objectMapper = new ObjectMapperProducer().objectMapper();
        final YamlFileValidator yamlFileValidator = new YamlFileValidator(new YamlToJsonObjectConverter(yamlParser, objectMapper), yamlSchemaLoader);

        final EventSourcesParser eventSourcesParser = new EventSourcesParser(yamlParser, yamlFileValidator);
        final PathToUrlResolver pathToUrlResolver = new PathToUrlResolver();
        final EventSourceYamlClasspathFinder eventSourceYamlClasspathFinder = new EventSourceYamlClasspathFinder();

        final EventSourcesFileParser eventSourcesFileParser = new EventSourcesFileParser(eventSourcesParser, pathToUrlResolver, eventSourceYamlClasspathFinder);

        return eventSourcesFileParser;
    }
}
