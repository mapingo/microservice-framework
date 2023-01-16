package uk.gov.justice.services.core.featurecontrol;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FeatureControlAnnotationFinderTest {

    @InjectMocks
    private FeatureControlAnnotationFinder featureControlAnnotationFinder;

    @Test
    public void shouldGetASingleFeatureNameFromTheMethodAnnotation() throws Exception {

        final Method someHandlerMethodWithFeatures = SomeHandler.class.getMethod("someHandlerMethodWithSingleFeature");

        final List<String> annotatedFeatures = featureControlAnnotationFinder.findAnnotatedFeatures(someHandlerMethodWithFeatures);

        assertThat(annotatedFeatures.size(), is(1));

        assertThat(annotatedFeatures, hasItem("feature-1"));
    }

    @Test
    public void shouldGetAListOfFeatureNamesFromTheMethodAnnotation() throws Exception {

        final Method someHandlerMethodWithFeatures = SomeHandler.class.getMethod("someHandlerMethodWithFeatures");

        final List<String> annotatedFeatures = featureControlAnnotationFinder.findAnnotatedFeatures(someHandlerMethodWithFeatures);

        assertThat(annotatedFeatures.size(), is(2));

        assertThat(annotatedFeatures, hasItem("feature-1"));
        assertThat(annotatedFeatures, hasItem("feature-2"));
    }

    @Test
    public void shouldReturnEmptyListIfNoAnnotationFound() throws Exception {

        final Method someHandlerMethodWithoutFeatures = SomeHandler.class.getMethod("someHandlerMethodWithoutFeatures");

        final List<String> annotatedFeatures = featureControlAnnotationFinder.findAnnotatedFeatures(someHandlerMethodWithoutFeatures);

        assertTrue(annotatedFeatures.isEmpty());
    }
}