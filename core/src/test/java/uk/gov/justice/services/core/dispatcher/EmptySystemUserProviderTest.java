package uk.gov.justice.services.core.dispatcher;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class EmptySystemUserProviderTest {

    @Mock
    private Logger logger;

    @InjectMocks
    final SystemUserProvider provider = new EmptySystemUserProvider();


    @Test
    public void shouldReturnEmptyOptional() throws Exception {
        assertThat(provider.getContextSystemUserId(), is(Optional.empty()));
    }

    @Test
    public void shouldLogError() {
        provider.getContextSystemUserId();
        verify(logger).error("Could not fetch system user. system-users-library not available in the classpath");
    }

}