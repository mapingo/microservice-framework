package uk.gov.justice.services.core.producers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import uk.gov.justice.services.core.envelope.EnvelopeValidationExceptionHandler;
import uk.gov.justice.services.core.envelope.EnvelopeValidator;
import uk.gov.justice.services.core.json.JsonSchemaValidator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnvelopeValidatorFactoryTest {

    @Mock
    private JsonSchemaValidator jsonSchemaValidator;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EnvelopeValidationExceptionHandler envelopeValidationExceptionHandler;

    @InjectMocks
    private EnvelopeValidatorFactory envelopeValidatorFactory;

    @Test
    public void shouldCreateNewEnvelopeValidator() throws Exception {

        final EnvelopeValidator envelopeValidator = envelopeValidatorFactory.createNew();

        assertThat(getValueOfField(envelopeValidator, "jsonSchemaValidator", JsonSchemaValidator.class), is(jsonSchemaValidator));
        assertThat(getValueOfField(envelopeValidator, "objectMapper", ObjectMapper.class), is(objectMapper));
        assertThat(getValueOfField(envelopeValidator, "envelopeValidationExceptionHandler", EnvelopeValidationExceptionHandler.class), is(envelopeValidationExceptionHandler));
    }
}