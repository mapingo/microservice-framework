package uk.gov.justice.services.adapter.rest.parameter;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

public class ValidParameterCollectionBuilderFactoryTest {

    @Test
    public void shouldCreateNewInstanceOfValidParameterCollectionBuilder() throws Exception {
        final ParameterCollectionBuilder collectionBuilder = new ValidParameterCollectionBuilderFactory().create();

        assertThat(collectionBuilder, instanceOf(ValidParameterCollectionBuilder.class));
        assertThat(collectionBuilder, instanceOf(ParameterCollectionBuilder.class));
        assertThat(getValueOfField(collectionBuilder, "logger", Logger.class), is(notNullValue()));
    }

    @Test
    public void shouldCreateNewInstanceForEachCreateCall() throws Exception {
        final ValidParameterCollectionBuilderFactory collectionBuilderFactory = new ValidParameterCollectionBuilderFactory();
        final ParameterCollectionBuilder collectionBuilder1 = collectionBuilderFactory.create();
        final ParameterCollectionBuilder collectionBuilder2 = collectionBuilderFactory.create();

        assertThat(collectionBuilder1, not(collectionBuilder2));
    }
}