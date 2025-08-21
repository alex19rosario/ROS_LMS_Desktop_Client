package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.dtos.AuthorDTO;
import com.ros.lmsdesktopclient.dtos.BookDTO;
import com.ros.lmsdesktopclient.dtos.PaginatedBooksDTO;
import com.ros.lmsdesktopclient.dtos.SearchBookDTO;
import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.models.SearchBookModel;
import com.ros.lmsdesktopclient.services.service.BookService;
import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.enums.Alerts;
import com.ros.lmsdesktopclient.util.enums.BookStatus;
import com.ros.lmsdesktopclient.util.enums.GenreType;
import com.ros.lmsdesktopclient.util.enums.PropertyType;
import com.ros.lmsdesktopclient.util.exceptions.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchBooksCommandTest {

    @Mock BookService bookService;
    @Mock ExecutorService executorService;
    @Mock Command openLoginViewCommand;

    StringProperty isbn;
    IntegerProperty totalPages;
    ListProperty<BookDisplayModel> books;
    SearchBookModel searchBookModel;

    UiExecutor immediateUiExecutor;
    SearchBooksCommand command;

    @BeforeEach
    void setup() {
        isbn = new SimpleStringProperty("");
        totalPages = new SimpleIntegerProperty(0);
        books = new SimpleListProperty<>(FXCollections.observableArrayList());
        searchBookModel = mock(SearchBookModel.class);

        immediateUiExecutor = Runnable::run;

        command = new SearchBooksCommand(
                Map.of(
                        PropertyType.ISBN, isbn,
                        PropertyType.TOTAL_PAGES, totalPages
                ),
                searchBookModel,
                books,
                bookService,
                executorService,
                immediateUiExecutor,
                openLoginViewCommand
        );
    }

    @Test
    void runCommand_withIsbn_shouldSearchByIsbnAndUpdateBooks() throws Exception {
        isbn.set("12345");
        BookDTO dto = new BookDTO(1L, "12345", "Title",
                Set.of(new AuthorDTO("John", "Doe")),
                Set.of(GenreType.SCIENCE),
                true,
                "cover.png");

        when(bookService.searchBookByIsbn("12345")).thenReturn(dto);

        command.runCommand();

        assertEquals(1, books.size());
        assertEquals("Title", books.getFirst().getTitle());
        assertEquals("AVAILABLE", books.getFirst().getStatus());
        verify(searchBookModel).setPage(0);
    }

    @Test
    void runCommand_withoutIsbn_shouldSearchBooksAndUpdateList() throws Exception {
        isbn.set("");
        when(searchBookModel.isComplete()).thenReturn(true);
        when(searchBookModel.getStatus()).thenReturn(BookStatus.AVAILABLE.toString());
        when(searchBookModel.getGenre()).thenReturn(GenreType.ADVENTURE.getStr());
        when(searchBookModel.getPage()).thenReturn(0);
        when(searchBookModel.getSize()).thenReturn(10);
        when(searchBookModel.getTitle()).thenReturn("SomeTitle");
        when(searchBookModel.getAuthorFirstName()).thenReturn("John");
        when(searchBookModel.getAuthorLastName()).thenReturn("Doe");

        BookDTO dto1 = new BookDTO(1L, "111", "First",
                Set.of(new AuthorDTO("A", "A")), Set.of(GenreType.ADVENTURE),
                true, "a.png");
        BookDTO dto2 = new BookDTO(2L, "222", "Second",
                Set.of(new AuthorDTO("B", "B")), Set.of(GenreType.ADVENTURE),
                false, "b.png");

        PaginatedBooksDTO page = new PaginatedBooksDTO(List.of(dto1, dto2), 5, 20);
        when(bookService.searchBooks(any(SearchBookDTO.class))).thenReturn(page);

        command.runCommand();

        assertEquals(2, books.size());
        assertEquals(5, totalPages.get());
        verify(searchBookModel).setSize(20);
    }

    @Test
    void runCommand_withEmptyForm_shouldThrowEmptyFieldsException() {
        isbn.set("");
        when(searchBookModel.isComplete()).thenReturn(false);
        assertThrows(EmptyFieldsException.class, command::runCommand);
    }

    @Test
    void runCommand_withServiceFailure_shouldStoreAndRethrow() throws Exception {
        isbn.set("12345");
        doThrow(new NetworkException("down"))
                .when(bookService).searchBookByIsbn("12345");

        assertThrows(NetworkException.class, command::runCommand);
        assertInstanceOf(NetworkException.class, command.getLastException());
    }

    @Test
    void onFailure_shouldShowEmptyFieldsAlert() {
        SearchBooksCommand spyCommand = spy(command);
        spyCommand.setLastException(new EmptyFieldsException("msg"));
        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.EMPTY_FIELDS_WARN);
        verify(alertMock).getModal("msg");
    }

    @Test
    void onFailure_shouldShowNetworkErrorAlert() {
        SearchBooksCommand spyCommand = spy(command);
        spyCommand.setLastException(new NetworkException("network"));
        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.NETWORK_ERROR);
        verify(alertMock).getModal("network");
    }

    @Test
    void onFailure_shouldShowServerErrorAlert() {
        SearchBooksCommand spyCommand = spy(command);
        spyCommand.setLastException(new ServerErrorException("server"));
        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.SERVER_ERROR);
        verify(alertMock).getModal("server");
    }

    @Test
    void onFailure_shouldShowBookNotFoundAlert() {
        SearchBooksCommand spyCommand = spy(command);
        spyCommand.setLastException(new BookNotFoundException("not found"));
        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.BOOK_NOT_FOUND);
        verify(alertMock).getModal("not found");
    }

    @Test
    void onFailure_shouldShowExpiredSessionAndOpenLoginView() {
        SearchBooksCommand spyCommand = spy(command);
        spyCommand.setLastException(new ExpiredSessionException("expired"));
        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.EXPIRED_SESSION_ERROR);
        verify(alertMock).getModal("expired");
        verify(openLoginViewCommand).execute();
    }

    @Test
    void onFailure_withUnexpectedException_shouldThrowIllegalStateException() {
        command.setLastException(new RuntimeException("unexpected"));
        assertThrows(IllegalStateException.class, command::onFailure);
    }

}
