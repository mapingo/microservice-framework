package uk.gov.justice.services.clients.core.webclient;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WebTargetFactoryFactoryTest {

    @Mock
    private BaseUriFactory baseUriFactory;

    @InjectMocks
    private WebTargetFactoryFactory webTargetFactoryFactory;

    @Test
    public void shouldCreateWebTargetFactory() {
        var webTargetFactory = webTargetFactoryFactory.create();

        assertThat(webTargetFactory.baseUriFactory, CoreMatchers.is(baseUriFactory));
    }
}