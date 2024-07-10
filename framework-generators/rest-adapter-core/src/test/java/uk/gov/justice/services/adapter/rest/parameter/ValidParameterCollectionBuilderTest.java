package uk.gov.justice.services.adapter.rest.parameter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.adapter.rest.exception.BadRequestException;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class ValidParameterCollectionBuilderTest {

    @Mock
    private HttpParameterEncoder httpParameterEncoder;

    @Mock
    private Logger logger;

    @InjectMocks
    private ValidParameterCollectionBuilder validParameterCollectionBuilder;


    @Test
    public void shouldReturnEmptyMap() throws Exception {
        assertThat(validParameterCollectionBuilder.parameters().size(), is(0));
    }

    @Test
    public void shouldReturnMapWithRequiredParameter() throws Exception {

        final String originalParameterValue = "ParameterValue";
        final String encodedParameterValue = "ParameterValue";

        when(httpParameterEncoder.encodeForHtmlAttribute(originalParameterValue)).thenReturn(encodedParameterValue);
        validParameterCollectionBuilder.putRequired("Name1", originalParameterValue, ParameterType.STRING);

        final Collection<Parameter> validParameters = validParameterCollectionBuilder.parameters();

        assertThat(validParameters.size(), is(1));
        assertThat(validParameters.iterator().next().getStringValue(), is(encodedParameterValue));
    }

    @Test
    public void shouldLogWaringIfParameterGetsEncodedToGuardAgainstCrossSiteScriptingAttack() throws Exception {

        final String originalParameterValue = "PossiblyEvilParameterValue";
        final String encodedParameterValue = "EncodedParameterValue";

        when(httpParameterEncoder.encodeForHtmlAttribute(originalParameterValue)).thenReturn(encodedParameterValue);
        validParameterCollectionBuilder.putRequired("Name1", originalParameterValue, ParameterType.STRING);

        final Collection<Parameter> validParameters = validParameterCollectionBuilder.parameters();

        assertThat(validParameters.size(), is(1));
        assertThat(validParameters.iterator().next().getStringValue(), is(encodedParameterValue));

        verify(logger).warn("SUSPICIOUS HTTP PARAMETER DETECTED: The http parameter 'Name1' was encoded to prevent cross site scripting attack. Original parameter value 'PossiblyEvilParameterValue' encoded as 'EncodedParameterValue'");
    }

    @Test
    public void shouldReturnMapWithNoOptionalParameterIfNullValue() throws Exception {
        validParameterCollectionBuilder.putOptional("OptionalName1", null, ParameterType.STRING);
        Collection<Parameter> validParameters = validParameterCollectionBuilder.parameters();
        assertThat(validParameters.size(), is(0));
    }

    @Test
    public void shouldReturnMapWithOptionalParameterIfValueSet() throws Exception {

        final String originalParameterValue = "OptionalValue1";
        final String encodedParameterValue = "OptionalValue1";

        when(httpParameterEncoder.encodeForHtmlAttribute(originalParameterValue)).thenReturn(encodedParameterValue);
        
        validParameterCollectionBuilder.putOptional("OptionalName1", originalParameterValue, ParameterType.STRING);

        Collection<Parameter> validParameters = validParameterCollectionBuilder.parameters();

        assertThat(validParameters.size(), is(1));
        assertThat(validParameters.iterator().next().getStringValue(), is(encodedParameterValue));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReturnMapWithMultipleRequiredAndOptionalParameters() throws Exception {
        final String originalParameterValue_1 = "Value1";
        final String originalParameterValue_2 = "OptionalValue2";
        final String originalParameterValue_3 = "1111";
        final String originalParameterValue_4 = "567";


        final String encodedParameterValue_1 = "Value1";
        final String encodedParameterValue_2 = "OptionalValue2";
        final String encodedParameterValue_3 = "1111";
        final String encodedParameterValue_4 = "567";

        when(httpParameterEncoder.encodeForHtmlAttribute(originalParameterValue_1)).thenReturn(encodedParameterValue_1);
        when(httpParameterEncoder.encodeForHtmlAttribute(originalParameterValue_2)).thenReturn(encodedParameterValue_2);
        when(httpParameterEncoder.encodeForHtmlAttribute(originalParameterValue_3)).thenReturn(encodedParameterValue_3);
        when(httpParameterEncoder.encodeForHtmlAttribute(originalParameterValue_4)).thenReturn(encodedParameterValue_4);


        validParameterCollectionBuilder
                .putRequired("paramName1", originalParameterValue_1, ParameterType.STRING)
                .putOptional("OptionalName1", null, ParameterType.STRING)
                .putOptional("OptionalName2", originalParameterValue_2, ParameterType.STRING)
                .putOptional("OptionalName3", originalParameterValue_3, ParameterType.NUMERIC)
                .putRequired("Name2", originalParameterValue_4, ParameterType.NUMERIC);

        Collection<Parameter> validParameters = validParameterCollectionBuilder.parameters();

        assertThat(validParameters, hasSize(4));
        assertThat(validParameters, hasItems(
                allOf(hasProperty("name", equalTo("paramName1")), hasProperty("stringValue", equalTo(encodedParameterValue_1))),
                allOf(hasProperty("name", equalTo("OptionalName2")), hasProperty("stringValue", equalTo(encodedParameterValue_2))),
                allOf(hasProperty("name", equalTo("OptionalName3")), hasProperty("numericValue", equalTo(BigDecimal.valueOf(1111)))),
                allOf(hasProperty("name", equalTo("Name2")), hasProperty("numericValue", equalTo(BigDecimal.valueOf(567))))
        ));

    }

    @Test
    public void shouldThrowExceptionIfRequiredParameterHasNullValue() throws Exception {

        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () ->
                validParameterCollectionBuilder
                        .putRequired("Name1", null, ParameterType.STRING)
                        .parameters()
        );

        assertThat(badRequestException.getMessage(), is("The required parameter Name1 has no value."));
    }

    @Test
    public void shouldThrowExceptionInCaseOfInvalidNumericParamValue() throws Exception {

        final String originalParameterValue = "NonNumeric";
        final String encodedParameterValue = "NonNumeric";

        when(httpParameterEncoder.encodeForHtmlAttribute(originalParameterValue)).thenReturn(encodedParameterValue);

        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () ->
                validParameterCollectionBuilder
                        .putRequired("param", originalParameterValue, ParameterType.NUMERIC)
                        .parameters()
        );
        
        assertThat(badRequestException.getMessage(), is("Invalid parameter value."));
    }

    @Test
    public void shouldThrowExceptionInCaseOfInvalidNumericParamValue2() throws Exception {

        final String originalParameterValue = "NonNumeric";
        final String encodedParameterValue = "NonNumeric";

        when(httpParameterEncoder.encodeForHtmlAttribute(originalParameterValue)).thenReturn(encodedParameterValue);

        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () ->
                validParameterCollectionBuilder
                        .putOptional("param", originalParameterValue, ParameterType.NUMERIC)
                        .parameters()
        );

        assertThat(badRequestException.getMessage(), is("Invalid parameter value."));
    }

    @Test
    public void shouldThrowExceptionInCaseOfInvalidBooleanParamValue() throws Exception {
        final String parameterValue = "NonBoolean";
        when(httpParameterEncoder.encodeForHtmlAttribute(parameterValue)).thenReturn(parameterValue);
        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () ->
                validParameterCollectionBuilder
                        .putRequired("param", parameterValue, ParameterType.BOOLEAN)
                        .parameters()
        );

        assertThat(badRequestException.getMessage(), is("Invalid parameter value."));
    }

    @Test
    public void shouldThrowExceptionInCaseOfInvalidBooleanParamValue2() throws Exception {
        final String parameterValue = "NonBoolean";
        when(httpParameterEncoder.encodeForHtmlAttribute(parameterValue)).thenReturn(parameterValue);
        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () ->
                validParameterCollectionBuilder
                        .putOptional("param", parameterValue, ParameterType.BOOLEAN)
                        .parameters()
        );

        assertThat(badRequestException.getMessage(), is("Invalid parameter value."));
    }
}