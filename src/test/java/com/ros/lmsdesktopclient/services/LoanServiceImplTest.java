package com.ros.lmsdesktopclient.services;

import com.ros.lmsdesktopclient.dtos.AddLoanDTO;
import com.ros.lmsdesktopclient.services.service_impl.LoanServiceImpl;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.exceptions.*;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private TokenHandler tokenHandler;

    @Mock
    private HttpResponse<String> httpResponse;

    @InjectMocks
    private LoanServiceImpl loanService;

    private AddLoanDTO loanDTO;

    @BeforeEach
    void setUp() {
        loanDTO = new AddLoanDTO(
                "8888888887",
                "john_doe",
                "staff_1"
        );
    }

    @Test
    void issueBook_successful() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);

        loanService.issueBook(loanDTO);

        verify(httpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verify(tokenHandler).getToken();
    }

    @Test
    void issueBook_bookNotFound() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(404);
        when(httpResponse.body()).thenReturn("Book not Found");

        assertThrows(BookNotFoundException.class, () -> loanService.issueBook(loanDTO));
    }

    @Test
    void issueBook_memberNotFound() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(404);
        when(httpResponse.body()).thenReturn("Member not Found");

        assertThrows(MemberNotFoundException.class, () -> loanService.issueBook(loanDTO));
    }

    @Test
    void issueBook_bookNotAvailable() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(400);
        when(httpResponse.body()).thenReturn("Book not Available");

        assertThrows(BookNotAvailableException.class, () -> loanService.issueBook(loanDTO));
    }

    @Test
    void issueBook_memberHasActiveLoan() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(400);
        when(httpResponse.body()).thenReturn("Member Has Active Loan");

        assertThrows(MemberHasActiveLoanException.class, () -> loanService.issueBook(loanDTO));
    }

    @Test
    void issueBook_memberHasOverdueLoan() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(400);
        when(httpResponse.body()).thenReturn("Member Has Overdue Loan");

        assertThrows(MemberHasOverdueLoanException.class, () -> loanService.issueBook(loanDTO));
    }

    @Test
    void issueBook_sessionExpired() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(401);

        assertThrows(ExpiredSessionException.class, () -> loanService.issueBook(loanDTO));
    }

    @Test
    void addLoan_shouldThrowServerErrorException_whenUnexpectedStatusCode() throws Exception {
        AddLoanDTO dto = new AddLoanDTO("8888888887", "member", "staff");

        // ✅ Mock tokenHandler to return a token so it doesn’t fail early
        when(tokenHandler.getToken()).thenReturn(Optional.of("mock-token"));

        // ✅ Mock HTTP response
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(418); // unhandled status code

        ServerErrorException exception = assertThrows(
                ServerErrorException.class,
                () -> loanService.issueBook(dto)
        );

        assertTrue(exception.getMessage().contains("Unexpected response from server: 418"));
    }

    @Test
    void addLoan_shouldThrowNetworkException_whenIOExceptionOccurs() throws Exception {
        AddLoanDTO dto = new AddLoanDTO("8888888887", "member", "staff");

        // Mock token to bypass ExpiredSessionException
        when(tokenHandler.getToken()).thenReturn(Optional.of("mock-token"));

        // Throw IOException when sending HTTP request
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("connection reset"));

        NetworkException exception = assertThrows(
                NetworkException.class,
                () -> loanService.issueBook(dto)
        );

        assertTrue(exception.getMessage().contains("Network error: connection reset"));
    }

    @Test
    void addLoan_shouldThrowNetworkException_whenInterruptedExceptionOccurs() throws Exception {
        AddLoanDTO dto = new AddLoanDTO("8888888887", "member", "staff");

        // Mock token to bypass ExpiredSessionException
        when(tokenHandler.getToken()).thenReturn(Optional.of("mock-token"));

        // Throw InterruptedException when sending HTTP request
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new InterruptedException("timeout"));

        NetworkException exception = assertThrows(
                NetworkException.class,
                () -> loanService.issueBook(dto)
        );

        assertTrue(exception.getMessage().contains("Request was interrupted: timeout"));
    }

    @Test
    void testFieldsAreSetCorrectly() {
        assertEquals("8888888887", loanDTO.bookIsbn());
        assertEquals("john_doe", loanDTO.memberUsername());
        assertEquals("staff_1", loanDTO.staffUsername());
    }
}
