package uk.gov.justice.services.integrationtest.utils.jms;

import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class JmsMessageConsumerClientProvider {
    private final JmsMessageClientFactory jmsMessageClientFactory;

    private final String topicName;
    private final List<String> eventNames = new ArrayList<>();

    public static JmsMessageConsumerClientProvider newPublicJmsMessageConsumerClientProvider() {
        return new JmsMessageConsumerClientProvider("jms.topic.public.event");
    }

    public static JmsMessageConsumerClientProvider newPrivateJmsMessageConsumerClientProvider(final String contextName) {
        return new JmsMessageConsumerClientProvider("jms.topic.%s.event".formatted(contextName));
    }

    public static JmsMessageConsumerClientProvider newJmsMessageConsumerClientBuilder(final String topicName) {
        return new JmsMessageConsumerClientProvider(topicName);
    }

    @VisibleForTesting
    JmsMessageConsumerClientProvider(final String topicName, final JmsResourcesContext jmsResourcesContext) {
        this.topicName = topicName;
        this.jmsMessageClientFactory = jmsResourcesContext.getJmsMessageClientFactory();
    }

    private JmsMessageConsumerClientProvider(final String topicName) {
        this(topicName, new JmsResourcesContextProvider().get());
    }

    public JmsMessageConsumerClientProvider withEventNames(final String eventName, final String...additionalEventNames) {
        if(eventName == null || eventName.isBlank()) {
            throw new JmsMessagingClientException("eventName must be supplied");
        }

        this.eventNames.add(eventName);

        if(additionalEventNames != null && additionalEventNames.length > 0) {
            this.eventNames.addAll(asList(additionalEventNames));
        }

        return this;
    }

    public DefaultJmsMessageConsumerClient getMessageConsumerClient() {
        if(eventNames.isEmpty()) {
            throw new JmsMessagingClientException("eventName(s) must be supplied");
        }

        final DefaultJmsMessageConsumerClient defaultJmsMessageConsumerClient = jmsMessageClientFactory.createJmsMessageConsumerClient();
        defaultJmsMessageConsumerClient.startConsumer(topicName, eventNames);

        return defaultJmsMessageConsumerClient;
    }
}
