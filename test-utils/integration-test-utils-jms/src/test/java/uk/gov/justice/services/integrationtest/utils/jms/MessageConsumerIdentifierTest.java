package uk.gov.justice.services.integrationtest.utils.jms;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageConsumerIdentifierTest {

    @Test
    void equalsTrueWithBothTopicAndSelectorAreSame() {
        final String topicName = "test-topic";
        final String messageSelector = "test-selector";

        final MessageConsumerIdentifier mci1 = new MessageConsumerIdentifier(topicName, messageSelector);
        final MessageConsumerIdentifier mci2 = new MessageConsumerIdentifier(topicName, messageSelector);

        assertTrue(mci1.equals(mci2));
    }

    @Test
    void equalsFalseWhenTopicNameIsNotSame() {
        final String messageSelector = "test-selector";

        final MessageConsumerIdentifier mci1 = new MessageConsumerIdentifier("topic1", messageSelector);
        final MessageConsumerIdentifier mci2 = new MessageConsumerIdentifier("topic2", messageSelector);

        assertFalse(mci1.equals(mci2));
    }

    @Test
    void equalsFalseWhenMessageSelectorIsNotSame() {
        final String topicName = "test-topic";

        final MessageConsumerIdentifier mci1 = new MessageConsumerIdentifier(topicName, "selector1");
        final MessageConsumerIdentifier mci2 = new MessageConsumerIdentifier(topicName, "selector2");

        assertFalse(mci1.equals(mci2));
    }
}