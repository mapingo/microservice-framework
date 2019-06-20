package uk.gov.justice;

import static uk.gov.justice.CatchUpAndShutteringManager.main;

import org.junit.Before;
import org.junit.Test;

public class CatchUpAndShutteringManagerTest {
    private String host;
    private String port;

    @Before
    public void before() {
        host = "-localhost";
        port = "-9999";
    }

    @Test
    public void shouldReturnErrorWhenInvokingMain() {
        final String [] args = {"-ch", "SHUTTER", "-ho", host, "-p", port};

        main(args);
    }
}