package uk.gov.justice.services.adapter.rest.filter;

import org.slf4j.MDC;

public class MdcWrapper {

    public void put(final String key, final String value) {
        MDC.put(key, value);
    }

    public String get(final String key) {
        return MDC.get(key);
    }

    public void clear() {
        MDC.clear();
    }
}
