package uk.gov.justice.services.common.http;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.common.http.DefaultServerPortProvider.DEFAULT_PORT;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class DefaultServerPortProviderTest {

    @InjectMocks
    private DefaultServerPortProvider defaultServerPortProvider;

    @BeforeEach
    @AfterEach
    public void clearSystemProperty() {
        System.clearProperty(DEFAULT_PORT);
    }

    @Test
    public void shouldReturn8080IfDefaultPortNotSet() throws Exception {

        assertThat(System.getProperty(DEFAULT_PORT), is(nullValue()));
        assertThat(defaultServerPortProvider.getDefaultPort(), is("8080"));
    }

    @Test
    public void shouldReturnSystemPropertyDefaultPortIfSet() throws Exception {

        final String defaultPort = "827364";

        System.setProperty(DEFAULT_PORT, defaultPort);
        assertThat(defaultServerPortProvider.getDefaultPort(), is(defaultPort));
    }
}
