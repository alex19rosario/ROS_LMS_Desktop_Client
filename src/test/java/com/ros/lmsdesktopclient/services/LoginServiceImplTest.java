package com.ros.lmsdesktopclient.services;

import com.ros.lmsdesktopclient.di.factories.AuthenticatedHttpClientFactory;
import com.ros.lmsdesktopclient.dtos.LoginDTO;
import com.ros.lmsdesktopclient.services.service_impl.LoginServiceImpl;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.exceptions.AccessDeniedException;
import com.ros.lmsdesktopclient.util.exceptions.AuthenticationException;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @Mock
    private AuthenticatedHttpClientFactory clientFactory;

    @Mock
    private TokenHandler tokenHandler;

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    @InjectMocks
    private LoginServiceImpl loginService;

    private LoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        loginDTO = new LoginDTO("username", "password");
    }

    @Test
    void login_successful_withRoleStaff() throws Exception {
        when(clientFactory.create(loginDTO.username(), loginDTO.password()))
                .thenReturn(httpClient);

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        when(httpResponse.body()).thenReturn("token");
        when(tokenHandler.getAuthorities()).thenReturn(Set.of("ROLE_STAFF"));

        loginService.login(loginDTO);

        verify(tokenHandler).saveToken("token");
    }

    @Test
    void login_shouldThrowAccessDeniedException_whenNotStaff() throws Exception {
        when(clientFactory.create(any(), any())).thenReturn(httpClient);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn("token");
        when(tokenHandler.getAuthorities()).thenReturn(Set.of("ROLE_MEMBER"));

        assertThrows(AccessDeniedException.class, () -> loginService.login(loginDTO));
    }

    @Test
    void login_shouldThrowAuthenticationException_onIOException() throws Exception {
        when(clientFactory.create(any(), any())).thenReturn(httpClient);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException());

        assertThrows(AuthenticationException.class, () -> loginService.login(loginDTO));
    }
}
