package uk.gov.justice.services.integrationtest.utils.jms;

import com.google.common.annotations.VisibleForTesting;

public class JmsMessageProducerClientBuilder {
    private final JmsMessageClientFactory jmsMessageClientFactory;
    private final String topicName;

    public static JmsMessageProducerClientBuilder newPublicJmsMessageProducerClientBuilder() {
        return new JmsMessageProducerClientBuilder("jms.topic.public.event");
    }

    public static JmsMessageProducerClientBuilder newPrivateJmsMessageProducerClientBuilder(final String contextName) {
        return new JmsMessageProducerClientBuilder("jms.topic.%s.event".formatted(contextName));
    }

    @VisibleForTesting
    JmsMessageProducerClientBuilder(final String topicName, final JmsSingletonResourceProvider jmsSingletonResourceProvider) {
        this.topicName = topicName;
        this.jmsMessageClientFactory = jmsSingletonResourceProvider.getJmsMessageClientFactory();
    }

    private JmsMessageProducerClientBuilder(final String topicName) {
        this(topicName, new JmsSingletonResourceProvider());
    }

    public JmsMessageProducerClient build() {
        final JmsMessageProducerClient jmsMessageProducerClient = jmsMessageClientFactory.createJmsMessageProducerClient();
        jmsMessageProducerClient.createProducer(topicName);

        return jmsMessageProducerClient;
    }
}
