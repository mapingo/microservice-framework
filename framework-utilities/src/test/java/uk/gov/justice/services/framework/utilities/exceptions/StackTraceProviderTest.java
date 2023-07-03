package uk.gov.justice.services.framework.utilities.exceptions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StackTraceProviderTest {

    @InjectMocks
    private StackTraceProvider stackTraceProvider;

    @Test
    public void shouldGetTheStackTraceOfAnExceptionAsAString() throws Exception {

        final String stackTrace = stackTraceProvider.getStackTrace(new Exception("Ooops"));

        final String stackTracePrefix = "java.lang.Exception: Ooops\n" +
                "\tat uk.gov.justice.services.framework.utilities.exceptions.StackTraceProviderTest.shouldGetTheStackTraceOfAnExceptionAsAString(StackTraceProviderTest.java";

        assertThat(stackTrace.startsWith(stackTracePrefix), is(true));
    }
}
