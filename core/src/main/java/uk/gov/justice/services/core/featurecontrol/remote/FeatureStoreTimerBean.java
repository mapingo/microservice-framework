package uk.gov.justice.services.core.featurecontrol.remote;

import static java.util.Optional.ofNullable;

import uk.gov.justice.services.core.featurecontrol.FeatureFetcher;
import uk.gov.justice.services.core.featurecontrol.domain.Feature;
import uk.gov.justice.services.ejb.timer.TimerServiceManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerService;
import javax.inject.Inject;

@Singleton
@Startup
public class FeatureStoreTimerBean {

    private static final String TIMER_JOB_NAME = "framework.feature-store-refresh.job";

    private AtomicReference<Map<String, Feature>> atomicFeatureMapReference = new AtomicReference<>(new HashMap<>());

    @Inject
    private FeatureFetcher featureFetcher;

    @Inject
    private TimerServiceManager timerServiceManager;

    @Resource
    private TimerService timerService;

    @Inject
    private FeatureRefreshTimerConfig featureRefreshTimerConfig;

    @PostConstruct
    public void startTimerService() {

        timerServiceManager.createIntervalTimer(
                TIMER_JOB_NAME,
                featureRefreshTimerConfig.getTimerStartWaitMilliseconds(),
                featureRefreshTimerConfig.getTimerIntervalMilliseconds(),
                timerService);
    }

    @Timeout
    public void reloadFeaturesOnTimeout() {
        reloadFeatures();
    }

    public void reloadFeatures() {
        final Map<String, Feature> featureMap = new HashMap<>();

        featureFetcher
                .fetchFeatures()
                .forEach(feature -> featureMap.put(feature.getFeatureName(), feature));

        atomicFeatureMapReference.set(featureMap);
    }

    public Optional<Feature> lookup(final String featureName) {
        return ofNullable(atomicFeatureMapReference.get().get(featureName));
    }
}
