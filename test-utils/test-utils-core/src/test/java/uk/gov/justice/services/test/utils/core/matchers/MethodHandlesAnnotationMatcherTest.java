package uk.gov.justice.services.test.utils.core.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.justice.services.test.utils.core.matchers.MethodHandlesAnnotationMatcher.methodThatHandles;

import uk.gov.justice.services.core.annotation.Handles;

import org.junit.jupiter.api.Test;

public class MethodHandlesAnnotationMatcherTest {

    @Test
    public void shouldMatchMethodWithHandlesAnnotation() throws Exception {
        assertThat(TestClass.class.getMethod("testA"), methodThatHandles("context.commandA"));
    }

    @Test
    public void shouldNotMatchMethodWithNoHandlesAnnotation() throws Exception {
        assertThrows(AssertionError.class, () -> assertThat(TestClass.class.getMethod("testB"), methodThatHandles("context.commandB")));
    }

    @Test
    public void shouldNotMatchMethodWithDifferentHandlesValue() throws Exception {
        assertThrows(AssertionError.class, () -> assertThat(TestClass.class.getMethod("testC"), methodThatHandles("context.commandC")));
    }

    public static class TestClass {
        @Handles("context.commandA")
        public void testA() {
        }

        public void testB() {
        }

        @Handles("context.commandA")
        public void testC() {
        }
    }
}