package uk.gov.justice.services.integrationtest.utils.jms;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TestSuiteExecutionTimeCalculatorTest {

    @Mock
    private StopWatch stopWatch;

    @Test
    void shouldStartStopWatchThroughConstructor() {
        new TestSuiteExecutionTimeCalculator(stopWatch);

        verify(stopWatch).start();
    }

    @Test
    void shouldStopAndLogTimeWhenClosed() {
        final TestSuiteExecutionTimeCalculator calculator = new TestSuiteExecutionTimeCalculator(stopWatch);

        calculator.close();

        verify(stopWatch).stop();
        verify(stopWatch).getTime(TimeUnit.SECONDS);
    }
}