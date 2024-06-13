package uk.gov.justice.services.integrationtest.utils.jms.converters;

@FunctionalInterface
public interface MessageConverter<T> {

    T convert(String message);
}
