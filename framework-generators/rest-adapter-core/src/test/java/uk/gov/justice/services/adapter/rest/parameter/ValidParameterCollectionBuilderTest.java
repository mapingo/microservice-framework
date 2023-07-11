package uk.gov.justice.services.adapter.rest.parameter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.jupiter.api.Assertions.assertThrows;

import uk.gov.justice.services.adapter.rest.exception.BadRequestException;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ValidParameterCollectionBuilderTest {

    private ValidParameterCollectionBuilder validParameterCollectionBuilder;

    @BeforeEach
    public void setup() {
        validParameterCollectionBuilder = new ValidParameterCollectionBuilder();
    }

    @Test
    public void shouldReturnEmptyMap() throws Exception {
        assertThat(validParameterCollectionBuilder.parameters().size(), is(0));
    }

    @Test
    public void shouldReturnMapWithRequiredParameter() throws Exception {
        validParameterCollectionBuilder.putRequired("Name1", "Value1", ParameterType.STRING);

        Collection<Parameter> validParameters = validParameterCollectionBuilder.parameters();

        assertThat(validParameters.size(), is(1));
        assertThat(validParameters.iterator().next().getStringValue(), is("Value1"));
    }

    @Test
    public void shouldReturnMapWithNoOptionalParameterIfNullValue() throws Exception {
        validParameterCollectionBuilder.putOptional("OptionalName1", null, ParameterType.STRING);
        Collection<Parameter> validParameters = validParameterCollectionBuilder.parameters();
        assertThat(validParameters.size(), is(0));
    }

    @Test
    public void shouldReturnMapWithOptionalParameterIfValueSet() throws Exception {
        validParameterCollectionBuilder.putOptional("OptionalName1", "OptionalValue1", ParameterType.STRING);

        Collection<Parameter> validParameters = validParameterCollectionBuilder.parameters();

        assertThat(validParameters.size(), is(1));
        assertThat(validParameters.iterator().next().getStringValue(), is("OptionalValue1"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReturnMapWithMultipleRequiredAndOptionalParameters() throws Exception {
        validParameterCollectionBuilder
                .putRequired("paramName1", "Value1", ParameterType.STRING)
                .putOptional("OptionalName1", null, ParameterType.STRING)
                .putOptional("OptionalName2", "OptionalValue2", ParameterType.STRING)
                .putOptional("OptionalName3", "1111", ParameterType.NUMERIC)
                .putRequired("Name2", "567", ParameterType.NUMERIC);

        Collection<Parameter> validParameters = validParameterCollectionBuilder.parameters();

        assertThat(validParameters, hasSize(4));
        assertThat(validParameters, hasItems(
                allOf(hasProperty("name", equalTo("paramName1")), hasProperty("stringValue", equalTo("Value1"))),
                allOf(hasProperty("name", equalTo("OptionalName2")), hasProperty("stringValue", equalTo("OptionalValue2"))),
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

        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () ->
                validParameterCollectionBuilder
                        .putRequired("param", "NonNumeric", ParameterType.NUMERIC)
                        .parameters()
        );
        
        assertThat(badRequestException.getMessage(), is("Invalid parameter value."));
    }

    @Test
    public void shouldThrowExceptionInCaseOfInvalidNumericParamValue2() throws Exception {
        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () ->
                validParameterCollectionBuilder
                        .putOptional("param", "NonNumeric", ParameterType.NUMERIC)
                        .parameters()
        );

        assertThat(badRequestException.getMessage(), is("Invalid parameter value."));
    }

    @Test
    public void shouldThrowExceptionInCaseOfInvalidBooleanParamValue() throws Exception {
        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () ->
                validParameterCollectionBuilder
                        .putRequired("param", "NonBoolean", ParameterType.BOOLEAN)
                        .parameters()
        );

        assertThat(badRequestException.getMessage(), is("Invalid parameter value."));
    }

    @Test
    public void shouldThrowExceptionInCaseOfInvalidBooleanParamValue2() throws Exception {
        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () ->
                validParameterCollectionBuilder
                        .putOptional("param", "NonBoolean", ParameterType.BOOLEAN)
                        .parameters()
        );

        assertThat(badRequestException.getMessage(), is("Invalid parameter value."));
    }
}