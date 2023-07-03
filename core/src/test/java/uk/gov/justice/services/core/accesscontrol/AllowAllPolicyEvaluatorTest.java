package uk.gov.justice.services.core.accesscontrol;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import uk.gov.justice.services.messaging.JsonEnvelope;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AllowAllPolicyEvaluatorTest {

    @InjectMocks
    private AllowAllPolicyEvaluator allowAllAccessController;

    @Test
    public void shouldAllowAllAccess() throws Exception {

        final JsonEnvelope jsonEnvelope = mock(JsonEnvelope.class);

        assertThat(allowAllAccessController.checkAccessPolicyFor("command", jsonEnvelope).isPresent(), is(false));
    }
}
