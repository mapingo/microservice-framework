package uk.gov.justice.services.clients.core.webclient;

import static uk.gov.justice.services.clients.core.webclient.BaseUriFactoryBuilder.aBaseUriFactoryBuilder;

public class WebTargetFactoryFactoryBuilder {

    public static WebTargetFactoryFactory aWebTargetFactoryFactory(String appName) {
        var webTargetFactoryFactory = new WebTargetFactoryFactory();
        webTargetFactoryFactory.baseUriFactory = aBaseUriFactoryBuilder()
                .withAppName(appName)
                .build();

        return webTargetFactoryFactory;
    }
}
