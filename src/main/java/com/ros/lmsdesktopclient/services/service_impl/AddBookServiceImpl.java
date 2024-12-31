package com.ros.lmsdesktopclient.services.service_impl;

import com.ros.lmsdesktopclient.dtos.BookDTO;
import com.ros.lmsdesktopclient.services.service.AddBookService;
import com.ros.lmsdesktopclient.util.ApiUrls;
import com.ros.lmsdesktopclient.util.exceptions.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AddBookServiceImpl implements AddBookService {

    @Override
    public void addBook(BookDTO book, HttpClient client) throws InvalidISBNException, NetworkException, ServerErrorException, ExpiredSessionException, BookAlreadyExistException {
        checkISBN(book);
        checkNetwork();
        checkServer();
        System.out.println("Adding book:\n" + book);
    }

    private void checkISBN(BookDTO book) throws InvalidISBNException {
        // Regex for a valid ISBN-10 or ISBN-13
        String isbnRegex = "^(\\d{10}|\\d{13})$";

        // Check if the book's ISBN matches the regex
        if (book == null || book.ISBN() == null || !book.ISBN().matches(isbnRegex)) {
            throw new InvalidISBNException("Invalid ISBN: The specified ISBN does not have the correct format.");
        }
    }

    private void checkNetwork() throws NetworkException{
        try {
            InetAddress address = InetAddress.getByName("8.8.8.8"); // Google's public DNS
            if (!address.isReachable(2000)) { // 2 seconds timeout
                throw new NetworkException("Network is not reachable");
            }
        } catch (IOException e) {
            throw new NetworkException("Failed to check network connectivity " + e);
        }
    }

    private void checkServer() throws ServerErrorException{
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(java.time.Duration.ofSeconds(2))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(ApiUrls.LOGIN.getUrl()))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            int statusCode = response.statusCode();

            if (statusCode != HttpURLConnection.HTTP_OK && statusCode != HttpURLConnection.HTTP_UNAUTHORIZED) {
                throw new ServerErrorException("Server is not reachable. Response code: " + statusCode);
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new ServerErrorException("Failed to check server availability " + e);
        }
    }
}
