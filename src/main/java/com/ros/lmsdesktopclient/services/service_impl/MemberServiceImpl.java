package com.ros.lmsdesktopclient.services.service_impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ros.lmsdesktopclient.dtos.AddMemberDTO;
import com.ros.lmsdesktopclient.services.service.MemberService;
import com.ros.lmsdesktopclient.util.ApiUrls;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.exceptions.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MemberServiceImpl implements MemberService {
    @Override
    public void addMember(AddMemberDTO member) throws NetworkException, ServerErrorException, ExpiredSessionException, MemberAlreadyExistException, UsernameAlreadyExistException, EmailAlreadyExistException {

        String token = TokenHandler.getInstance().getToken()
                .orElseThrow(() -> new ExpiredSessionException("No token found. Please log in again."));

        try (HttpClient client = HttpClient.newHttpClient()){
            // Serialize AddBookDTO to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(member);

            // Create HTTP POST Request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiUrls.MEMBERS.getUrl()))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Send Request and Handle Response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check Response Status
            switch (response.statusCode()) {
                case 200 -> {}
                case 409 -> {
                    String responseBody = response.body();
                    if (responseBody.contains("Username")) {
                        throw new UsernameAlreadyExistException("Username already exists.");
                    } else if (responseBody.contains("Email")) {
                        throw new EmailAlreadyExistException("Email already exists.");
                    } else if (responseBody.contains("Member")) {
                        throw new MemberAlreadyExistException("Member already exists.");
                    }
                }
                case 401, 403 -> throw new ExpiredSessionException("Session expired. Please log in again.");
                default -> throw new ServerErrorException("Unexpected response from server: " + response.statusCode());
            }

        } catch (IOException e) {
            throw new NetworkException("Failed to serialize book data or connect to the server." + e);
        } catch (InterruptedException e) {
            throw new NetworkException("Request was interrupted." + e);
        }
        System.out.println("Adding member: " + member);
    }
}
