package uk.gov.justice.services.jmx.bootstrap.blacklist;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmptyBlackListedCommandsTest {

    @InjectMocks
    private EmptyBlackListedCommands emptyBlackListedCommands;

    @Test
    public void shouldReturnEmptyByDefault() throws Exception {
        assertThat(emptyBlackListedCommands.getBlackListedCommands().isEmpty(), is(true));
    }
}
