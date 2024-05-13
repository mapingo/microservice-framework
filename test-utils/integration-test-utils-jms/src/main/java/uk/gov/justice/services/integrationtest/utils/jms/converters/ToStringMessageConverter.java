package uk.gov.justice.services.integrationtest.utils.jms.converters;

public class ToStringMessageConverter implements MessageConverter<String> {

    @Override
    public String convert(String message) {
        return message;
    }
}
