package uk.gov.justice.services.integrationtest.utils.jms;

public class JmsMessagingClientException extends RuntimeException {

    JmsMessagingClientException(final String message) {
        super(message);
    }

    JmsMessagingClientException(final String message, Exception e) {
        super(message, e);
    }
}
