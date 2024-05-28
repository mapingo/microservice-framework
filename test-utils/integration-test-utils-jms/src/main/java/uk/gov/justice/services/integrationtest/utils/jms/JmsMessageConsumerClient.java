package uk.gov.justice.services.integrationtest.utils.jms;

import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.List;
import java.util.Optional;

import io.restassured.path.json.JsonPath;

public interface JmsMessageConsumerClient {

    Optional<String> retrieveMessageNoWait();
    Optional<String> retrieveMessage();
    Optional<String> retrieveMessage(final long timeout);
    Optional<JsonEnvelope> retrieveMessageAsJsonEnvelopeNoWait();
    Optional<JsonEnvelope> retrieveMessageAsJsonEnvelope();
    Optional<JsonEnvelope> retrieveMessageAsJsonEnvelope(final long timeout);
    Optional<JsonPath> retrieveMessageAsJsonPathNoWait();
    Optional<JsonPath> retrieveMessageAsJsonPath();
    Optional<JsonPath> retrieveMessageAsJsonPath(final long timeout);
    List<JsonPath> retrieveMessagesAsJsonPath(final int expectedCount);
    void clearMessages();
}
