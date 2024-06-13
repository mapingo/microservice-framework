package uk.gov.justice.services.integrationtest.utils.jms;

import uk.gov.justice.services.messaging.JsonEnvelope;

import javax.json.JsonObject;

public interface JmsMessageProducerClient {

    void createProducer(String topicName);
    void sendMessage(String commandName, JsonObject payload);
    void sendMessage(String commandName, JsonEnvelope jsonEnvelope);
}
