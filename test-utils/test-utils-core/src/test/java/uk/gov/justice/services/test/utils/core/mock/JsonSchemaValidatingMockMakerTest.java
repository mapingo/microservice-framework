package uk.gov.justice.services.test.utils.core.mock;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import static uk.gov.justice.services.test.utils.core.messaging.JsonEnvelopeBuilder.envelope;
import static uk.gov.justice.services.test.utils.core.messaging.MetadataBuilderFactory.metadataWithDefaults;
import static uk.gov.justice.services.test.utils.core.messaging.MetadataBuilderFactory.metadataWithRandomUUID;

import uk.gov.justice.services.core.annotation.Handles;
import uk.gov.justice.services.core.envelope.EnvelopeValidationException;
import uk.gov.justice.services.core.requester.Requester;
import uk.gov.justice.services.core.sender.Sender;
import uk.gov.justice.services.messaging.JsonEnvelope;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JsonSchemaValidatingMockMakerTest {

    @Test
    public void shouldPassWhenPayloadPassedToSenderAdheresToSchema() throws Exception {

        new SendingHandler(mock(Sender.class)).handle(
                envelope()
                        .with(metadataWithRandomUUID("example.add-recipe"))
                        .withPayloadOf("someName", "name")
                        .withPayloadOf(true, "glutenFree")
                        .build());
    }

    @Test
    public void shouldThrowExceptionWhenPayloadPassedToSenderDoesNotAdhereToSchema() {

        final MockitoException mockitoException = assertThrows(MockitoException.class, () ->
                new SendingHandler(mock(Sender.class)).handle(
                        envelope()
                                .with(metadataWithRandomUUID("example.add-recipe"))
                                .withPayloadOf(true, "glutenFree")
                                .build())
        );

        assertThat(mockitoException.getCause(), allOf(instanceOf(EnvelopeValidationException.class),
                hasProperty("message", containsString("#: required key [name] not found"))));
    }

    @Test
    public void shouldThrowExceptionIfNoMetadataInEnvelope() {

        final MockitoException mockitoException = assertThrows(MockitoException.class, () ->
                new SendingHandler(mock(Sender.class)).handle(
                        envelope()
                                .withPayloadOf("someName", "name")
                                .withPayloadOf(true, "glutenFree")
                                .build())
        );

        assertThat(mockitoException.getCause(), allOf(instanceOf(EnvelopeValidationException.class),
                hasProperty("message", equalTo("Metadata not set in the envelope."))));
    }

    @Test
    public void shouldPassWhenPayloadReturnedByRequesterAdheresToSchema() throws Exception {

        final Requester requester = mock(Requester.class);

        when(requester.request(any(JsonEnvelope.class))).thenReturn(envelope()
                .with(metadataWithRandomUUID("example.get-recipe"))
                .withPayloadOf("someName", "name")
                .withPayloadOf(true, "glutenFree")
                .build());

        new RequestingHandler(requester).handle(envelope().build());
    }

    @Test
    public void shouldThrowExceptionWhenPayloadReturnedByRequesterDoesNotAdhereToSchema() {


        final Requester requester = mock(Requester.class);

        when(requester.request(any(JsonEnvelope.class))).thenReturn(envelope()
                .with(metadataWithRandomUUID("example.get-recipe"))
                .withPayloadOf("someName", "name")
                .build());

        final MockitoException mockitoException = assertThrows(MockitoException.class, () ->
                new RequestingHandler(requester).handle(envelope().build())
        );

        assertThat(mockitoException.getCause(), allOf(instanceOf(EnvelopeValidationException.class),
                hasProperty("message", containsString("#: required key [glutenFree] not found"))));
    }

    @Test
    public void shouldSkipValidationIfSkippingListenerAddedToConfig() {

        new SendingHandler(
                mock(Sender.class, withSettings().invocationListeners(new SkipJsonValidationListener())))
                .handle(envelope()
                        .with(metadataWithRandomUUID("unknown"))
                        .build());
    }

    @Test
    public void shouldSkipValidationOnOtherInterface() throws Exception {

        new OtherHandler(mock(SomeInterface.class)).handle(
                envelope()
                        .with(metadataWithDefaults())
                        .withPayloadOf(true, "glutenFree")
                        .build());
    }

    public static class SendingHandler {
        private Sender sender;

        public SendingHandler(final Sender sender) {
            this.sender = sender;
        }

        public void handle(final JsonEnvelope envelope) {
            sender.send(envelope);
        }
    }

    public static class RequestingHandler {
        private Requester requester;

        public RequestingHandler(final Requester requester) {
            this.requester = requester;
        }

        public JsonEnvelope handle(final JsonEnvelope envelope) {
            return requester.request(envelope);
        }
    }

    public static class OtherHandler {
        private SomeInterface someInterface;

        public OtherHandler(final SomeInterface someInterface) {
            this.someInterface = someInterface;
        }

        @Handles("example.add-recipe")
        public void handle(final JsonEnvelope envelope) {
            someInterface.process(envelope);
        }
    }
}
