package uk.gov.justice.services.generators.subscription.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.*;

import uk.gov.justice.maven.generator.io.files.parser.FileParser;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class EventSourcesFileParserFactoryTest {

    @Test
    void create() {
        final EventSourcesFileParser parser = new EventSourcesFileParserFactory().create();
        assertThat(parser, is(notNullValue()));
    }
}