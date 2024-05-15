package uk.gov.justice.services.integrationtest.utils.jms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;
import static org.mockito.Mockito.*;
import static uk.gov.justice.services.integrationtest.utils.jms.JmsResourceManagementExtension.TEST_SUITE_SHUTDOWN_EXECUTION_TIMER_HOOK;
import static uk.gov.justice.services.integrationtest.utils.jms.JmsResourceManagementExtension.TEST_SUITE_SHUTDOWN_JMS_RESOURCE_CLEANUP_HOOK;

@ExtendWith(MockitoExtension.class)
class JmsResourceManagementExtensionTest {

    @Mock
    private JmsResourcesContextProvider jmsResourcesContextProvider;

    private JmsResourceManagementExtension jmsResourceManagementExtension;

    @BeforeEach
    void setUp() {
        jmsResourceManagementExtension = new JmsResourceManagementExtension(jmsResourcesContextProvider);
    }

    @Test
    void shouldHaveDefaultConstructorForJunitExtensionToWork() {
        final JmsResourceManagementExtension result = new JmsResourceManagementExtension();

        assertNotNull(result);
    }

    @Test
    void clearMessagesBeforeEachTestMethod() {
        final JmsResourcesContext jmsResourcesContext = mock(JmsResourcesContext.class);
        when(jmsResourcesContextProvider.get()).thenReturn(jmsResourcesContext);

        jmsResourceManagementExtension.beforeEach(null);

        verify(jmsResourcesContext).clearMessages();
    }

    @Test
    void closeAllMessageConsumersAndProducersAfterAllTestsInATestClass() {
        final JmsResourcesContext jmsResourcesContext = mock(JmsResourcesContext.class);
        when(jmsResourcesContextProvider.get()).thenReturn(jmsResourcesContext);

        jmsResourceManagementExtension.afterAll(null);

        verify(jmsResourcesContext).closeConsumersAndProducers();
    }

    @Nested
    class BeforeAllTest {

        @Test
        void registerTestSuiteShutdownHooksIfNotRegistered() throws Exception {
            final ExtensionContext extensionContext = mock(ExtensionContext.class);
            final ExtensionContext rootContext = mock(ExtensionContext.class);
            final ExtensionContext.Store store = mock(ExtensionContext.Store.class);
            when(extensionContext.getRoot()).thenReturn(rootContext);
            when(rootContext.getStore(GLOBAL)).thenReturn(store);
            when(store.get(TEST_SUITE_SHUTDOWN_JMS_RESOURCE_CLEANUP_HOOK)).thenReturn(null);
            when(store.get(TEST_SUITE_SHUTDOWN_EXECUTION_TIMER_HOOK)).thenReturn(null);

            jmsResourceManagementExtension.beforeAll(extensionContext);

            verify(store).put(TEST_SUITE_SHUTDOWN_JMS_RESOURCE_CLEANUP_HOOK, jmsResourcesContextProvider);
            verify(store).put(eq(TEST_SUITE_SHUTDOWN_EXECUTION_TIMER_HOOK), any(TestSuiteExecutionTimeCalculator.class));
        }

        @Test
        void doNotRegisterTestSuiteShutdownHooksIfAlreadyRegistered() throws Exception {
            final ExtensionContext extensionContext = mock(ExtensionContext.class);
            final ExtensionContext rootContext = mock(ExtensionContext.class);
            final ExtensionContext.Store store = mock(ExtensionContext.Store.class);
            when(extensionContext.getRoot()).thenReturn(rootContext);
            when(rootContext.getStore(GLOBAL)).thenReturn(store);
            when(store.get(TEST_SUITE_SHUTDOWN_JMS_RESOURCE_CLEANUP_HOOK)).thenReturn(jmsResourcesContextProvider);
            when(store.get(TEST_SUITE_SHUTDOWN_EXECUTION_TIMER_HOOK)).thenReturn(new TestSuiteExecutionTimeCalculator());

            jmsResourceManagementExtension.beforeAll(extensionContext);

            verify(store, times(0)).put(any(), any());
        }
    }
}