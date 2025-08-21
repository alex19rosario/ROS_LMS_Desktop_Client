package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.dtos.AuthorDTO;
import com.ros.lmsdesktopclient.dtos.BookDTO;
import com.ros.lmsdesktopclient.dtos.PaginatedBooksDTO;
import com.ros.lmsdesktopclient.dtos.SearchBookDTO;
import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.models.SearchBookModel;
import com.ros.lmsdesktopclient.services.service.BookService;
import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.enums.GenreType;
import com.ros.lmsdesktopclient.util.enums.PropertyType;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoadBooksCommandTest {

    @Mock
    private SearchBookModel searchBookModel;

    @Mock
    private BookService bookService;

    @Mock
    private ExecutorService executorService;

    @Mock
    private UiExecutor javaFxUiExecutor;

    private SimpleListProperty<BookDisplayModel> books;
    private IntegerProperty totalPages;
    private LoadBooksCommand command;

    @BeforeEach
    void setUp() {
        books = new SimpleListProperty<>(FXCollections.observableArrayList());
        totalPages = new SimpleIntegerProperty(0);
        Map<PropertyType, Property> properties = Map.of(PropertyType.TOTAL_PAGES, totalPages);

        command = new LoadBooksCommand(
                properties,
                searchBookModel,
                books,
                bookService,
                executorService,
                javaFxUiExecutor
        );
    }

    @Test
    void runCommand_shouldLoadBooksAndUpdateUI() throws Exception {
        // Arrange
        when(searchBookModel.getPage()).thenReturn(1);
        when(searchBookModel.getSize()).thenReturn(10);
        when(searchBookModel.getTitle()).thenReturn("Test");
        when(searchBookModel.getGenre()).thenReturn("FICTION");
        when(searchBookModel.getAuthorFirstName()).thenReturn("John");
        when(searchBookModel.getAuthorLastName()).thenReturn("Doe");
        when(searchBookModel.getStatus()).thenReturn("AVAILABLE");

        BookDTO bookDTO = new BookDTO(
                1L, "12345", "Test Book",
                Set.of(new AuthorDTO("John", "Doe")),
                Set.of(GenreType.FICTION),
                true, "/path/to/image.jpg"
        );
        PaginatedBooksDTO paginatedBooks = new PaginatedBooksDTO(
                List.of(bookDTO), 1, 10
        );
        when(bookService.searchBooks(any(SearchBookDTO.class))).thenReturn(paginatedBooks);

        // Act
        command.runCommand();

        // Assert - Verify UI updates
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(javaFxUiExecutor).runLater(runnableCaptor.capture());

        // Execute the UI update
        runnableCaptor.getValue().run();

        assertEquals(1, books.size());
        assertEquals("12345", books.getFirst().getIsbn());
        assertEquals("Test Book", books.getFirst().getTitle());
        assertEquals("John Doe", books.getFirst().getAuthors());
        assertEquals("FICTION", books.getFirst().getGenres());
        assertEquals("AVAILABLE", books.getFirst().getStatus());
        assertEquals(1, totalPages.get());
        verify(searchBookModel).setSize(10);
    }

    @Test
    void runCommand_shouldHandleNullValuesInSearchCriteria() throws Exception {
        // Arrange
        when(searchBookModel.getPage()).thenReturn(1);
        when(searchBookModel.getSize()).thenReturn(10);
        when(searchBookModel.getTitle()).thenReturn(null);
        when(searchBookModel.getGenre()).thenReturn(null);
        when(searchBookModel.getAuthorFirstName()).thenReturn(null);
        when(searchBookModel.getAuthorLastName()).thenReturn(null);
        when(searchBookModel.getStatus()).thenReturn(null);

        PaginatedBooksDTO paginatedBooks = new PaginatedBooksDTO(List.of(), 0, 0);
        when(bookService.searchBooks(any(SearchBookDTO.class))).thenReturn(paginatedBooks);

        // Act
        command.runCommand();

        // Assert
        verify(bookService).searchBooks(argThat(dto ->
                dto.title() == null &&
                        dto.genre() == null &&
                        dto.authorFirstName() == null &&
                        dto.authorLastName() == null &&
                        dto.isAvailable() == null
        ));
    }

    @Test
    void runCommand_shouldMapMultipleAuthorsAndGenresCorrectly() throws Exception {
        // Arrange
        when(searchBookModel.getPage()).thenReturn(1);
        when(searchBookModel.getSize()).thenReturn(10);

        BookDTO bookDTO = new BookDTO(
                1L, "12345", "Test Book",
                Set.of(
                        new AuthorDTO("John", "Doe"),
                        new AuthorDTO("Jane", "Smith")
                ),
                Set.of(GenreType.FICTION, GenreType.SCIENCE),
                true, "/path/to/image.jpg"
        );
        PaginatedBooksDTO paginatedBooks = new PaginatedBooksDTO(
                List.of(bookDTO), 1, 10
        );
        when(bookService.searchBooks(any(SearchBookDTO.class))).thenReturn(paginatedBooks);

        // Act
        command.runCommand();

        // Assert - Verify UI updates
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(javaFxUiExecutor).runLater(runnableCaptor.capture());
        runnableCaptor.getValue().run();

        assertEquals("Jane Smith, John Doe", books.get(0).getAuthors());
        assertEquals("FICTION, SCIENCE", books.get(0).getGenres());
    }

    @Test
    void runCommand_shouldHandleServiceExceptions() throws Exception {
        // Arrange
        when(searchBookModel.getPage()).thenReturn(1);
        when(searchBookModel.getSize()).thenReturn(10);
        when(bookService.searchBooks(any(SearchBookDTO.class)))
                .thenThrow(new RuntimeException("Service error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> command.runCommand());
    }
}
