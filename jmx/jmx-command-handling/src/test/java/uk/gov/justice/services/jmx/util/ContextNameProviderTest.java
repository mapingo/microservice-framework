package uk.gov.justice.services.jmx.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.common.configuration.ServiceContextNameProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ContextNameProviderTest {

    @Mock
    private ServiceContextNameProvider serviceContextNameProvider;

    @InjectMocks
    private ContextNameProvider contextNameProvider;

    @Test
    public void shouldGetTheNameOfTheContextAsTheAppNameUpToTheFirstDash() throws Exception {

        final String appName = "somecontext-service";

        when(serviceContextNameProvider.getServiceContextName()).thenReturn(appName);

        assertThat(contextNameProvider.getContextName(), is("somecontext"));
    }

    @Test
    public void shouldHandleContextNamesWithoutADash() throws Exception {

        final String appName = "somecontext";

        when(serviceContextNameProvider.getServiceContextName()).thenReturn(appName);

        assertThat(contextNameProvider.getContextName(), is(appName));
    }
}
