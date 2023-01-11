package uk.gov.justice.services.core.producers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import uk.gov.justice.services.core.envelope.EnvelopeInspector;
import uk.gov.justice.services.core.envelope.EnvelopeValidator;
import uk.gov.justice.services.core.envelope.MediaTypeProvider;
import uk.gov.justice.services.core.envelope.RequestResponseEnvelopeValidator;
import uk.gov.justice.services.core.mapping.NameToMediaTypeConverter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RequestResponseEnvelopeValidatorFactoryTest {

    @Mock
    private NameToMediaTypeConverter nameToMediaTypeConverter;

    @Mock
    private MediaTypeProvider mediaTypeProvider;

    @Mock
    private EnvelopeInspector envelopeInspector;

    @Mock
    private EnvelopeValidatorFactory envelopeValidatorFactory;

    @InjectMocks
    private RequestResponseEnvelopeValidatorFactory requestResponseEnvelopeValidatorFactory;

    @Test
    public void shouldCreateNewRequestResponseEnvelopeValidatorFactory() throws Exception {

        final EnvelopeValidator envelopeValidator = mock(EnvelopeValidator.class);

        when(envelopeValidatorFactory.createNew()).thenReturn(envelopeValidator);

        final RequestResponseEnvelopeValidator requestResponseEnvelopeValidator = requestResponseEnvelopeValidatorFactory.createNew();

        assertThat(getValueOfField(requestResponseEnvelopeValidator, "envelopeValidator", EnvelopeValidator.class), is(envelopeValidator));
        assertThat(getValueOfField(requestResponseEnvelopeValidator, "nameToMediaTypeConverter", NameToMediaTypeConverter.class), is(nameToMediaTypeConverter));
        assertThat(getValueOfField(requestResponseEnvelopeValidator, "mediaTypeProvider", MediaTypeProvider.class), is(mediaTypeProvider));
        assertThat(getValueOfField(requestResponseEnvelopeValidator, "envelopeInspector", EnvelopeInspector.class), is(envelopeInspector));
    }
}