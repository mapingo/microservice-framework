package uk.gov.justice.services.messaging.jms;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import org.junit.jupiter.api.Test;

public class JmsMessagingConfigurationTest {

    private final JmsMessagingConfiguration jmsMessagingConfiguration = new JmsMessagingConfiguration();

    @Test
    public void shouldGetOversizeMessageThresholdInBytes() throws Exception {
        final int oversizeMessageThresholdBytes = 1024;

        setField(jmsMessagingConfiguration, "oversizeMessageThresholdBytes", oversizeMessageThresholdBytes + "");

        assertThat(jmsMessagingConfiguration.getOversizeMessageThresholdBytes(), is(oversizeMessageThresholdBytes));
    }
}