package uk.gov.justice.services.test.utils.core.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.justice.services.core.annotation.Component.COMMAND_API;
import static uk.gov.justice.services.core.annotation.Component.QUERY_API;
import static uk.gov.justice.services.test.utils.core.matchers.HandlerClassMatcher.isCustomHandlerClass;
import static uk.gov.justice.services.test.utils.core.matchers.HandlerClassMatcher.isHandlerClass;
import static uk.gov.justice.services.test.utils.core.matchers.HandlerMatcher.isHandler;
import static uk.gov.justice.services.test.utils.core.matchers.HandlerMethodMatcher.method;
import static uk.gov.justice.services.test.utils.core.matchers.ServiceComponentTestClasses.CUSTOM_API;

import uk.gov.justice.services.test.utils.core.matchers.ServiceComponentTestClasses.NoHandlerMethod;
import uk.gov.justice.services.test.utils.core.matchers.ServiceComponentTestClasses.NoServiceComponentAnnotation;
import uk.gov.justice.services.test.utils.core.matchers.ServiceComponentTestClasses.ValidCommandApi;
import uk.gov.justice.services.test.utils.core.matchers.ServiceComponentTestClasses.ValidCustomServiceComponent;

import org.junit.jupiter.api.Test;

public class HandlerClassMatcherTest {

    @Test
    public void shouldMatchIfServiceComponentAnnotation() throws Exception {
        assertThat(ValidCommandApi.class, isHandlerClass(COMMAND_API));
    }

    @Test
    public void shouldMatchIfCustomServiceComponentAnnotation() throws Exception {
        assertThat(ValidCustomServiceComponent.class, isCustomHandlerClass(CUSTOM_API));
    }

    @Test
    public void shouldNotMatchIfNoServiceComponentAnnotation() throws Exception {
        assertThrows(AssertionError.class, () -> assertThat(NoServiceComponentAnnotation.class, isHandlerClass(COMMAND_API)));
    }

    @Test
    public void shouldNotMatchWhenNoAnnotationGiven() throws Exception {
        assertThrows(AssertionError.class, () -> assertThat(ValidCustomServiceComponent.class, isCustomHandlerClass(null)));
    }

    @Test
    public void shouldNotMatchIfNoCustomServiceComponentAnnotation() throws Exception {
        assertThrows(AssertionError.class, () -> assertThat(NoServiceComponentAnnotation.class, isCustomHandlerClass(CUSTOM_API)));
    }

    @Test
    public void shouldNotMatchInstanceOfAHandlerWithTheWrongAnnotation() throws Exception {
        assertThrows(AssertionError.class, () -> assertThat(ValidCommandApi.class, isHandler(QUERY_API)));
    }

    @Test
    public void shouldMatchHandlerMethod() throws Exception {
        assertThat(ValidCommandApi.class, isHandlerClass(COMMAND_API).with(method("testA").thatHandles("context.commandA")));
    }

    @Test
    public void shouldNotMatchWhenNoHandlerMethod() throws Exception {
        assertThrows(AssertionError.class, () -> assertThat(NoHandlerMethod.class, isHandlerClass(COMMAND_API).with(method("testA").thatHandles("context.commandA"))));
    }
}
