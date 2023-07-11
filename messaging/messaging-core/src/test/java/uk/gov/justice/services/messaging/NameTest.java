package uk.gov.justice.services.messaging;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import uk.gov.justice.services.messaging.exception.InvalidMediaTypeException;

import org.junit.jupiter.api.Test;

public class NameTest {

    private static final String MEDIA_TYPE = "application/vnd.cakeshop.add-recipe+json";
    private static final String NAME = "cakeshop.add-recipe";

    @Test
    public void shouldReturnValidName() {
        assertThat(Name.fromMediaType(MEDIA_TYPE).toString(), equalTo(NAME));
    }

    @Test
    public void shouldThrowExceptionOnInvalidPrefix() {
        assertThrows(InvalidMediaTypeException.class, () -> Name.fromMediaType("application/invalid+json"));
    }

//    @Test(expected = InvalidMediaTypeException.class)
//    public void shouldThrowExceptionOnInvalidSuffix() {
//        assertThat(Name.fromMediaType("application/vnd.cakeshop.add-recipe.json").toString(), equalTo(NAME));
//    }

}