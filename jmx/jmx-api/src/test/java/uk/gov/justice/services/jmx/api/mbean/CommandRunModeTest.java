package uk.gov.justice.services.jmx.api.mbean;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.jmx.api.mbean.CommandRunMode.FORCED;
import static uk.gov.justice.services.jmx.api.mbean.CommandRunMode.GUARDED;

import org.junit.jupiter.api.Test;

public class CommandRunModeTest {

    @Test
    public void shouldGetTheCorrectCommandRunModeFromItsBoolean() throws Exception {

      assertThat(CommandRunMode.fromBoolean(true), is(GUARDED));
      assertThat(CommandRunMode.fromBoolean(false), is(FORCED));
    }
}