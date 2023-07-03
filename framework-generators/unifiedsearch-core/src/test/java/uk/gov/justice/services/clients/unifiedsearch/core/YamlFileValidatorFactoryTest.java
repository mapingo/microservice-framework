package uk.gov.justice.services.clients.unifiedsearch.core;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import uk.gov.justice.services.yaml.YamlFileValidator;

import org.junit.jupiter.api.Test;

public class YamlFileValidatorFactoryTest {


    @Test
    public void shouldCreateYamlFileValidator() {
        final YamlFileValidator yamlFileValidator = new YamlFileValidatorFactory().create();

        assertThat(yamlFileValidator, is(notNullValue()));
        assertThat(yamlFileValidator , instanceOf(YamlFileValidator.class));
    }
}