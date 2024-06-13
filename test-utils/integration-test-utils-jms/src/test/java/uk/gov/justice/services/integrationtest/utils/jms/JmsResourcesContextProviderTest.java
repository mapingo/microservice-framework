package uk.gov.justice.services.integrationtest.utils.jms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getField;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

class JmsResourcesContextProviderTest {

    @Test
    void shouldImplementJunitGlobalStoreCloseableResource() {
        assertTrue(CloseableResource.class.isAssignableFrom(JmsResourcesContextProvider.class));
    }

    @Test
    void shouldCreateContextAndReturnIfNotExist() {
        final JmsResourcesContext jmsResourcesContext = new JmsResourcesContextProvider().get();

        assertNotNull(jmsResourcesContext);
        assertNotNull(getValueOfField(jmsResourcesContext, "jmsMessageConsumerPool", JmsMessageConsumerPool.class));
        assertNotNull(getValueOfField(jmsResourcesContext, "jmsMessageProducerFactory", JmsMessageProducerFactory.class));
        assertNotNull(getValueOfField(jmsResourcesContext, "jmsMessageClientFactory", JmsMessageClientFactory.class));
    }

    @Test
    void shouldReturnCachedInstance() {
        final JmsResourcesContextProvider jmsResourcesContextProvider = new JmsResourcesContextProvider();

        final JmsResourcesContext jmsResourcesContext1 = jmsResourcesContextProvider.get();
        final JmsResourcesContext jmsResourcesContext2 = jmsResourcesContextProvider.get();

        assertThat(jmsResourcesContext1, is(jmsResourcesContext2));
    }

    @Test
    void shouldReturnSameInstanceWhenRetrievedFromDifferentInstancesOfProvider() {
        final JmsResourcesContext jmsResourcesContext1 = new JmsResourcesContextProvider().get();
        final JmsResourcesContext jmsResourcesContext2 = new JmsResourcesContextProvider().get();

        assertThat(jmsResourcesContext1, is(jmsResourcesContext2));
    }

    @Test
    void shouldInvokeCloseOnCachedInstanceAndClearCachedInstance() throws Exception {
        final JmsResourcesContextProvider jmsResourcesContextProvider = new JmsResourcesContextProvider();
        final JmsResourcesContext jmsResourcesContext = mock(JmsResourcesContext.class);
        setStaticField(JmsResourcesContextProvider.class, "jmsResourcesContext", jmsResourcesContext);

        jmsResourcesContextProvider.close();

        verify(jmsResourcesContext).close();
        assertNull(getStaticFieldValue(JmsResourcesContextProvider.class, "jmsResourcesContext"));
    }

    @Test
    void closeShouldDoNothingWhenCachedInstanceIsNull() {
        final JmsResourcesContextProvider jmsResourcesContextProvider = new JmsResourcesContextProvider();

        jmsResourcesContextProvider.close();
    }

    private void setStaticField(final Class<?> clazz, final String fieldName, final Object value) throws Exception {
        final Field field = getField(clazz, fieldName);
        field.setAccessible(true);
        field.set(null, value);
    }

    private Object getStaticFieldValue(final Class<?> clazz, final String fieldName) throws Exception {
        final Field field = getField(clazz, fieldName);
        field.setAccessible(true);
        return field.get(null);
    }
}