package uk.gov.justice.services.management.suspension.commands;

import uk.gov.justice.services.jmx.api.command.BaseSystemCommand;

public class RefreshFeatureControlCacheCommand extends BaseSystemCommand {

    public static final String REFRESH_FEATURE_CACHE = "REFRESH_FEATURE_CACHE";
    private static final String DESCRIPTION = "Forces the cache of FeatureControl Features to be refreshed.";

    public RefreshFeatureControlCacheCommand() {
        super(REFRESH_FEATURE_CACHE, DESCRIPTION);
    }
}
