package uk.gov.justice.services.adapter.rest.parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ValidParameterCollectionBuilderFactory implements ParameterCollectionBuilderFactory {

    @Inject
    private HttpParameterEncoder httpParameterEncoder;

    @Override
    public ParameterCollectionBuilder create() {

        final Logger logger = LoggerFactory.getLogger(ParameterCollectionBuilder.class);

        return new ValidParameterCollectionBuilder(httpParameterEncoder, logger);
    }
}