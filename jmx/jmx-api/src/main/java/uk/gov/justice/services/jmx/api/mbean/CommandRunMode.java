package uk.gov.justice.services.jmx.api.mbean;

public enum CommandRunMode {

    GUARDED(true),
    FORCED(false);

    private boolean guarded;

    CommandRunMode(final boolean guarded) {
        this.guarded = guarded;
    }

    public boolean isGuarded() {
        return guarded;
    }

    public static CommandRunMode fromBoolean(final boolean guarded) {

        if (guarded) {
            return GUARDED;
        }

        return FORCED;
    }
}
