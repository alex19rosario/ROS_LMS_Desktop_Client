package com.ros.lmsdesktopclient.services;

import com.ros.lmsdesktopclient.dtos.AddMemberDTO;
import com.ros.lmsdesktopclient.services.service_impl.MemberServiceImpl;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private TokenHandler tokenHandler;

    @Mock
    private HttpResponse<String> httpResponse;

    @InjectMocks
    private MemberServiceImpl memberService;

    private AddMemberDTO dto;

    @BeforeEach
    void setup() {
        dto = new AddMemberDTO(
                "123456789",
                "John",
                "Doe",
                "555-1234",
                LocalDate.of(2000, 1, 1),
                "MALE",
                "john@example.com",
                "john_doe",
                "securePassword123",
                "staff_admin"
        );
    }

    @Test
    void addMember_successful() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);

        assertDoesNotThrow(() -> memberService.addMember(dto));

        verify(httpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    void addMember_missingToken_throwsExpiredSessionException() {
        when(tokenHandler.getToken()).thenReturn(Optional.empty());

        assertThrows(ExpiredSessionException.class, () -> memberService.addMember(dto));
    }

    @Test
    void addMember_conflict_username_throwsUsernameAlreadyExistException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(409);
        when(httpResponse.body()).thenReturn("Username already exists");

        assertThrows(UsernameAlreadyExistException.class, () -> memberService.addMember(dto));
    }

    @Test
    void addMember_conflict_email_throwsEmailAlreadyExistException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(409);
        when(httpResponse.body()).thenReturn("Email already exists");

        assertThrows(EmailAlreadyExistException.class, () -> memberService.addMember(dto));
    }

    @Test
    void addMember_conflict_member_throwsMemberAlreadyExistException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(409);
        when(httpResponse.body()).thenReturn("Member already exists");

        assertThrows(MemberAlreadyExistException.class, () -> memberService.addMember(dto));
    }

    @Test
    void addMember_unauthorized_throwsExpiredSessionException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(401);

        assertThrows(ExpiredSessionException.class, () -> memberService.addMember(dto));
    }

    @Test
    void addMember_forbidden_throwsExpiredSessionException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(403);

        assertThrows(ExpiredSessionException.class, () -> memberService.addMember(dto));
    }

    @Test
    void addMember_unexpectedStatus_throwsServerErrorException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(500);

        assertThrows(ServerErrorException.class, () -> memberService.addMember(dto));
    }

    @Test
    void addMember_httpClientThrowsIOException_throwsNetworkException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("IO error"));

        assertThrows(NetworkException.class, () -> memberService.addMember(dto));
    }

    @Test
    void addMember_httpClientThrowsInterruptedException_throwsNetworkException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new InterruptedException("interrupted"));

        assertThrows(NetworkException.class, () -> memberService.addMember(dto));
    }

    @Test
    void addMember_buildsCorrectRequest() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("jwt-token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);

        ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);

        memberService.addMember(dto);

        verify(httpClient).send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));

        HttpRequest request = requestCaptor.getValue();
        assertEquals("Bearer jwt-token", request.headers().firstValue("Authorization").orElseThrow());
        assertEquals("application/json", request.headers().firstValue("Content-Type").orElseThrow());
        assertTrue(request.bodyPublisher().isPresent());
    }

}
