package com.ros.lmsdesktopclient.services;

import com.ros.lmsdesktopclient.services.service_impl.StorageServiceImpl;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.exceptions.ExpiredSessionException;
import com.ros.lmsdesktopclient.util.exceptions.ImageNotFoundException;
import com.ros.lmsdesktopclient.util.exceptions.NetworkException;
import com.ros.lmsdesktopclient.util.exceptions.ServerErrorException;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StorageServiceImplTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private TokenHandler tokenHandler;

    @Mock
    private HttpResponse<byte[]> httpResponse;

    @InjectMocks
    private StorageServiceImpl storageService;

    @BeforeEach
    void setup() {
        // handled by @InjectMocks
    }

    @Test
    void getCoverImage_successful_returnsImage() throws Exception {
        // given
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(new byte[]{/* dummy image bytes */1, 2, 3});

        // when
        Image image = storageService.getCoverImage("cover.png");

        // then
        assertNotNull(image);
        verify(tokenHandler).getToken();
        verify(httpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    void getCoverImage_missingToken_throwsExpiredSessionException() {
        when(tokenHandler.getToken()).thenReturn(Optional.empty());

        assertThrows(ExpiredSessionException.class,
                () -> storageService.getCoverImage("cover.png"));
    }

    @Test
    void getCoverImage_notFound_throwsImageNotFoundException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(404);

        assertThrows(ImageNotFoundException.class,
                () -> storageService.getCoverImage("missing.png"));
    }

    @Test
    void getCoverImage_forbiddenAlsoThrowsImageNotFoundException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(403);

        assertThrows(ImageNotFoundException.class,
                () -> storageService.getCoverImage("forbidden.png"));
    }

    @Test
    void getCoverImage_unauthorized_throwsExpiredSessionException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(401);

        assertThrows(ExpiredSessionException.class,
                () -> storageService.getCoverImage("unauthorized.png"));
    }

    @Test
    void getCoverImage_unexpectedStatus_throwsServerErrorException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(500);

        assertThrows(ServerErrorException.class,
                () -> storageService.getCoverImage("server-error.png"));
    }

    @Test
    void getCoverImage_httpClientThrowsInterruptedException_throwsNetworkException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new InterruptedException("interrupted"));

        assertThrows(NetworkException.class,
                () -> storageService.getCoverImage("cover.png"));
    }

    @Test
    void getCoverImage_httpClientThrowsIOException_rethrowsIOException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("I/O error"));

        assertThrows(IOException.class,
                () -> storageService.getCoverImage("cover.png"));
    }

}
