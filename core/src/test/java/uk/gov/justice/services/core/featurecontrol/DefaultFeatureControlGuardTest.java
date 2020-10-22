package uk.gov.justice.services.core.featurecontrol;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.core.featurecontrol.domain.Feature;
import uk.gov.justice.services.core.featurecontrol.lookup.FeatureStore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultFeatureControlGuardTest {

    @Mock
    private FeatureStore featureStore;

    @InjectMocks
    private DefaultFeatureControlGuard featureControlGuard;

    @Test
    public void shouldReturnTrueIfTheFeatureIsEnabled() throws Exception {

        final String featureName = "some-feature";
        final boolean enabled = true;

        final Feature feature = new Feature(
                featureName,
                "Some stuff we're working on",
                enabled
        );

        when(featureStore.lookup(featureName)).thenReturn(of(feature));

        assertThat(featureControlGuard.isFeatureEnabled(featureName), is(true));
    }

    @Test
    public void shouldReturnFalseIfTheFeatureIsDisabled() throws Exception {

        final String featureName = "some-feature";
        final boolean enabled = false;

        final Feature feature = new Feature(
                featureName,
                "Some stuff we're working on",
                enabled
        );

        when(featureStore.lookup(featureName)).thenReturn(of(feature));

        assertThat(featureControlGuard.isFeatureEnabled(featureName), is(false));
    }

    @Test
    public void shouldReturnFalseIfTheFeatureIsNotFound() throws Exception {

        final String featureName = "some-feature";

        when(featureStore.lookup(featureName)).thenReturn(empty());

        assertThat(featureControlGuard.isFeatureEnabled(featureName), is(false));
    }
}