package uk.gov.justice.services.core.featurecontrol.remote;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FeatureRefreshTimerConfigTest {

    @InjectMocks
    private FeatureRefreshTimerConfig featureRefreshTimerConfig;

    @Test
    public void shouldGetTheFeatureRefreshTimerStartWaitInMilliseconds() throws Exception {

        final long timerStartWait = 98234L;

        setField(featureRefreshTimerConfig, "timerStartWaitMilliseconds", timerStartWait + "");

        assertThat(featureRefreshTimerConfig.getTimerStartWaitMilliseconds(), is(timerStartWait));
    }
    
    @Test
    public void shouldGetTheFeatureRefreshTimerIntervalInMilliseconds() throws Exception {

        final long timerInterval = 98234L;

        setField(featureRefreshTimerConfig, "timerIntervalMilliseconds", timerInterval + "");

        assertThat(featureRefreshTimerConfig.getTimerIntervalMilliseconds(), is(timerInterval));
    }
}