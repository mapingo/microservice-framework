package uk.gov.justice.services.generators.commons.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static uk.gov.justice.services.generators.commons.client.ActionMimeTypeDefinition.definitionWithRequest;
import static uk.gov.justice.services.generators.commons.client.ActionMimeTypeDefinition.definitionWithRequestAndResponse;
import static uk.gov.justice.services.generators.commons.client.ActionMimeTypeDefinition.definitionWithResponse;

import org.junit.jupiter.api.Test;
import org.raml.model.MimeType;

public class ActionMimeTypeDefinitionTest {

    @Test
    public void shouldCreateWithRequestTypeAndReturnRequestTypeAsNameType() throws Exception {
        final MimeType mimeType = mock(MimeType.class);
        assertThat(definitionWithRequest(mimeType).getNameType(), sameInstance(mimeType));
    }

    @Test
    public void shouldCreateWithRequestTypeAndReturnRequestTypeAsResponeType() throws Exception {
        final MimeType mimeType = mock(MimeType.class);
        assertThat(definitionWithRequest(mimeType).getResponseType(), sameInstance(mimeType));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldThrowExceptionForDefinitionWithRequestIfNullRequest() throws Exception {

        final IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () ->
                definitionWithRequest(null).getResponseType()
        );

        assertThat(illegalStateException.getMessage(), is("A RAML action must have either a request or response mimetype."));
    }

    @Test
    public void shouldCreateWithResponseTypeAndReturnResponseTypeAsNameType() throws Exception {
        final MimeType mimeType = mock(MimeType.class);
        assertThat(definitionWithResponse(mimeType).getNameType(), sameInstance(mimeType));
    }

    @Test
    public void shouldCreateWithResponseTypeAndReturnResponseTypeAsResponeType() throws Exception {
        final MimeType mimeType = mock(MimeType.class);
        assertThat(definitionWithResponse(mimeType).getResponseType(), sameInstance(mimeType));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldThrowExceptionForDefinitionWithResponseIfNullRequest() throws Exception {

        final IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () ->
                definitionWithResponse(null).getResponseType()

        );

        assertThat(illegalStateException.getMessage(), is("A RAML action must have either a request or response mimetype."));
    }

    @Test
    public void shouldCreateWithRequestTypeAndResponseTypeAndReturnRequestTypeAsNameType() throws Exception {
        final MimeType requestType = mock(MimeType.class);
        final MimeType responseType = mock(MimeType.class);

        assertThat(definitionWithRequestAndResponse(requestType, responseType).getNameType(), sameInstance(requestType));
    }

    @Test
    public void shouldCreateWithRequestTypeAndResponseTypeAndAndReturnResponseTypeAsResponseType() throws Exception {
        final MimeType requestType = mock(MimeType.class);
        final MimeType responseType = mock(MimeType.class);

        assertThat(definitionWithRequestAndResponse(requestType, responseType).getResponseType(), sameInstance(responseType));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldThrowExceptionForNullRequest() throws Exception {

        final IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () ->
                definitionWithRequestAndResponse(null, mock(MimeType.class)).getResponseType()
        );

        assertThat(illegalStateException.getMessage(), is("A RAML action must have either a request or response mimetype."));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldThrowExceptionForNullResponse() throws Exception {

        final IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () ->
                definitionWithRequestAndResponse(mock(MimeType.class), null).getResponseType()
        );

        assertThat(illegalStateException.getMessage(), is("A RAML action must have either a request or response mimetype."));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldThrowExceptionForNullRequestAndNullResponse() throws Exception {

        final IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () ->
                definitionWithRequestAndResponse(null, null).getResponseType()
        );

        assertThat(illegalStateException.getMessage(), is("A RAML action must have either a request or response mimetype."));
    }
}