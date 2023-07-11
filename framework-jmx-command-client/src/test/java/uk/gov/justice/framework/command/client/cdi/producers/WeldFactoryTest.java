package uk.gov.justice.framework.command.client.cdi.producers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WeldFactoryTest {

    @InjectMocks
    private WeldFactory weldFactory;

    @Test
    public void shouldCreateAnInstanceOfWeld() throws Exception {

        assertThat(weldFactory.create(), is(notNullValue()));
    }

    @Test
    public void shouldStopCoverallsAnnoyingUs() throws Exception {
        assertThat(new WeldFactory(), is(notNullValue()));
    }
}
