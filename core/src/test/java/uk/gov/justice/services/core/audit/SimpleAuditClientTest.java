package uk.gov.justice.services.core.audit;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.common.configuration.ServiceContextNameProvider;
import uk.gov.justice.services.messaging.JsonEnvelope;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class SimpleAuditClientTest {

    private static final String COMPONENT = "test-component";

    @Mock
    Logger logger;

    @Mock
    ServiceContextNameProvider serviceContextNameProvider;

    @InjectMocks
    private SimpleAuditClient simpleAuditClient;

    @Test
    public void shouldPrependTheAppNameToTheEnvelopeJsonAndLog() throws Exception {

        final String serviceContextName = "the-service-context-name";
        final String propertyValue = "value";

        final String envelopeJson = new JSONObject()
                .put("propertyName", propertyValue)
                .toString();

        final JsonEnvelope envelope =  mock(JsonEnvelope.class);

        when(envelope.toString()).thenReturn(envelopeJson);
        when(serviceContextNameProvider.getServiceContextName()).thenReturn(serviceContextName);

        simpleAuditClient.auditEntry(envelope, COMPONENT);

        final ArgumentCaptor<String> argumentCaptor = forClass(String.class);

        verify(logger).info(argumentCaptor.capture());

        final String json = argumentCaptor.getValue();

        with(json)
                .assertEquals("serviceContext", serviceContextName)
                .assertEquals("component", COMPONENT)
                .assertEquals("envelope.propertyName", propertyValue);
    }
}
