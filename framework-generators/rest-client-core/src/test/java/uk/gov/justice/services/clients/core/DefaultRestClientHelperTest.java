package uk.gov.justice.services.clients.core;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Set;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultRestClientHelperTest {

    @Test
    public void shouldExtractPathParametersFromPathWithOneParam() {
        Set<String> pathParams = new DefaultRestClientHelper().extractPathParametersFromPath("/users/{userId}");
        assertThat(pathParams, IsCollectionWithSize.hasSize(1));
        assertThat(pathParams, IsCollectionContaining.hasItem("userId"));

    }

    @Test
    public void shouldExtractPathParametersFromPathWithTwoParams() {
        Set<String> pathParams = new DefaultRestClientHelper().extractPathParametersFromPath("/users/{lastName}/{dob}");
        assertThat(pathParams, IsCollectionWithSize.hasSize(2));
        assertThat(pathParams, IsCollectionContaining.hasItems("lastName", "dob"));
    }

    @Test
    public void shouldReturnEmptySetWhenNoParams() {
        Set<String> pathParams = new DefaultRestClientHelper().extractPathParametersFromPath("/users/");
        assertThat(pathParams, IsCollectionWithSize.hasSize(0));
    }

}
