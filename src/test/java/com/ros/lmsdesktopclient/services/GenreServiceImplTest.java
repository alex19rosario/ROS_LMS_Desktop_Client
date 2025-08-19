package com.ros.lmsdesktopclient.services;

import com.ros.lmsdesktopclient.services.service_impl.GenreServiceImpl;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.exceptions.AccessDeniedException;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GenreServiceImplTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private TokenHandler tokenHandler;

    @Mock
    private HttpResponse<String> httpResponse;

    @InjectMocks
    private GenreServiceImpl genreService;

    @BeforeEach
    void setup() {
        // No extra setup needed, @InjectMocks will inject mocks
    }

    @Test
    void getAllGenres_successful() throws Exception {
        // given
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn("[\"Fiction\",\"Drama\"]");

        // when
        Set<String> genres = genreService.getAllGenres();

        // then
        assertEquals(Set.of("Fiction", "Drama"), genres);
        verify(tokenHandler).getToken();
        verify(httpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    void getAllGenres_missingToken_throwsRuntimeException() {
        when(tokenHandler.getToken()).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> genreService.getAllGenres());
    }

    @Test
    void getAllGenres_forbiddenResponse_throwsAccessDeniedException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(403);

        assertThrows(AccessDeniedException.class, () -> genreService.getAllGenres());
    }

    @Test
    void getAllGenres_httpClientThrowsIOException_wrappedInRuntimeException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Network error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> genreService.getAllGenres());
        assertInstanceOf(IOException.class, ex.getCause());
    }

    @Test
    void getAllGenres_httpClientThrowsInterruptedException_wrappedInRuntimeException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new InterruptedException("Interrupted"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> genreService.getAllGenres());
        assertInstanceOf(InterruptedException.class, ex.getCause());
    }
}
