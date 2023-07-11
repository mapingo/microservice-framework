package uk.gov.justice.services.test.utils.common.stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class StreamCloseSpyTest {


    private StreamCloseSpy streamCloseSpy = new StreamCloseSpy();

    @Test
    public void shouldSetStreamClosedToTrueOnRun() throws Exception {

        assertThat(streamCloseSpy.streamClosed(), is(false));

        streamCloseSpy.run();

        assertThat(streamCloseSpy.streamClosed(), is(true));

    }
}
