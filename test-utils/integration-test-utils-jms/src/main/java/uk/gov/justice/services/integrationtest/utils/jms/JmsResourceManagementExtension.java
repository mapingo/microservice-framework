package uk.gov.justice.services.integrationtest.utils.jms;

import com.google.common.annotations.VisibleForTesting;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

/**
 *  This manages Life cycle of Jms consumers and producers created by a Test class using various junit hooks.
 *  As long as this extension is used by an Integration test, as a developer of writing integration tests you don't need to worry about managing underlying jms resources that are created in Tests
 */
public class JmsResourceManagementExtension implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback {

    static final String TEST_SUITE_SHUTDOWN_JMS_RESOURCE_CLEANUP_HOOK = "JMS_RESOURCE_CLEANUP_HOOK";
    static final String TEST_SUITE_SHUTDOWN_EXECUTION_TIMER_HOOK = "EXECUTION_TIMER_HOOK";
    private final JmsResourcesContextProvider jmsResourcesContextProvider;

    public JmsResourceManagementExtension() {
        this(new JmsResourcesContextProvider());

    }

    @VisibleForTesting
    JmsResourceManagementExtension(final JmsResourcesContextProvider jmsResourcesContextProvider) {
        this.jmsResourcesContextProvider = jmsResourcesContextProvider;
    }

    @Override
    public void beforeAll(final ExtensionContext extensionContext) {
        final Store globalStore = getGlobalStore(extensionContext);
        registerTestSuiteShutdownHookForJmsResourceCleanup(globalStore);
        registerTestSuiteShutdownHookForExecutionTimer(globalStore);
    }

    /**
     * Drains all consumer queues created during test, so that message consumers can be reused across other tests within same Test class
     */
    @Override
    public void beforeEach(final ExtensionContext extensionContext) {
        jmsResourcesContextProvider.get().clearMessages();
    }

    /**
     * Closes all consumers/producers that are created across all tests within a single Test class.  So, consumers/producers can't be reused across different Test classes (Without this recycling, number of message consumers may outgrow and can cause resource exhaustion)
     */
    @Override
    public void afterAll(final ExtensionContext extensionContext) {
        jmsResourcesContextProvider.get().closeConsumersAndProducers();
    }

    private Store getGlobalStore(ExtensionContext extensionContext) {
        return extensionContext.getRoot().getStore(GLOBAL);
    }

    private void registerTestSuiteShutdownHookForJmsResourceCleanup(Store globalStore) {
        if (globalStore.get(TEST_SUITE_SHUTDOWN_JMS_RESOURCE_CLEANUP_HOOK) == null) {
            System.out.println("----------Registering test suite shutdown hook (jms resource cleanup)-------------");
            globalStore.put(TEST_SUITE_SHUTDOWN_JMS_RESOURCE_CLEANUP_HOOK, jmsResourcesContextProvider);
        }
    }

    private void registerTestSuiteShutdownHookForExecutionTimer(Store globalStore) {
        if (globalStore.get(TEST_SUITE_SHUTDOWN_EXECUTION_TIMER_HOOK) == null) {
            globalStore.put(TEST_SUITE_SHUTDOWN_EXECUTION_TIMER_HOOK, new TestSuiteExecutionTimeCalculator());
        }
    }
}
