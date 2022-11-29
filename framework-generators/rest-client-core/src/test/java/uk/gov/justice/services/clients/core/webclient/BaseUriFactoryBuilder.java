package uk.gov.justice.services.clients.core.webclient;

import static uk.gov.justice.services.common.configuration.JndiBasedServiceContextNameProviderFactory.jndiBasedServiceContextNameProviderWith;

import uk.gov.justice.services.common.http.DefaultServerPortProvider;

public class BaseUriFactoryBuilder {

    private String appName;

    public static BaseUriFactoryBuilder aBaseUriFactoryBuilder() {
        return new BaseUriFactoryBuilder();
    }

    public BaseUriFactoryBuilder withAppName(final String appName) {
        this.appName = appName;
        return this;
    }

    public BaseUriFactory build() {
        var baseUriFactory = new BaseUriFactory();
        baseUriFactory.serverPortProvider = new DefaultServerPortProvider();
        baseUriFactory.mockServerPortProvider = new MockServerPortProvider();
        baseUriFactory.mockServerPortProvider.contextMatcher = new ContextMatcher();
        baseUriFactory.mockServerPortProvider.contextMatcher.contextNameProvider = jndiBasedServiceContextNameProviderWith(appName);

        return baseUriFactory;
    }
}
