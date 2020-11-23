package uk.gov.justice.services.core.featurecontrol.remote;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FeatureControlConfigurationTest {

    @InjectMocks
    private FeatureControlConfiguration featureControlConfiguration;

    @Test
    public void shouldGetTheFeatureRefreshTimerStartWaitInMilliseconds() throws Exception {

        final long timerStartWait = 98234L;

        setField(featureControlConfiguration, "timerStartWaitMilliseconds", timerStartWait + "");

        assertThat(featureControlConfiguration.getTimerStartWaitMilliseconds(), is(timerStartWait));
    }
    
    @Test
    public void shouldGetTheFeatureRefreshTimerIntervalInMilliseconds() throws Exception {

        final long timerInterval = 98234L;

        setField(featureControlConfiguration, "timerIntervalMilliseconds", timerInterval + "");

        assertThat(featureControlConfiguration.getTimerIntervalMilliseconds(), is(timerInterval));
    }

    @Test
    public void shouldGetTheEnabledStateOfTheFeatureCache() throws Exception {

        setField(featureControlConfiguration, "featureControlCacheEnabled", "true");
        assertThat(featureControlConfiguration.isFeatureControlCacheEnabled(), is(true));

        setField(featureControlConfiguration, "featureControlCacheEnabled", "false");
        assertThat(featureControlConfiguration.isFeatureControlCacheEnabled(), is(false));

        setField(featureControlConfiguration, "featureControlCacheEnabled", "something silly");
        assertThat(featureControlConfiguration.isFeatureControlCacheEnabled(), is(false));
    }

    @Test
    public void shouldGetTheEnabledStateOfTheFeatureControl() throws Exception {

        setField(featureControlConfiguration, "featureControlEnabled", "true");
        assertThat(featureControlConfiguration.isFeatureControlEnabled(), is(true));

        setField(featureControlConfiguration, "featureControlEnabled", "false");
        assertThat(featureControlConfiguration.isFeatureControlEnabled(), is(false));

        setField(featureControlConfiguration, "featureControlEnabled", "something silly");
        assertThat(featureControlConfiguration.isFeatureControlEnabled(), is(false));
    }
}