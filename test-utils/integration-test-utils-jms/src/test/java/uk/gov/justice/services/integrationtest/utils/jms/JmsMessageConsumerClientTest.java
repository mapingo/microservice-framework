package uk.gov.justice.services.integrationtest.utils.jms;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToJsonEnvelopeMessageConverter;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToJsonPathMessageConverter;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToStringMessageConverter;
import uk.gov.justice.services.messaging.JsonEnvelope;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

@ExtendWith(MockitoExtension.class)
class JmsMessageConsumerClientTest {

    private static final String TOPIC_NAME = "jms.topic.public.event";
    private static final List<String> EVENT_NAMES = List.of("event1");
    private static final String QUEUE_URI = "tcp://localhost:61616";
    private static final JMSException jmsException = new JMSException("Test");
    private static final MessageConsumer messageConsumer = mock(MessageConsumer.class);

    @Mock
    private JmsMessageConsumerPool jmsMessageConsumerPool;
    @Mock
    private JmsMessageReader jmsMessageReader;
    @Mock
    private ToStringMessageConverter toStringMessageConverter;
    @Mock
    private ToJsonEnvelopeMessageConverter toJsonEnvelopeMessageConverter;
    @Mock
    private ToJsonPathMessageConverter toJsonPathMessageConverter;

    private JmsMessageConsumerClient jmsMessageConsumerClient;

    @BeforeEach
    void setUp() {
        jmsMessageConsumerClient = new JmsMessageConsumerClient(jmsMessageConsumerPool,
                jmsMessageReader,
                toStringMessageConverter,
                toJsonEnvelopeMessageConverter,
                toJsonPathMessageConverter);
    }

    @Test
    void shouldStartConsumer() {
        when(jmsMessageConsumerPool.getOrCreateMessageConsumer(TOPIC_NAME, QUEUE_URI, EVENT_NAMES)).thenReturn(messageConsumer);

        jmsMessageConsumerClient.startConsumer(TOPIC_NAME, EVENT_NAMES);

        assertThat(getValueOfField(jmsMessageConsumerClient, "messageConsumer", MessageConsumer.class), is(messageConsumer));
    }

    @Nested
    class RetrieveMessageTest {

        @BeforeEach
        void setUp() {
            setField(jmsMessageConsumerClient, "messageConsumer", messageConsumer);
        }

        @Nested
        class NoWaitTest {
            @Test
            void shouldDelegate() throws Exception {
                when(jmsMessageReader.retrieveMessageNoWait(messageConsumer, toStringMessageConverter)).thenReturn(of("message"));

                final Optional<String> result = jmsMessageConsumerClient.retrieveMessageNoWait();

                assertThat(result, is(of("message")));
            }

            @Test
            void shouldConvertJmsException() throws Exception {
                doThrow(jmsException).when(jmsMessageReader).retrieveMessageNoWait(messageConsumer, toStringMessageConverter);

                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessageNoWait());

                assertThat(e.getMessage(), is("Failed to read message"));
                assertThat(e.getCause(), is(jmsException));
            }

            @Test
            void shouldThrowExceptionWhenMessageConsumerNotStarted() {
                setField(jmsMessageConsumerClient, "messageConsumer", null);
                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessageNoWait());

