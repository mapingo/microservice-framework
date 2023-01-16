package uk.gov.justice.framework.command.client.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SleeperTest {

    @InjectMocks
    private Sleeper sleeper;

    @Test
    public void shouldSleepForTheRequiredNumberOfMillseconds() throws Exception {

        final long sleepTime = 1_000;

        final long startTime = System.currentTimeMillis();

        sleeper.sleepFor(sleepTime);

        final long endTime = System.currentTimeMillis();

        assertThat(endTime - startTime, is(greaterThanOrEqualTo(sleepTime)));
    }
}
