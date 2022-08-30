package uk.gov.justice.services.test.utils.core.messaging;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

import com.google.common.annotations.VisibleForTesting;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageProducerClientBuilder {

    private Optional<Integer> retryInterval = empty();
    private Optional<Integer> maxRetryInterval = empty();
    private Optional<Double> retryIntervalMultiplier = empty();
    private Optional<Integer> reconnectAttempts = empty();

    private final ActiveMQConnectionFactory activeMQConnectionFactory;

    public static MessageProducerClientBuilder aMessageProducerClient() {
        return new MessageProducerClientBuilder(new ActiveMQConnectionFactory());
    }

    @VisibleForTesting
    private MessageProducerClientBuilder(final ActiveMQConnectionFactory activeMQConnectionFactory) {
        this.activeMQConnectionFactory = activeMQConnectionFactory;
    }

    public MessageProducerClientBuilder withRetryInterval(final int retryInterval) {
        this.retryInterval = of(retryInterval);
        return this;
    }

    public MessageProducerClientBuilder withMaxRetryInterval(final int maxRetryInterval) {
        this.maxRetryInterval = of(maxRetryInterval);
        return this;
    }

    public MessageProducerClientBuilder withRetryIntervalMultiplier(final double retryIntervalMultiplier) {
        this.retryIntervalMultiplier = of(retryIntervalMultiplier);
        return this;
    }

    public MessageProducerClientBuilder withReconnectAttempts(final int reconnectAttempts) {
        this.reconnectAttempts = of(reconnectAttempts);
        return this;
    }

    public MessageProducerClient build() {

        retryInterval.ifPresent(activeMQConnectionFactory::setRetryInterval);
        maxRetryInterval.ifPresent(activeMQConnectionFactory::setMaxRetryInterval);
        retryIntervalMultiplier.ifPresent(activeMQConnectionFactory::setRetryIntervalMultiplier);
        reconnectAttempts.ifPresent(activeMQConnectionFactory::setReconnectAttempts);

        return new MessageProducerClient(activeMQConnectionFactory);
    }
}
