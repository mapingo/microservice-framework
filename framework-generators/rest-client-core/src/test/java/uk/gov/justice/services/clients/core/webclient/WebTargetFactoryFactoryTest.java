package uk.gov.justice.services.clients.core.webclient;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
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