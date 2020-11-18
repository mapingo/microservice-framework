package uk.gov.justice.services.core.featurecontrol.remote;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FeatureCachingConfigurationTest {

    @InjectMocks
    private FeatureCachingConfiguration featureCachingConfiguration;

    @Test
    public void shouldGetTheFeatureRefreshTimerStartWaitInMilliseconds() throws Exception {

        final long timerStartWait = 98234L;

        setField(featureCachingConfiguration, "timerStartWaitMilliseconds", timerStartWait + "");

        assertThat(featureCachingConfiguration.getTimerStartWaitMilliseconds(), is(timerStartWait));
    }
    
    @Test
    public void shouldGetTheFeatureRefreshTimerIntervalInMilliseconds() throws Exception {

        final long timerInterval = 98234L;

        setField(featureCachingConfiguration, "timerIntervalMilliseconds", timerInterval + "");

        assertThat(featureCachingConfiguration.getTimerIntervalMilliseconds(), is(timerInterval));
    }

    @Test
    public void shouldGetTheEnabledStateOfTheFeatureCache() throws Exception {

        setField(featureCachingConfiguration, "featureCacheEnabled", "true");
        assertThat(featureCachingConfiguration.isFeatureCacheEnabled(), is(true));

        setField(featureCachingConfiguration, "featureCacheEnabled", "false");
        assertThat(featureCachingConfiguration.isFeatureCacheEnabled(), is(false));

        setField(featureCachingConfiguration, "featureCacheEnabled", "something silly");
        assertThat(featureCachingConfiguration.isFeatureCacheEnabled(), is(false));
    }
}