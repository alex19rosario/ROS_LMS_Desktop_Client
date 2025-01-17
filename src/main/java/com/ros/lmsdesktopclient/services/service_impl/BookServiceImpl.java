package com.ros.lmsdesktopclient.services.service_impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ros.lmsdesktopclient.dtos.AddBookDTO;
import com.ros.lmsdesktopclient.services.service.BookService;
import com.ros.lmsdesktopclient.util.ApiUrls;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.exceptions.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BookServiceImpl implements BookService {

    @Override
    public void addBook(AddBookDTO book) throws InvalidISBNException, NetworkException, ServerErrorException, ExpiredSessionException, BookAlreadyExistException {

        checkISBN(book);
        String token = TokenHandler.getInstance().getToken()
                .orElseThrow(() -> new ExpiredSessionException("No token found. Please log in again."));

        try (HttpClient client = HttpClient.newHttpClient()) {
            // Serialize AddBookDTO to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(book);

            // Create HTTP POST Request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiUrls.BOOKS.getUrl()))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Send Request and Handle Response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check Response Status
            switch (response.statusCode()) {
                case 200 -> {}
                case 409 -> throw new BookAlreadyExistException("Book already exists.");
                case 401, 403 -> throw new ExpiredSessionException("Session expired. Please log in again.");
                default -> throw new ServerErrorException("Unexpected response from server: " + response.statusCode());
            }

        } catch (IOException e) {
            throw new NetworkException("Failed to serialize book data or connect to the server." + e);
        } catch (InterruptedException e) {
            throw new NetworkException("Request was interrupted." + e);
        }
        //Consume the service here
    }

    private void checkISBN(AddBookDTO book) throws InvalidISBNException {
        // Regex for a valid ISBN-10 or ISBN-13
        String isbnRegex = "^(\\d{10}|\\d{13})$";

        // Check if the book's ISBN matches the regex
        if (book == null || !String.valueOf(book.ISBN()).matches(isbnRegex)) {
            throw new InvalidISBNException("Invalid ISBN: The specified ISBN does not have the correct format.");
        }
    }

}
