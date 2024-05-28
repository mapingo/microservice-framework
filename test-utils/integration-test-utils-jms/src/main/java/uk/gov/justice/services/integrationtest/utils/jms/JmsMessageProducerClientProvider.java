package uk.gov.justice.services.integrationtest.utils.jms;

import com.google.common.annotations.VisibleForTesting;

public class JmsMessageProducerClientProvider {
    private final JmsMessageClientFactory jmsMessageClientFactory;
    private final String topicName;

    public static JmsMessageProducerClientProvider newPublicJmsMessageProducerClientProvider() {
        return new JmsMessageProducerClientProvider("jms.topic.public.event");
    }

    public static JmsMessageProducerClientProvider newPrivateJmsMessageProducerClientProvider(final String contextName) {
        return new JmsMessageProducerClientProvider("jms.topic.%s.event".formatted(contextName));
    }

    @VisibleForTesting
    JmsMessageProducerClientProvider(final String topicName, final JmsResourcesContext jmsResourcesContext) {
        this.topicName = topicName;
        this.jmsMessageClientFactory = jmsResourcesContext.getJmsMessageClientFactory();
    }

    private JmsMessageProducerClientProvider(final String topicName) {
        this(topicName, new JmsResourcesContextProvider().get());
    }

    public JmsMessageProducerClient getMessageProducerClient() {
        final DefaultJmsMessageProducerClient defaultJmsMessageProducerClient = jmsMessageClientFactory.createJmsMessageProducerClient();
        defaultJmsMessageProducerClient.createProducer(topicName);

        return defaultJmsMessageProducerClient;
    }
}
