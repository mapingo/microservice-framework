package uk.gov.justice.services.jmx.state.observers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import uk.gov.justice.services.jmx.state.events.SystemCommandStateChangedEvent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SystemCommandStateObserverTest {

    @Mock
    private SystemCommandStateHandler systemCommandStateHandler;

    @InjectMocks
    private SystemCommandStateObserver systemCommandStateObserver;

    @Test
    public void shouldProcessSystemCommandInProgressEvent() throws Exception {

        final SystemCommandStateChangedEvent systemCommandStateChangedEvent = mock(SystemCommandStateChangedEvent.class);

        systemCommandStateObserver.onSystemCommandStateChanged(systemCommandStateChangedEvent);

        verify(systemCommandStateHandler).handleSystemCommandStateChanged(systemCommandStateChangedEvent);
    }
}
