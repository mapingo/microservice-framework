package uk.gov.justice.services.generators.commons.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.core.annotation.Component.EVENT_LISTENER;

import org.junit.jupiter.api.Test;

public class CommonGeneratorPropertiesTest {

    @Test
    public void shouldGetServiceComponent() {
        final CommonGeneratorProperties commonGeneratorProperties = (CommonGeneratorProperties) new GeneratorPropertiesFactory()
                .withServiceComponentOf(EVENT_LISTENER);

        assertThat(commonGeneratorProperties.getServiceComponent(), is(EVENT_LISTENER));
    }

    @Test
    public void shouldGetCustomMDBPool() {
        final CommonGeneratorProperties commonGeneratorProperties = (CommonGeneratorProperties) new GeneratorPropertiesFactory()
                .withCustomMDBPool();

        assertThat(commonGeneratorProperties.getCustomMDBPool(), is("TRUE"));
    }
}