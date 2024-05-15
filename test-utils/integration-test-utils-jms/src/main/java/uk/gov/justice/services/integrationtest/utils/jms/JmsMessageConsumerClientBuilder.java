package uk.gov.justice.services.integrationtest.utils.jms;

import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class JmsMessageConsumerClientBuilder {
    private final JmsMessageClientFactory jmsMessageClientFactory;

    private final String topicName;
    private final List<String> eventNames = new ArrayList<>();

    public static JmsMessageConsumerClientBuilder newPublicJmsMessageConsumerClientBuilder() {
        return new JmsMessageConsumerClientBuilder("jms.topic.public.event");
    }

    public static JmsMessageConsumerClientBuilder newPrivateJmsMessageConsumerClientBuilder(final String contextName) {
        return new JmsMessageConsumerClientBuilder("jms.topic.%s.event".formatted(contextName));
    }

    public static JmsMessageConsumerClientBuilder newJmsMessageConsumerClientBuilder(final String topicName) {
        return new JmsMessageConsumerClientBuilder(topicName);
    }

    @VisibleForTesting
    JmsMessageConsumerClientBuilder(final String topicName, final JmsResourcesContext jmsResourcesContext) {
        this.topicName = topicName;
        this.jmsMessageClientFactory = jmsResourcesContext.getJmsMessageClientFactory();
    }

    private JmsMessageConsumerClientBuilder(final String topicName) {
        this(topicName, new JmsResourcesContextProvider().get());
    }

    public JmsMessageConsumerClientBuilder withEventNames(final String eventName, final String...additionalEventNames) {
        if(eventName == null || eventName.isBlank()) {
            throw new JmsMessagingClientException("eventName must be supplied");
        }

        this.eventNames.add(eventName);

        if(additionalEventNames != null && additionalEventNames.length > 0) {
            this.eventNames.addAll(asList(additionalEventNames));
        }

        return this;
    }

    public JmsMessageConsumerClient build() {
        if(eventNames.isEmpty()) {
            throw new JmsMessagingClientException("eventName(s) must be supplied");
        }

        final JmsMessageConsumerClient jmsMessageConsumerClient = jmsMessageClientFactory.createJmsMessageConsumerClient();
        jmsMessageConsumerClient.startConsumer(topicName, eventNames);

        return jmsMessageConsumerClient;
    }
}
