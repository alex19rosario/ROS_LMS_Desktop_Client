package com.ros.lmsdesktopclient.services.service_impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ros.lmsdesktopclient.dtos.AddLoanDTO;
import com.ros.lmsdesktopclient.services.service.LoanService;
import com.ros.lmsdesktopclient.util.enums.ApiUrls;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.exceptions.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoanServiceImpl implements LoanService {

    private final HttpClient client;
    private final TokenHandler tokenHandler;

    public LoanServiceImpl(HttpClient client, TokenHandler tokenHandler) {
        this.client = client;
        this.tokenHandler = tokenHandler;

    }

    @Override
    public void issueBook(AddLoanDTO addLoanDTO) throws NetworkException, ServerErrorException, ExpiredSessionException, BookNotFoundException, BookNotAvailableException, MemberNotFoundException, MemberHasActiveLoanException, MemberHasOverdueLoanException {
        String token = tokenHandler
                .getToken()
                .orElseThrow(() -> new ExpiredSessionException("No token found. Please log in again."));

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            String jsonPayload = objectMapper.writeValueAsString(addLoanDTO);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiUrls.LOANS.getUrl()))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 200 -> { /* OK */ }
                case 404 -> {
                    String body = response.body();
                    if (body.contains("Book not Found")) {
                        throw new BookNotFoundException("Book not found.");
                    } else if (body.contains("Member not Found")) {
                        throw new MemberNotFoundException("Member not found.");
                    }
                }
                case 400 -> {
                    String body = response.body();
                    if (body.contains("Book not Available")) {
                        throw new BookNotAvailableException("Book is not available.");
                    } else if (body.contains("Member Has Active Loan")) {
                        throw new MemberHasActiveLoanException("The member '" + addLoanDTO.memberUsername() + "' has an active loan.");
                    } else if (body.contains("Member Has Overdue Loan")) {
                        throw new MemberHasOverdueLoanException("The member '" + addLoanDTO.memberUsername() + "' has an overdue loan.");
                    }
                }
                case 401, 403 -> throw new ExpiredSessionException("Session expired. Please log in again.");
                default -> throw new ServerErrorException("Unexpected response from server: " + response.statusCode());
            }

        } catch (IOException e) {
            throw new NetworkException("Network error: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new NetworkException("Request was interrupted: " + e.getMessage());
        }
    }
}
