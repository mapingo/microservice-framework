package uk.gov.justice.framework.command.client;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.jmx.api.mbean.CommandRunMode.FORCED;
import static uk.gov.justice.services.jmx.api.mbean.CommandRunMode.GUARDED;

import uk.gov.justice.services.jmx.api.mbean.CommandRunMode;

import org.apache.commons.cli.CommandLine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommandRunModeSelectorTest {

    @InjectMocks
    private CommandRunModeSelector commandRunModeSelector;

    @Test
    public void shouldReturnForcedModeIfTheCommandLineOptionIsForce() {

        final CommandLine commandLine = mock(CommandLine.class);
        when(commandLine.hasOption("force")).thenReturn(true);

        final CommandRunMode commandRunMode = commandRunModeSelector.selectCommandRunMode(commandLine);

        assertThat(commandRunMode, is(FORCED));
    }

    @Test
    public void shouldReturnGuardedModeIfTheCommandLineOptionIsNotForce() {

        final CommandLine commandLine = mock(CommandLine.class);
        when(commandLine.hasOption("force")).thenReturn(false);

        final CommandRunMode commandRunMode = commandRunModeSelector.selectCommandRunMode(commandLine);

        assertThat(commandRunMode, is(GUARDED));
    }
}