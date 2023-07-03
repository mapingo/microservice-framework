package uk.gov.justice.services.management.suspension.commands;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UnsuspendCommandTest {

    @InjectMocks
    private UnsuspendCommand unsuspendCommand;

    @Test
    public void shouldBeUnsuspendCommand() throws Exception {

        assertThat(unsuspendCommand.isSuspendCommand(), is(false));
    }
}
