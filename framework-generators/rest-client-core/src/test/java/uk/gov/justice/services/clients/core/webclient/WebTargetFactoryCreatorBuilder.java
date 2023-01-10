package uk.gov.justice.services.clients.core.webclient;

import static uk.gov.justice.services.clients.core.webclient.BaseUriFactoryBuilder.aBaseUriFactoryBuilder;

public class WebTargetFactoryCreatorBuilder {

    public static WebTargetFactoryCreator aWebTargetFactoryCreator(String appName) {
        var webTargetFactoryCreator = new WebTargetFactoryCreator();
        webTargetFactoryCreator.baseUriFactory = aBaseUriFactoryBuilder()
                .withAppName(appName)
                .build();

        return webTargetFactoryCreator;
    }
}
