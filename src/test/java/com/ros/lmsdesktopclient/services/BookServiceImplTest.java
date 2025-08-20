package com.ros.lmsdesktopclient.services;

import com.ros.lmsdesktopclient.dtos.AddBookDTO;
import com.ros.lmsdesktopclient.dtos.SearchBookDTO;
import com.ros.lmsdesktopclient.services.service_impl.BookServiceImpl;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.enums.GenreType;
import com.ros.lmsdesktopclient.util.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
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
class BookServiceImplTest {
    @Mock
    HttpClient httpClient;

    @Mock
    TokenHandler tokenHandler;

    @Mock
    HttpResponse<String> httpResponse;

    BookServiceImpl bookService;

    File tempCover;

    @BeforeEach
    void setup() throws IOException {
        MockitoAnnotations.openMocks(this);

        // Create a temporary file to act as coverImage
        tempCover = File.createTempFile("cover", ".jpg");
        tempCover.deleteOnExit();

        bookService = new BookServiceImpl(httpClient, tokenHandler);
    }

    // ---------------- addBook tests ----------------
    @Test
    void addBook_shouldThrowInvalidISBNException() {
        AddBookDTO dto = new AddBookDTO("1234", "Title", "Author", "Genre", "staff", tempCover);

        assertThrows(InvalidISBNException.class, () -> bookService.addBook(dto));
    }

    @Test
    void addBook_shouldThrowExpiredSessionException_whenNoToken() throws Exception {
        AddBookDTO dto = new AddBookDTO("1234567890", "Title", "Author", "Genre", "staff", tempCover);
        when(tokenHandler.getToken()).thenReturn(Optional.empty());

        assertThrows(ExpiredSessionException.class, () -> bookService.addBook(dto));
    }

    @Test
    void addBook_shouldThrowBookAlreadyExistException_on409() throws Exception {
        AddBookDTO dto = new AddBookDTO("1234567890", "Title", "Author", "Genre", "staff", tempCover);
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(409);

        assertThrows(BookAlreadyExistException.class, () -> bookService.addBook(dto));
    }

    @Test
    void addBook_shouldThrowExpiredSessionException_on401() throws Exception {
        AddBookDTO dto = new AddBookDTO("1234567890", "Title", "Author", "Genre", "staff", tempCover);
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(401);

        assertThrows(ExpiredSessionException.class, () -> bookService.addBook(dto));
    }

    @Test
    void addBook_shouldThrowServerErrorException_onUnexpectedStatus() throws Exception {
        AddBookDTO dto = new AddBookDTO("1234567890", "Title", "Author", "Genre", "staff", tempCover);
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(500);

        assertThrows(ServerErrorException.class, () -> bookService.addBook(dto));
    }

    @Test
    void addBook_shouldThrowNetworkException_onIOException() throws Exception {
        AddBookDTO dto = new AddBookDTO("1234567890", "Title", "Author", "Genre", "staff", tempCover);
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("connection reset"));

