package uk.gov.justice.services.messaging.jms;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EnvelopeSenderSelectorTest {

    @Mock
    private JmsSender jmsSender;

    @Mock
    private ShutteringStoreSender shutteringStoreSender;

    @InjectMocks
    private EnvelopeSenderSelector envelopeSenderSelector;

    @Test
    public void shouldGetTheJmsSenderByDefault() throws Exception {

        assertThat(envelopeSenderSelector.getEnvelopeSender(), is(jmsSender));
    }

    @Test
    public void shouldGetTheShutteringStoreSenderIfSuspendedIsSetToTrue() throws Exception {

        assertThat(envelopeSenderSelector.getEnvelopeSender(), is(jmsSender));

        envelopeSenderSelector.setSuspended(true);
        assertThat(envelopeSenderSelector.getEnvelopeSender(), is(shutteringStoreSender));

        envelopeSenderSelector.setSuspended(false);
        assertThat(envelopeSenderSelector.getEnvelopeSender(), is(jmsSender));
    }
}
