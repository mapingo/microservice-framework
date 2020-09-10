package uk.gov.justice.services.test.utils.core.converter;

import uk.gov.justice.services.common.converter.JsonObjectToObjectConverter;
import uk.gov.justice.services.test.utils.framework.api.JsonObjectConvertersFactory;

/**
 * @deprecated Please use JsonObjectConvertersFactory directly in your tests
 */
@Deprecated
public class JsonObjectToObjectConverterFactory {


    /**
     * @deprecated Please use JsonObjectConvertersFactory directly in your tests
     */
    @Deprecated
    public static JsonObjectToObjectConverter createJsonObjectToObjectConverter() {
        return new JsonObjectConvertersFactory().jsonObjectToObjectConverter();
    }
}