        NetworkException ex = assertThrows(NetworkException.class, () -> bookService.addBook(dto));
        assertTrue(ex.getMessage().contains("Failed to serialize book data"));
    }

    @Test
    void addBook_shouldThrowNetworkException_onInterruptedException() throws Exception {
        AddBookDTO dto = new AddBookDTO("1234567890", "Title", "Author", "Genre", "staff", tempCover);
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new InterruptedException("timeout"));

        NetworkException ex = assertThrows(NetworkException.class, () -> bookService.addBook(dto));
        assertTrue(ex.getMessage().contains("Request was interrupted"));
    }

    @Test
    void searchBooks_shouldThrowExpiredSessionException_whenNoToken() {
        SearchBookDTO filter = new SearchBookDTO(1, 10, "title", null, null, null, null);
        when(tokenHandler.getToken()).thenReturn(Optional.empty());

        assertThrows(ExpiredSessionException.class, () -> bookService.searchBooks(filter));
    }

    @Test
    void searchBooks_shouldThrowBookNotFoundException_whenEmbeddedMissing() throws Exception {
        SearchBookDTO filter = new SearchBookDTO(1, 10, "title", null, null, null, null);
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn("{\"page\":{}}"); // missing _embedded

        assertThrows(BookNotFoundException.class, () -> bookService.searchBooks(filter));
    }

    @Test
    void searchBooks_shouldReturnPaginatedBooks() throws Exception {
        SearchBookDTO filter = new SearchBookDTO(1, 10, "title", null, null, null, null);
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);

        String json = "{ \"_embedded\": { \"bookDTOList\": [] }, \"page\": {\"totalPages\": 1, \"size\": 10}}";
        when(httpResponse.body()).thenReturn(json);

        assertThrows(BookNotFoundException.class, () -> bookService.searchBooks(filter));
    }

    @Test
    void searchBooks_shouldThrowServerErrorException_onUnexpectedStatus() throws Exception {
        SearchBookDTO filter = new SearchBookDTO(1, 10, "title", null, null, null, null);
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(500);

        assertThrows(ServerErrorException.class, () -> bookService.searchBooks(filter));
    }

    @Test
    void searchBooks_shouldThrowNetworkException_onIOException() throws Exception {
        SearchBookDTO filter = new SearchBookDTO(1, 10, "title", null, null, null, null);
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("fail"));

        NetworkException ex = assertThrows(NetworkException.class, () -> bookService.searchBooks(filter));
        assertTrue(ex.getMessage().contains("Failed to communicate"));
    }

    @Test
    void searchBooks_shouldIncludeAllQueryParams_whenFilterFieldsArePresent() throws Exception {
        SearchBookDTO filter = new SearchBookDTO(
                1,
                10,
                "MyTitle",
                GenreType.FICTION,
                "John",
                "Doe",
                true
        );

        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);

        // Provide a valid JSON with one book so it doesn't throw BookNotFoundException
        String json = """
            {
              "_embedded": {
                "bookDTOList": [
                  {
                    "id":1,
                    "isbn":"1234567890",
                    "title":"MyTitle",
                    "authors":[],
                    "genres":[],
                    "status":true,
                    "imagePath":"path"
                  }
                ]
              },
              "page": {"totalPages":1,"size":10}
            }
            """;
        when(httpResponse.body()).thenReturn(json);

        bookService.searchBooks(filter);

        // Capture the URL from HttpRequest
        ArgumentCaptor<HttpRequest> captor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(httpClient).send(captor.capture(), any(HttpResponse.BodyHandler.class));
        String url = captor.getValue().uri().toString();

        // Verify that all query params are included
        assertTrue(url.contains("title=MyTitle"));
        assertTrue(url.contains("genre=" + GenreType.FICTION.getStr()));
        assertTrue(url.contains("authorFirstName=John"));
        assertTrue(url.contains("authorLastName=Doe"));
        assertTrue(url.contains("isAvailable=true"));
    }

    @Test
    void searchBooks_shouldSkipOptionalQueryParams_whenFieldsAreNullOrBlank() throws Exception {
        SearchBookDTO filter = new SearchBookDTO(
                1,
                10,
                null,       // title null → skip
                null,       // genre null → skip
                "",         // authorFirstName blank → skip
                "   ",      // authorLastName blank → skip
                null        // isAvailable null → skip
        );

        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);

        String json = """
            {
              "_embedded": {
                "bookDTOList": [
                  {
                    "id":1,
                    "isbn":"1234567890",
                    "title":"Title",
                    "authors":[],
                    "genres":[],
                    "status":true,
                    "imagePath":"path"
                  }
                ]
              },
              "page": {"totalPages":1,"size":10}
            }
            """;
        when(httpResponse.body()).thenReturn(json);

        bookService.searchBooks(filter);

        ArgumentCaptor<HttpRequest> captor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(httpClient).send(captor.capture(), any(HttpResponse.BodyHandler.class));
        String url = captor.getValue().uri().toString();

        // None of the optional query params should be present
        assertFalse(url.contains("title="));
        assertFalse(url.contains("genre="));
        assertFalse(url.contains("authorFirstName="));
        assertFalse(url.contains("authorLastName="));
        assertFalse(url.contains("isAvailable="));
    }


    // ---------------- searchBookByIsbn tests ----------------
    @Test
    void searchBookByIsbn_shouldThrowBookNotFoundException_on404() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(404);

        assertThrows(BookNotFoundException.class, () -> bookService.searchBookByIsbn("1234567890"));
    }

    @Test
    void searchBookByIsbn_shouldReturnBookDTO_on200() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn("{\"id\":1,\"isbn\":\"1234567890\",\"title\":\"title\",\"authors\":[],\"genres\":[],\"status\":true,\"imagePath\":\"path\"}");

        assertNotNull(bookService.searchBookByIsbn("1234567890"));
    }

    @Test
    void searchBookByIsbn_shouldThrowExpiredSessionException_on401() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(401);

        assertThrows(ExpiredSessionException.class, () -> bookService.searchBookByIsbn("1234567890"));
    }

    @Test
    void searchBookByIsbn_shouldThrowServerErrorException_onUnexpectedStatus() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(500);

        assertThrows(ServerErrorException.class, () -> bookService.searchBookByIsbn("1234567890"));
    }


    @Test
    void searchBookByIsbn_shouldThrowNetworkException_onIOException() throws Exception {
        when(tokenHandler.getToken()).thenReturn(Optional.of("token"));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("fail"));

        NetworkException ex = assertThrows(NetworkException.class, () -> bookService.searchBookByIsbn("1234567890"));
        assertTrue(ex.getMessage().contains("Failed to communicate"));
    }

    @AfterEach
    void cleanup() {
        tempCover.delete();
    }
}
