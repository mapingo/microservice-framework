package uk.gov.justice.services.clients.core.webclient;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

@Default
public class WebTargetFactoryFactory {

    @Inject
    BaseUriFactory baseUriFactory;

    public WebTargetFactory create() {
        return new WebTargetFactory(baseUriFactory);
    }
}
