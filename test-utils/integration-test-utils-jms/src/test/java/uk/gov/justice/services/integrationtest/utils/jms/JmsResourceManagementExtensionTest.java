package uk.gov.justice.services.integrationtest.utils.jms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;
import static org.mockito.Mockito.*;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getField;

@ExtendWith(MockitoExtension.class)
class JmsResourceManagementExtensionTest {

    @Mock
    private JmsMessageConsumerPool jmsMessageConsumerPool;

    @Mock
    private JmsMessageProducerFactory jmsMessageProducerFactory;

    private JmsResourceManagementExtension jmsResourceManagementExtension;

    @BeforeEach
    void setUp() {
        jmsResourceManagementExtension = new JmsResourceManagementExtension(jmsMessageConsumerPool, jmsMessageProducerFactory);
    }

    @Test
    void shouldHaveDefaultConstructorForJunitExtensionToWork() {
        final JmsResourceManagementExtension result = new JmsResourceManagementExtension();

        assertNotNull(result);
    }

    @Test
    void clearMessagesBeforeEachTestMethod() {
        jmsResourceManagementExtension.beforeEach(null);

        verify(jmsMessageConsumerPool).clearMessages();
    }

    @Test
    void closeAllMessageConsumersAfterAllTestsInATestClass() {
        jmsResourceManagementExtension.afterAll(null);

        verify(jmsMessageConsumerPool).closeConsumers();
    }

    @Test
    void closeConsumerPoolAndProducerFactoryAfterCompletionOfTestSuite() {
        jmsResourceManagementExtension.close();

        verify(jmsMessageConsumerPool).close();
        verify(jmsMessageProducerFactory).close();
    }

    @Test
    void registerShutdownMethodThroughBeforeAllHook() throws Exception {
        setStaticField(JmsResourceManagementExtension.class, "registered", false);
        final ExtensionContext extensionContext = mock(ExtensionContext.class);
        final ExtensionContext rootContext = mock(ExtensionContext.class);
        final ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        when(extensionContext.getRoot()).thenReturn(rootContext);
        when(rootContext.getStore(GLOBAL)).thenReturn(store);

        jmsResourceManagementExtension.beforeAll(extensionContext);

        verify(store).put("Clean JMS resources", jmsResourceManagementExtension);
    }

    @Test
    void doNotRegisterShutdownMethodIfItAlreadyRegistered() throws Exception {
        setStaticField(JmsResourceManagementExtension.class, "registered", true);
        final ExtensionContext extensionContext = mock(ExtensionContext.class);

        jmsResourceManagementExtension.beforeAll(extensionContext);

        verifyNoInteractions(extensionContext);
    }

    private void setStaticField(final Class<?> clazz, final String fieldName, final Object value) throws Exception {
        final Field field = getField(clazz, fieldName);
        field.setAccessible(true);
        field.set(null, value);
    }
}