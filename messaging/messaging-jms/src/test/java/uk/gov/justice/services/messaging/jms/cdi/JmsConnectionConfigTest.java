package uk.gov.justice.services.messaging.jms.cdi;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class JmsConnectionConfigTest {


    @InjectMocks
    private JmsConnectionConfig jmsConnectionConfig;

    @Test
    public void shouldGetTheJndiConfigValueAsABoolean() throws Exception {

        setField(jmsConnectionConfig, "shouldUseSeparateAuditMessageBroker", "true");
        assertThat(jmsConnectionConfig.shouldUseSeparateAuditMessageBroker(), is(true));

        setField(jmsConnectionConfig, "shouldUseSeparateAuditMessageBroker", "True");
        assertThat(jmsConnectionConfig.shouldUseSeparateAuditMessageBroker(), is(true));

        setField(jmsConnectionConfig, "shouldUseSeparateAuditMessageBroker", "TrUe");
        assertThat(jmsConnectionConfig.shouldUseSeparateAuditMessageBroker(), is(true));

        setField(jmsConnectionConfig, "shouldUseSeparateAuditMessageBroker", "false");
        assertThat(jmsConnectionConfig.shouldUseSeparateAuditMessageBroker(), is(false));

        setField(jmsConnectionConfig, "shouldUseSeparateAuditMessageBroker", "false");
        assertThat(jmsConnectionConfig.shouldUseSeparateAuditMessageBroker(), is(false));

        setField(jmsConnectionConfig, "shouldUseSeparateAuditMessageBroker", null);
        assertThat(jmsConnectionConfig.shouldUseSeparateAuditMessageBroker(), is(false));

        setField(jmsConnectionConfig, "shouldUseSeparateAuditMessageBroker", "");
        assertThat(jmsConnectionConfig.shouldUseSeparateAuditMessageBroker(), is(false));

        setField(jmsConnectionConfig, "shouldUseSeparateAuditMessageBroker", "something silly");
        assertThat(jmsConnectionConfig.shouldUseSeparateAuditMessageBroker(), is(false));
    }
}
