package uk.gov.justice.services.core.featurecontrol.remote;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Long.parseLong;

import uk.gov.justice.services.common.configuration.GlobalValue;

import javax.inject.Inject;

public class FeatureCachingConfiguration {

    private final String TEN_MINUTES_IN_MILLISECONDS = 10 * 60 * 1000 + "";

    @Inject
    @GlobalValue(key = "feature-control-cache.enabled", defaultValue = "false")
    private String featureCacheEnabled;

    @Inject
    @GlobalValue(key = "feature-control-cache-refresh-rate.timer.interval.milliseconds", defaultValue = TEN_MINUTES_IN_MILLISECONDS)
    private String timerIntervalMilliseconds;

    @Inject
    @GlobalValue(key = "feature-control-cache-refresh-rate.timer.start.wait.milliseconds", defaultValue = "0")
    private String timerStartWaitMilliseconds;


    public long getTimerStartWaitMilliseconds() {
        return parseLong(timerStartWaitMilliseconds);
    }

    public long getTimerIntervalMilliseconds() {
        return parseLong(timerIntervalMilliseconds);
    }

    public boolean isFeatureCacheEnabled() {
        return parseBoolean(featureCacheEnabled);
    }
}
