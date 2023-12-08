package uk.gov.justice.services.jmx.bootstrap;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

public class JmxSystemCommandBootstrapper implements Extension {

    private final JmxCommandBootstrapObjectFactory jmxCommandBootstrapObjectFactory;

    public JmxSystemCommandBootstrapper() {
        this(new JmxCommandBootstrapObjectFactory());
    }
    
    public JmxSystemCommandBootstrapper(final JmxCommandBootstrapObjectFactory jmxCommandBootstrapObjectFactory) {
        this.jmxCommandBootstrapObjectFactory = jmxCommandBootstrapObjectFactory;
    }

    public void afterDeploymentValidation(@Observes final AfterDeploymentValidation event, final BeanManager beanManager) {
        jmxCommandBootstrapObjectFactory.systemCommandScanner().registerSystemCommands(beanManager);
    }
}
