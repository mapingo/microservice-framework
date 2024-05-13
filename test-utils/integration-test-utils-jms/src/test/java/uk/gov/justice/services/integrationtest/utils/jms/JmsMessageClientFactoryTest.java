package uk.gov.justice.services.integrationtest.utils.jms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToJsonEnvelopeMessageConverter;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToJsonPathMessageConverter;
import uk.gov.justice.services.integrationtest.utils.jms.converters.ToStringMessageConverter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

@ExtendWith(MockitoExtension.class)
class JmsMessageClientFactoryTest {

    @Mock
    private JmsMessageProducerFactory jmsMessageProducerFactory;
    @Mock
    private ToStringMessageConverter toStringMessageConverter;
    @Mock
    private ToJsonEnvelopeMessageConverter toJsonEnvelopeMessageConverter;
    @Mock
    private ToJsonPathMessageConverter toJsonPathMessageConverter;
    @Mock
    private JmsMessageReader jmsMessageReader;
    @Mock
    private JmsMessageConsumerPool jmsMessageConsumerPool;

    @InjectMocks
    private JmsMessageClientFactory jmsMessageClientFactory;

    @Test
    void shouldCreateMessageProducerClient() {
        final JmsMessageProducerClient jmsMessageProducerClient = jmsMessageClientFactory.createJmsMessageProducerClient();

        assertNotNull(jmsMessageProducerClient);
        assertThat(getValueOfField(jmsMessageProducerClient, "jmsMessageProducerFactory", JmsMessageProducerFactory.class), is(jmsMessageProducerFactory));
    }

    @Test
    void shouldCreateMessageConsumerClient() {
        final JmsMessageConsumerClient jmsMessageConsumerClient = jmsMessageClientFactory.createJmsMessageConsumerClient();

        assertNotNull(jmsMessageConsumerClient);
        assertThat(getValueOfField(jmsMessageConsumerClient, "jmsMessageConsumerPool", JmsMessageConsumerPool.class), is(jmsMessageConsumerPool));
        assertThat(getValueOfField(jmsMessageConsumerClient, "jmsMessageReader", JmsMessageReader.class), is(jmsMessageReader));
        assertThat(getValueOfField(jmsMessageConsumerClient, "toStringMessageConverter", ToStringMessageConverter.class), is(toStringMessageConverter));
        assertThat(getValueOfField(jmsMessageConsumerClient, "toJsonEnvelopeMessageConverter", ToJsonEnvelopeMessageConverter.class), is(toJsonEnvelopeMessageConverter));
        assertThat(getValueOfField(jmsMessageConsumerClient, "toJsonPathMessageConverter", ToJsonPathMessageConverter.class), is(toJsonPathMessageConverter));
    }
}