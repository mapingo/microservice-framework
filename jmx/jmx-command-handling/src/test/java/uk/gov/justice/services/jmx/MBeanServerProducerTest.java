package uk.gov.justice.services.jmx;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.management.MBeanServer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MBeanServerProducerTest {

    @InjectMocks
    private MBeanServerProducer mBeanServerProducer;

    @Test
    public void shouldCreateAnMBeanServer() throws Exception {

        final MBeanServer mBeanServer = mBeanServerProducer.mBeanServer();

        assertThat(mBeanServer, is(notNullValue()));
    }
}
