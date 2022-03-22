package uk.gov.justice.services.healthcheck.registration;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.common.configuration.ContextNameProvider;
import uk.gov.justice.services.healthcheck.api.DefaultIgnoredHealthcheckNamesProvider;
import uk.gov.justice.services.healthcheck.api.Healthcheck;
import uk.gov.justice.services.healthcheck.api.HealthcheckResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class HealthcheckRegistryTest {

    @Mock
    private DefaultIgnoredHealthcheckNamesProvider ignoredHealthcheckNamesProvider;

    @Mock
    private ContextNameProvider contextNameProvider;

    @Mock
    private Logger logger;

    @InjectMocks
    private HealthcheckRegistry healthcheckRegistry;

    @Test
    public void shouldAddHealthchecksToRegistry() throws Exception {

        final Healthcheck healthcheck_1 = new SomeHealthcheck();
        final Healthcheck healthcheck_2 = new AnotherHealthcheck();

        final String contextName = "people";

        when(ignoredHealthcheckNamesProvider.getNamesOfIgnoredHealthChecks()).thenReturn(emptyList());
        when(contextNameProvider.getContextName()).thenReturn(contextName);

        healthcheckRegistry.addHealthcheck(healthcheck_1);
        healthcheckRegistry.addHealthcheck(healthcheck_2);

        assertThat(healthcheckRegistry.getHealthChecks().size(), is(2));
        assertThat(healthcheckRegistry.getHealthChecks(), hasItems(healthcheck_1, healthcheck_2));

        verify(logger).info("Registering healthcheck class 'SomeHealthcheck' as 'some-healthcheck' for 'people' context");
        verify(logger).info("Registering healthcheck class 'AnotherHealthcheck' as 'another-healthcheck' for 'people' context");
    }

    @Test
    public void shouldNotRegisterHealthcheckIfMarkedAsIgnored() throws Exception {

        final Healthcheck healthcheck_1 = new SomeHealthcheck();
        final Healthcheck healthcheck_2 = new AnotherHealthcheck();

        final String contextName = "people";

        when(ignoredHealthcheckNamesProvider.getNamesOfIgnoredHealthChecks()).thenReturn(singletonList(healthcheck_1.getHealthcheckName()));
        when(contextNameProvider.getContextName()).thenReturn(contextName);

        healthcheckRegistry.addHealthcheck(healthcheck_1);
        healthcheckRegistry.addHealthcheck(healthcheck_2);

        assertThat(healthcheckRegistry.getHealthChecks().size(), is(1));
        assertThat(healthcheckRegistry.getHealthChecks(), hasItem(healthcheck_2));

        verify(logger).info("Ignoring healthcheck 'some-healthcheck' as it's marked as ignored for 'people' context");
        verify(logger).info("Registering healthcheck class 'AnotherHealthcheck' as 'another-healthcheck' for 'people' context");
    }

    @SuppressWarnings("NewClassNamingConvention")
    private static class SomeHealthcheck implements Healthcheck {

        @Override
        public String getHealthcheckName() {
            return "some-healthcheck";
        }

        @Override
        public String healthcheckDescription() {
            return null;
        }

        @Override
        public HealthcheckResult runHealthcheck() {
            return null;
        }
    }

    @SuppressWarnings("NewClassNamingConvention")
    private static class AnotherHealthcheck implements Healthcheck {

        @Override
        public String getHealthcheckName() {
            return "another-healthcheck";
        }

        @Override
        public String healthcheckDescription() {
            return null;
        }

        @Override
        public HealthcheckResult runHealthcheck() {
            return null;
        }
    }

}