package uk.gov.justice.services.messaging.jms;

import uk.gov.justice.services.messaging.JsonEnvelope;

public class AuditJmsEnvelopeSender implements JmsEnvelopeSender {
    private final AuditJmsSender jmsSender;

    public AuditJmsEnvelopeSender(final AuditJmsSender jmsSender) {
        this.jmsSender = jmsSender;
    }

    /**
     * Sends envelope to the destination via JMS.
     *
     * @param envelope        envelope to be sent.
     * @param destinationName JNDI name of the JMS destination.
     */
    @Override
    public void send(final JsonEnvelope envelope, final String destinationName) {
        jmsSender.send(envelope, destinationName);
    }

}
