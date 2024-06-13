package uk.gov.justice.services.integrationtest.utils.jms;

record MessageConsumerIdentifier(String topicName, String messageSelector) {

    public boolean equals(final Object anObject) {
        if (this == anObject) {
            return true;
        }
        return (anObject instanceof MessageConsumerIdentifier messageConsumerIdentifier)
                && topicName.equals(messageConsumerIdentifier.topicName)
                && messageSelector.equals(messageConsumerIdentifier.messageSelector);
    }
}
