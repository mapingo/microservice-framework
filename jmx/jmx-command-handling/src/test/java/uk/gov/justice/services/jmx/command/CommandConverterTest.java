package uk.gov.justice.services.jmx.command;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.services.jmx.api.command.SystemCommand;
import uk.gov.justice.services.jmx.api.command.SystemCommandDetails;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommandConverterTest {

    @InjectMocks
    private CommandConverter commandConverter;

    @Test
    public void shouldConvertSystemCommandToSystemCommandDetails() throws Exception {

        final SystemCommand systemCommand = new TestCommand();

        final SystemCommandDetails systemCommandDetails = commandConverter.toCommandDetails(systemCommand);

        assertThat(systemCommandDetails.getName(), is(systemCommand.getName()));
        assertThat(systemCommandDetails.getDescription(), is(systemCommand.getDescription()));
    }
}
