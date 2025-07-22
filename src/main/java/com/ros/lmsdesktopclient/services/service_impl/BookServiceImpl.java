package com.ros.lmsdesktopclient.services.service_impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ros.lmsdesktopclient.dtos.AddBookDTO;
import com.ros.lmsdesktopclient.dtos.BookDTO;
import com.ros.lmsdesktopclient.dtos.SearchBookDTO;
import com.ros.lmsdesktopclient.services.service.BookService;
import com.ros.lmsdesktopclient.util.ApiUrls;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.exceptions.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class BookServiceImpl implements BookService {

    @Override
    public void addBook(AddBookDTO book) throws InvalidISBNException, NetworkException, ServerErrorException, ExpiredSessionException, BookAlreadyExistException {
        // 1. Validate ISBN before doing anything
        checkISBN(book);

        // 2. Retrieve the authentication token
        String token = TokenHandler.getInstance().getToken()
                .orElseThrow(() -> new ExpiredSessionException("No token found. Please log in again."));

        // 3. Create a unique boundary string for multipart form separation
        String boundary = "----JavaFormBoundary" + System.currentTimeMillis();

        try (HttpClient client = HttpClient.newHttpClient()) {
            // 4. Prepare the string part of the multipart body (text fields)
            StringBuilder sb = new StringBuilder();

            // Add text fields (isbn, title, authors, genres) with correct boundary and format
            appendFormField(sb, "isbn", String.valueOf(book.ISBN()), boundary);
            appendFormField(sb, "title", book.title(), boundary);
            appendFormField(sb, "authors", book.authors(), boundary);
            appendFormField(sb, "staffUsername", book.staffUsername(), boundary);
            appendFormField(sb, "genres", book.genres(), boundary);

            // 5. Read the file bytes from the cover image
            byte[] fileBytes = Files.readAllBytes(book.coverImage().toPath());

            // 6. Build the header for the file part (note: filename and content-type are required)
            String filePartHeader = "--" + boundary + "\r\n" +
                    "Content-Disposition: form-data; name=\"coverImage\"; filename=\"" + book.coverImage().getName() + "\"\r\n" +
                    "Content-Type: " + Files.probeContentType(book.coverImage().toPath()) + "\r\n\r\n";

            // 7. Convert string parts to bytes
            byte[] headerBytes = sb.toString().getBytes(StandardCharsets.UTF_8);

            // 8. Add the closing boundary at the end of the request body
            byte[] footerBytes = ("\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8);

            // 9. Combine all parts into a single byte array:
            byte[] requestBody = new byte[
                    headerBytes.length +
                    filePartHeader.getBytes(StandardCharsets.UTF_8).length +
                    fileBytes.length +
                    footerBytes.length
            ];

            // 10. Copy parts into the final requestBody array
            int offset = 0;
            System.arraycopy(headerBytes, 0, requestBody, offset, headerBytes.length); offset += headerBytes.length;
            byte[] fileHeaderBytes = filePartHeader.getBytes(StandardCharsets.UTF_8);
            System.arraycopy(fileHeaderBytes, 0, requestBody, offset, fileHeaderBytes.length); offset += fileHeaderBytes.length;
            System.arraycopy(fileBytes, 0, requestBody, offset, fileBytes.length); offset += fileBytes.length;
            System.arraycopy(footerBytes, 0, requestBody, offset, footerBytes.length);

            // 11. Build the HTTP POST request with headers and multipart body
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiUrls.BOOKS.getUrl()))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(requestBody))
                    .build();

            // 12. Send the HTTP request
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

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
    }

    @Override
    public List<BookDTO> searchBooks(SearchBookDTO filter) throws NetworkException, ServerErrorException, ExpiredSessionException, BookNotFoundException {

        String token = TokenHandler.getInstance().getToken()
                .orElseThrow(() -> new ExpiredSessionException("No token found. Please log in again."));

        try {
            // Build URL with query params
            StringBuilder urlBuilder = new StringBuilder(ApiUrls.BOOKS.getUrl());
            urlBuilder.append("?page=").append(filter.page());
            urlBuilder.append("&size=").append(filter.size());

            if (filter.title() != null && !filter.title().isBlank())
                urlBuilder.append("&title=").append(encode(filter.title()));
            if (filter.genre() != null)
                urlBuilder.append("&genre=").append(encode(filter.genre().getStr()));
            if (filter.authorFirstName() != null && !filter.authorFirstName().isBlank())
                urlBuilder.append("&authorFirstName=").append(encode(filter.authorFirstName()));
            if (filter.authorLastName() != null && !filter.authorLastName().isBlank())
                urlBuilder.append("&authorLastName=").append(encode(filter.authorLastName()));
            if (filter.isAvailable() != null)
                urlBuilder.append("&isAvailable=").append(filter.isAvailable());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlBuilder.toString()))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 200 -> {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(response.body());

                    JsonNode embeddedNode = root.get("_embedded");
                    if (embeddedNode == null || !embeddedNode.has("bookDTOList")) {
                        throw new BookNotFoundException("No books found matching the criteria.");
                    }

                    List<BookDTO> books = new ArrayList<>();
                    for (JsonNode bookNode : root.get("_embedded").get("bookDTOList")) {
                        BookDTO book = mapper.treeToValue(bookNode, BookDTO.class);
                        books.add(book);
                    }

                    if (books.isEmpty()) {
                        throw new BookNotFoundException("No books found matching the criteria.");
                    }

                    return books;
                }
                case 401, 403 -> throw new ExpiredSessionException("Session expired. Please log in again.");
                default -> throw new ServerErrorException("Server returned status: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            throw new NetworkException("Failed to communicate with the server." + e.getMessage());
        }
    }

    @Override
    public BookDTO searchBookByIsbn(String isbn) throws BookNotFoundException, NetworkException, ServerErrorException, ExpiredSessionException {
        String token = TokenHandler.getInstance().getToken()
                .orElseThrow(() -> new ExpiredSessionException("No token found. Please log in again."));

        try {
            String url = ApiUrls.BOOKS.getUrl() + "/" + encode(isbn);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 200 -> {
                    ObjectMapper mapper = new ObjectMapper();
                    return mapper.readValue(response.body(), BookDTO.class);
                }
                case 404 -> throw new BookNotFoundException("Book with ISBN '" + isbn + "' was not found.");
                case 401, 403 -> throw new ExpiredSessionException("Session expired. Please log in again.");
                default -> throw new ServerErrorException("Server returned status: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            throw new NetworkException("Failed to communicate with the server. " + e.getMessage());
        }
    }

    private void checkISBN(AddBookDTO book) throws InvalidISBNException {
        // Regex for a valid ISBN-10 or ISBN-13
        String isbnRegex = "^(\\d{10}|\\d{13})$";

        // Check if the book's ISBN matches the regex
        if (book == null || !String.valueOf(book.ISBN()).matches(isbnRegex)) {
            throw new InvalidISBNException("Invalid ISBN: The specified ISBN does not have the correct format.");
        }
    }

    /**
     * Appends a form-data text field to the multipart body.
     *
     * @param sb        StringBuilder accumulating the multipart content
     * @param name      the name of the form field
     * @param value     the value of the form field
     * @param boundary  the boundary string used to separate parts
     */
    private void appendFormField(StringBuilder sb, String name, String value, String boundary) {
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"").append(name).append("\"\r\n\r\n");
        sb.append(value).append("\r\n");
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

}
