package uk.gov.justice.services.core.dispatcher;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DispatcherConfigurationTest {

    @InjectMocks
    private DispatcherConfiguration dispatcherConfiguration;

    @Test
    public void shouldGetTheStartWaitTime() throws Exception {

        setField(dispatcherConfiguration, "validateRestResponseJson", "true");
        assertThat(dispatcherConfiguration.shouldValidateRestResponseJson(), is(true));

        setField(dispatcherConfiguration, "validateRestResponseJson", "TRUE");
        assertThat(dispatcherConfiguration.shouldValidateRestResponseJson(), is(true));

        setField(dispatcherConfiguration, "validateRestResponseJson", "True");
        assertThat(dispatcherConfiguration.shouldValidateRestResponseJson(), is(true));

        setField(dispatcherConfiguration, "validateRestResponseJson", "false");
        assertThat(dispatcherConfiguration.shouldValidateRestResponseJson(), is(false));
        
        setField(dispatcherConfiguration, "validateRestResponseJson", "FALSE");
        assertThat(dispatcherConfiguration.shouldValidateRestResponseJson(), is(false));

        setField(dispatcherConfiguration, "validateRestResponseJson", "False");
        assertThat(dispatcherConfiguration.shouldValidateRestResponseJson(), is(false));

        setField(dispatcherConfiguration, "validateRestResponseJson", null);
        assertThat(dispatcherConfiguration.shouldValidateRestResponseJson(), is(false));

        setField(dispatcherConfiguration, "validateRestResponseJson", "");
        assertThat(dispatcherConfiguration.shouldValidateRestResponseJson(), is(false));

        setField(dispatcherConfiguration, "validateRestResponseJson", "something silly");
        assertThat(dispatcherConfiguration.shouldValidateRestResponseJson(), is(false));
    }
}