                assertThat(e.getMessage(), is("MessageConsumer is not started. Please call startConsumer(...) first"));
            }
        }

        @Nested
        class WithDefaultTimeoutTest {

            @Test
            void shouldDelegate() throws Exception {
                setField(jmsMessageConsumerClient, "messageConsumer", messageConsumer);
                when(jmsMessageReader.retrieveMessage(messageConsumer, toStringMessageConverter, 20_000)).thenReturn(of("message"));

                final Optional<String> result = jmsMessageConsumerClient.retrieveMessage();

                assertThat(result, is(of("message")));
            }

            @Test
            void shouldConvertJmsException() throws Exception {
                setField(jmsMessageConsumerClient, "messageConsumer", messageConsumer);
                doThrow(jmsException).when(jmsMessageReader).retrieveMessage(messageConsumer, toStringMessageConverter, 20_000);

                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessage());

                assertThat(e.getMessage(), is("Failed to read message"));
                assertThat(e.getCause(), is(jmsException));
            }

            @Test
            void shouldThrowExceptionWhenMessageConsumerNotStarted() {
                setField(jmsMessageConsumerClient, "messageConsumer", null);
                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessage());

                assertThat(e.getMessage(), is("MessageConsumer is not started. Please call startConsumer(...) first"));
            }
        }

        @Nested
        class WithTimeoutTest {

            @Test
            void shouldDelegate() throws Exception {
                setField(jmsMessageConsumerClient, "messageConsumer", messageConsumer);
                when(jmsMessageReader.retrieveMessage(messageConsumer, toStringMessageConverter, 1000)).thenReturn(of("message"));

                final Optional<String> result = jmsMessageConsumerClient.retrieveMessage(1000);

                assertThat(result, is(of("message")));
            }

            @Test
            void shouldConvertJmsException() throws Exception {
                setField(jmsMessageConsumerClient, "messageConsumer", messageConsumer);
                doThrow(jmsException).when(jmsMessageReader).retrieveMessage(messageConsumer, toStringMessageConverter, 1000);

                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessage(1000));

                assertThat(e.getMessage(), is("Failed to read message"));
                assertThat(e.getCause(), is(jmsException));
            }

            @Test
            void shouldThrowExceptionWhenMessageConsumerNotStarted() {
                setField(jmsMessageConsumerClient, "messageConsumer", null);
                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessage(1000));

                assertThat(e.getMessage(), is("MessageConsumer is not started. Please call startConsumer(...) first"));
            }
        }
    }

    @Nested
    class RetrieveMessageAsJsonEnvelopeTest {
        final JsonEnvelope jsonEnvelope = mock(JsonEnvelope.class);

        @BeforeEach
        void setUp() {
            setField(jmsMessageConsumerClient, "messageConsumer", messageConsumer);
        }

        @Nested
        class NoWaitTest {

            @Test
            void shouldDelegate() throws Exception {
                when(jmsMessageReader.retrieveMessageNoWait(messageConsumer, toJsonEnvelopeMessageConverter)).thenReturn(of(jsonEnvelope));

                final Optional<JsonEnvelope> result = jmsMessageConsumerClient.retrieveMessageAsJsonEnvelopeNoWait();

                assertThat(result, is(of(jsonEnvelope)));
            }

            @Test
            void shouldConvertJmsException() throws Exception {
                doThrow(jmsException).when(jmsMessageReader).retrieveMessageNoWait(messageConsumer, toJsonEnvelopeMessageConverter);

                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessageAsJsonEnvelopeNoWait());

                assertThat(e.getMessage(), is("Failed to read message"));
                assertThat(e.getCause(), is(jmsException));
            }

            @Test
            void shouldThrowExceptionWhenMessageConsumerNotStarted() {
                setField(jmsMessageConsumerClient, "messageConsumer", null);
                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessageAsJsonEnvelopeNoWait());

                assertThat(e.getMessage(), is("MessageConsumer is not started. Please call startConsumer(...) first"));
            }
        }

        @Nested
        class WithDefaultTimeoutTest {
            @Test
            void shouldDelegate() throws Exception {
                when(jmsMessageReader.retrieveMessage(messageConsumer, toJsonEnvelopeMessageConverter, 20_000)).thenReturn(of(jsonEnvelope));

                final Optional<JsonEnvelope> result = jmsMessageConsumerClient.retrieveMessageAsJsonEnvelope();

                assertThat(result, is(of(jsonEnvelope)));
            }

            @Test
            void shouldConvertJmsException() throws Exception {
                doThrow(jmsException).when(jmsMessageReader).retrieveMessage(messageConsumer, toJsonEnvelopeMessageConverter, 20_000);

                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessageAsJsonEnvelope());

                assertThat(e.getMessage(), is("Failed to read message"));
                assertThat(e.getCause(), is(jmsException));
            }

            @Test
            void shouldThrowExceptionWhenMessageConsumerNotStarted() {
                setField(jmsMessageConsumerClient, "messageConsumer", null);
                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessageAsJsonEnvelope());

                assertThat(e.getMessage(), is("MessageConsumer is not started. Please call startConsumer(...) first"));
            }
        }

        @Nested
        class WithTimeoutTest {

            @Test
            void shouldDelegate() throws Exception {
                when(jmsMessageReader.retrieveMessage(messageConsumer, toJsonEnvelopeMessageConverter, 1000)).thenReturn(of(jsonEnvelope));

                final Optional<JsonEnvelope> result = jmsMessageConsumerClient.retrieveMessageAsJsonEnvelope(1000);

                assertThat(result, is(of(jsonEnvelope)));
            }

            @Test
            void shouldConvertJmsException() throws Exception {
                doThrow(jmsException).when(jmsMessageReader).retrieveMessage(messageConsumer, toJsonEnvelopeMessageConverter, 1000);

                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessageAsJsonEnvelope(1000));

                assertThat(e.getMessage(), is("Failed to read message"));
                assertThat(e.getCause(), is(jmsException));
            }

            @Test
            void shouldThrowExceptionWhenMessageConsumerNotStarted() {
                setField(jmsMessageConsumerClient, "messageConsumer", null);
                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessageAsJsonEnvelope(1000));

                assertThat(e.getMessage(), is("MessageConsumer is not started. Please call startConsumer(...) first"));
            }
        }
    }

    @Nested
    class RetrieveMessageAsJsonPathTest {

        final JsonPath jsonPath = mock(JsonPath.class);

        @BeforeEach
        void setUp() {
            setField(jmsMessageConsumerClient, "messageConsumer", messageConsumer);
        }

        @Nested
        class NoWaitTest {
            @Test
            void shouldDelegate() throws Exception {
                when(jmsMessageReader.retrieveMessageNoWait(messageConsumer, toJsonPathMessageConverter)).thenReturn(of(jsonPath));

                final Optional<JsonPath> result = jmsMessageConsumerClient.retrieveMessageAsJsonPathNoWait();

                assertThat(result, is(of(jsonPath)));
            }

            @Test
            void shouldConvertJmsException() throws Exception {
                doThrow(jmsException).when(jmsMessageReader).retrieveMessageNoWait(messageConsumer, toJsonPathMessageConverter);

                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessageAsJsonPathNoWait());

                assertThat(e.getMessage(), is("Failed to read message"));
                assertThat(e.getCause(), is(jmsException));
            }

            @Test
            void shouldThrowExceptionWhenMessageConsumerNotStarted() {
                setField(jmsMessageConsumerClient, "messageConsumer", null);
                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessageAsJsonPathNoWait());

                assertThat(e.getMessage(), is("MessageConsumer is not started. Please call startConsumer(...) first"));
            }
        }

        @Nested
        class WithDefaultTimeoutTest {

            @Test
            void shouldDelegate() throws Exception {
                when(jmsMessageReader.retrieveMessage(messageConsumer, toJsonPathMessageConverter, 20_000)).thenReturn(of(jsonPath));

                final Optional<JsonPath> result = jmsMessageConsumerClient.retrieveMessageAsJsonPath();

                assertThat(result, is(of(jsonPath)));
            }

            @Test
            void shouldConvertJmsException() throws Exception {
                doThrow(jmsException).when(jmsMessageReader).retrieveMessage(messageConsumer, toJsonPathMessageConverter, 20_000);

                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessageAsJsonPath());

                assertThat(e.getMessage(), is("Failed to read message"));
                assertThat(e.getCause(), is(jmsException));
            }

            @Test
            void shouldThrowExceptionWhenMessageConsumerNotStarted() {
                setField(jmsMessageConsumerClient, "messageConsumer", null);
                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessageAsJsonPath());

                assertThat(e.getMessage(), is("MessageConsumer is not started. Please call startConsumer(...) first"));
            }
        }

        @Nested
        class WithTimeoutTest {

            @Test
            void shouldDelegate() throws Exception {
                when(jmsMessageReader.retrieveMessage(messageConsumer, toJsonPathMessageConverter, 1000)).thenReturn(of(jsonPath));

                final Optional<JsonPath> result = jmsMessageConsumerClient.retrieveMessageAsJsonPath(1000);

                assertThat(result, is(of(jsonPath)));
            }

            @Test
            void shouldConvertJmsException() throws Exception {
                doThrow(jmsException).when(jmsMessageReader).retrieveMessage(messageConsumer, toJsonPathMessageConverter, 1000);

                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessageAsJsonPath(1000));

                assertThat(e.getMessage(), is("Failed to read message"));
                assertThat(e.getCause(), is(jmsException));
            }

            @Test
            void shouldThrowExceptionWhenMessageConsumerNotStarted() {
                setField(jmsMessageConsumerClient, "messageConsumer", null);
                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessageAsJsonPath(1000));

                assertThat(e.getMessage(), is("MessageConsumer is not started. Please call startConsumer(...) first"));
            }
        }

        @Nested
        class ReadMultipleMessagesTest {

            @Test
            void shouldReadMultipleMessages() throws Exception {
                final JsonPath jsonPath1 = mock(JsonPath.class);
                final JsonPath jsonPath2 = mock(JsonPath.class);
                when(jmsMessageReader.retrieveMessage(messageConsumer, toJsonPathMessageConverter, 20_000))
                        .thenReturn(of(jsonPath1))
                        .thenReturn(of(jsonPath2));

                final List<JsonPath> result = jmsMessageConsumerClient.retrieveMessagesAsJsonPath(2);

                assertThat(result.size(), is(2));
                assertThat(result, hasItems(jsonPath1, jsonPath2));
            }

            @Test
            void shouldReturnNullWhenJmsReaderReturnsEmpty() throws Exception {
                when(jmsMessageReader.retrieveMessage(messageConsumer, toJsonPathMessageConverter, 20_000)).thenReturn(empty());

                final List<JsonPath> result = jmsMessageConsumerClient.retrieveMessagesAsJsonPath(1);

                assertNull(result.get(0));
            }

            @Test
            void shouldThrowExceptionWhenMessageConsumerNotStarted() {
                setField(jmsMessageConsumerClient, "messageConsumer", null);
                final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.retrieveMessagesAsJsonPath(1));

                assertThat(e.getMessage(), is("MessageConsumer is not started. Please call startConsumer(...) first"));
            }
        }
    }

    @Nested
    class ClearMessagesTest {

        @BeforeEach
        void setUp() {
            setField(jmsMessageConsumerClient, "messageConsumer", messageConsumer);
        }

        @Test
        void shouldDelegate() throws Exception {
            jmsMessageConsumerClient.clearMessages();

            verify(jmsMessageReader).clear(messageConsumer);
        }

        @Test
        void shouldConvertJmsException() throws Exception {
            doThrow(jmsException).when(jmsMessageReader).clear(messageConsumer);

            final JmsMessagingClientException e = assertThrows(JmsMessagingClientException.class, () -> jmsMessageConsumerClient.clearMessages());

            assertThat(e.getMessage(), is("Failed to clear messages"));
        }
    }
}