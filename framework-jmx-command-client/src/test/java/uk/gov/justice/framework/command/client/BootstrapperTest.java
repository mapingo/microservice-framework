package uk.gov.justice.framework.command.client;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.framework.command.client.ReturnCode.SUCCESS;

import uk.gov.justice.framework.command.client.startup.Bootstrapper;
import uk.gov.justice.framework.command.client.startup.ObjectFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BootstrapperTest {

    @Mock
    private ObjectFactory objectFactory;

    @InjectMocks
    private Bootstrapper bootstrapper;

    @SuppressWarnings("unchecked")
    @Test
    public void shouldStartTheWeldCdiContainerGetTheMainApplicationClassAndRun() throws Exception {

        final String[] args = {"some", "args"};
        final ReturnCode returnCode = SUCCESS;

        final MainApplication mainApplication = mock(MainApplication.class);

        when(objectFactory.mainApplication()).thenReturn(mainApplication);
        when(bootstrapper.startContainerAndRun(args)).thenReturn(returnCode);

        assertThat(bootstrapper.startContainerAndRun(args), is(returnCode));
        verify(mainApplication).run(args);
    }
}
