package uk.gov.justice.services.messaging.jms;

/**
 * Exception thrown when there's error accessing database
 */
public class JndiException extends RuntimeException {

    private static final long serialVersionUID = 5934757852541630246L;

    public JndiException(String message) {
        super(message);
    }

    public JndiException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public JndiException(final Throwable cause) {
        super(cause);
    }
}
