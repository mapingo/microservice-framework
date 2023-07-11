package uk.gov.justice.services.adapters.rest.generator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.raml.model.ActionType.HEAD;
import static org.raml.model.ActionType.OPTIONS;
import static org.raml.model.ActionType.TRACE;
import static uk.gov.justice.services.generators.test.utils.builder.HttpActionBuilder.httpActionWithDefaultMapping;
import static uk.gov.justice.services.generators.test.utils.builder.RamlBuilder.raml;
import static uk.gov.justice.services.generators.test.utils.builder.ResourceBuilder.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.raml.model.ActionType;
import org.raml.model.Raml;

public class JaxRsInterfaceGeneratorTest {

    private JaxRsInterfaceGenerator jaxRsInterfaceGenerator;

    @BeforeEach
    public void setup() {
        jaxRsInterfaceGenerator = new JaxRsInterfaceGenerator();
    }

    @Test
    public void shouldThrowExceptionIfActionTypeIsHEAD() throws Exception {
        assertThrows(IllegalStateException.class, () -> jaxRsInterfaceGenerator.generateFor(singleResourceWithActionType(HEAD)));
    }

    @Test
    public void shouldThrowExceptionIfActionTypeIsOPTIONS() throws Exception {
        assertThrows(IllegalStateException.class, () -> jaxRsInterfaceGenerator.generateFor(singleResourceWithActionType(OPTIONS)));
    }

    @Test
    public void shouldThrowExceptionIfActionTypeIsTRACE() throws Exception {
        assertThrows(IllegalStateException.class, () -> jaxRsInterfaceGenerator.generateFor(singleResourceWithActionType(TRACE)));
    }

    private Raml singleResourceWithActionType(final ActionType actionType) {
        return raml()
                .with(resource()
                        .with(httpActionWithDefaultMapping(actionType))).build();
    }
}