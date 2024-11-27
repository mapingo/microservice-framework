package uk.gov.justice.services.jmx.api.mbean;

import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.jmx.api.mbean.CommandRunMode.GUARDED;
import static uk.gov.justice.services.jmx.api.parameters.JmxCommandRuntimeParameters.withNoCommandParameters;

import uk.gov.justice.services.jmx.api.parameters.JmxCommandRuntimeParameters;
import uk.gov.justice.services.jmx.api.parameters.JmxCommandRuntimeParameters.JmxCommandRuntimeParametersBuilder;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class SystemCommanderTest {

    @Mock
    private JmxCommandRunner jmxCommandRunner;

    @Mock
    private Logger logger;

    @InjectMocks
    private SystemCommander systemCommander;

    @Test
    public void shouldRunJmxCommandWithTheCorrectParameters() throws Exception {

        final String systemCommandName = "some-command-name";
        final UUID commandRuntimeId = fromString("4e0b577b-917d-4862-b0bb-139b1a5b69bc");
        final UUID commandId = randomUUID();
        final String commandRuntimeString = "some-runtime-string";
        final boolean guarded = true;

        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = new JmxCommandRuntimeParametersBuilder()
                .withCommandRuntimeId(commandRuntimeId)
                .withCommandRuntimeString(commandRuntimeString)
                .build();

        when(jmxCommandRunner.run(systemCommandName, jmxCommandRuntimeParameters, GUARDED)).thenReturn(commandId);

        assertThat(systemCommander.call(
                systemCommandName,
                commandRuntimeId,
                commandRuntimeString,
                guarded
        ), is(commandId));

        verify(logger).info("Received System Command 'some-command-name'");
        verify(logger).info("Running 'some-command-name' in GUARDED mode with command-runtime-id '4e0b577b-917d-4862-b0bb-139b1a5b69bc' and command-runtime-string 'some-runtime-string'");
        verify(jmxCommandRunner).run(
                systemCommandName,
                jmxCommandRuntimeParameters,
                GUARDED
        );
    }

    @Test
    public void shouldHandleMissingCommandRuntimeId() throws Exception {

        final String systemCommandName = "some-command-name";
        final UUID commandId = randomUUID();
        final String commandRuntimeString = "some-runtime-string";
        final boolean guarded = true;

        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = new JmxCommandRuntimeParametersBuilder()
                .withCommandRuntimeString(commandRuntimeString)
                .build();

        when(jmxCommandRunner.run(systemCommandName, jmxCommandRuntimeParameters, GUARDED)).thenReturn(commandId);

        assertThat(systemCommander.call(
                systemCommandName,
                null,
                commandRuntimeString,
                guarded
        ), is(commandId));

        verify(logger).info("Received System Command 'some-command-name'");
        verify(logger).info("Running 'some-command-name' in GUARDED mode with command-runtime-string 'some-runtime-string'");
        verify(jmxCommandRunner).run(
                systemCommandName,
                jmxCommandRuntimeParameters,
                GUARDED
        );
    }

    @Test
    public void shouldHandleMissingCommandRuntimeString() throws Exception {

        final String systemCommandName = "some-command-name";
        final UUID commandId = randomUUID();
        final UUID commandRuntimeId = fromString("4e0b577b-917d-4862-b0bb-139b1a5b69bc");
        final boolean guarded = true;

        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = new JmxCommandRuntimeParametersBuilder()
                .withCommandRuntimeId(commandRuntimeId)
                .build();

        when(jmxCommandRunner.run(systemCommandName, jmxCommandRuntimeParameters, GUARDED)).thenReturn(commandId);

        assertThat(systemCommander.call(
                systemCommandName,
                commandRuntimeId,
                null,
                guarded
        ), is(commandId));

        verify(logger).info("Received System Command 'some-command-name'");
        verify(logger).info("Running 'some-command-name' in GUARDED mode with command-runtime-id '4e0b577b-917d-4862-b0bb-139b1a5b69bc'");
        verify(jmxCommandRunner).run(
                systemCommandName,
                jmxCommandRuntimeParameters,
                GUARDED
        );
    }

    @Test
    public void shouldHandleNoParameters() throws Exception {

        final String systemCommandName = "some-command-name";
        final UUID commandId = randomUUID();
        final boolean guarded = true;

        final JmxCommandRuntimeParameters jmxCommandRuntimeParameters = withNoCommandParameters();

        when(jmxCommandRunner.run(systemCommandName, jmxCommandRuntimeParameters, GUARDED)).thenReturn(commandId);

        assertThat(systemCommander.call(
                systemCommandName,
                null,
                null,
                guarded
        ), is(commandId));

        verify(logger).info("Received System Command 'some-command-name'");
        verify(logger).info("Running 'some-command-name' in GUARDED mode");
        verify(jmxCommandRunner).run(
                systemCommandName,
                withNoCommandParameters(),
                GUARDED
        );
    }
}