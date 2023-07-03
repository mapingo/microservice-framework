package uk.gov.justice.services.management.suspension.commands;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SuspendCommandTest {

    @InjectMocks
    private SuspendCommand suspendCommand;

    @Test
    public void shouldBeSuspendCommand() throws Exception {

        assertThat(suspendCommand.isSuspendCommand(), is(true));
    }
}
