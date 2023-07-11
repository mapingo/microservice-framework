package uk.gov.justice.services.test.utils.core.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.justice.services.core.annotation.Component.COMMAND_API;
import static uk.gov.justice.services.core.annotation.Component.QUERY_API;
import static uk.gov.justice.services.test.utils.core.matchers.HandlerMatcher.isCustomHandler;
import static uk.gov.justice.services.test.utils.core.matchers.HandlerMatcher.isHandler;
import static uk.gov.justice.services.test.utils.core.matchers.HandlerMethodMatcher.method;

import uk.gov.justice.services.test.utils.core.matchers.ServiceComponentTestClasses.NoHandlerMethod;
import uk.gov.justice.services.test.utils.core.matchers.ServiceComponentTestClasses.NoServiceComponentAnnotation;
import uk.gov.justice.services.test.utils.core.matchers.ServiceComponentTestClasses.ValidCommandApi;
import uk.gov.justice.services.test.utils.core.matchers.ServiceComponentTestClasses.ValidCustomServiceComponent;

import org.junit.jupiter.api.Test;

public class HandlerMatcherTest {

    @Test
    public void shouldMatchInstanceOfCommandApiHandlerWithAnnotation() throws Exception {
        assertThat(new ValidCommandApi(), isHandler(COMMAND_API));
    }

    @Test
    public void shouldMatchInstanceOfCustomApiHandlerWithAnnotation() throws Exception {
        assertThat(new ValidCustomServiceComponent(), isCustomHandler("CUSTOM_API"));
    }

    @Test
    public void shouldNotMatchInstanceOfAHandlerWithNoAnnotation() throws Exception {
        assertThrows(AssertionError.class, () -> assertThat(new NoServiceComponentAnnotation(), isHandler(COMMAND_API)));
    }

    @Test
    public void shouldNotMatchInstanceOfAHandlerWithTheWrongAnnotation() throws Exception {
        assertThrows(AssertionError.class, () -> assertThat(new ValidCommandApi(), isHandler(QUERY_API)));
    }

    @Test
    public void shouldMatchWithGivenMatcherHandlerMethod() throws Exception {
        assertThat(new ValidCommandApi(), isHandler(COMMAND_API).with(method("testA").thatHandles("context.commandA")));
    }

    @Test
    public void shouldNotMatchWithGivenMatcher() throws Exception {
        assertThrows(AssertionError.class, () -> assertThat(new NoHandlerMethod(), isHandler(COMMAND_API).with(method("testA").thatHandles("context.commandA"))));
    }
}