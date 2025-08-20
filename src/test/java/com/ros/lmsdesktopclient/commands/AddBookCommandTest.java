package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.dtos.AddBookDTO;
import com.ros.lmsdesktopclient.models.AuthorModel;
import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.models.GenreModel;
import com.ros.lmsdesktopclient.services.service.BookService;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.Alerts;
import com.ros.lmsdesktopclient.util.exceptions.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddBookCommandTest {

    @Mock BookService bookService;
    @Mock ExecutorService executorService;
    @Mock UiExecutor uiExecutor;
    @Mock TokenHandler tokenHandler;
    @Mock Command openLoginViewCommand; // injected mock

    BookModel bookModel;
    ListProperty<AuthorModel> authorListProperty;
    ObservableList<GenreModel> genreList;

    AddBookCommand command;

    @BeforeEach
    void setup() {
        bookModel = mock(BookModel.class);
        authorListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        genreList = FXCollections.observableArrayList();

        command = new AddBookCommand(
                bookModel,
                authorListProperty,
                bookService,
                genreList,
                executorService,
                uiExecutor,
                tokenHandler,
                openLoginViewCommand
        );
    }

    @Test
    void runCommand_shouldCallBookServiceWithValidData() throws Exception {
        // arrange
        AuthorModel author = mock(AuthorModel.class);
        when(author.isComplete()).thenReturn(true);
        when(author.getFirstName()).thenReturn("John");
        when(author.getLastName()).thenReturn("Doe");
        authorListProperty.add(author);

        GenreModel genre = mock(GenreModel.class);
        when(genre.isSelected()).thenReturn(true);
        when(genre.getGenre()).thenReturn("SciFi");
        genreList.add(genre);

        when(bookModel.isComplete()).thenReturn(true);
        when(bookModel.getIsbn()).thenReturn("12345");
        when(bookModel.getTitle()).thenReturn("Some Book");
        when(bookModel.getCoverImageFile()).thenReturn(new File("cover.png"));
        when(tokenHandler.getUsername()).thenReturn("librarian");

        // act
        command.runCommand();

        // assert
        verify(bookService).addBook(any(AddBookDTO.class));
    }

    @Test
    void runCommand_withEmptyFields_shouldThrowEmptyFieldsException() {
        when(bookModel.isComplete()).thenReturn(false);
        assertThrows(EmptyFieldsException.class, command::runCommand);
    }

    @Test
    void runCommand_shouldStoreExceptionAndRethrow_whenBookServiceFails() throws Exception {
        AuthorModel author = mock(AuthorModel.class);
        when(author.isComplete()).thenReturn(true);
        when(author.getFirstName()).thenReturn("John");
        when(author.getLastName()).thenReturn("Doe");
        authorListProperty.add(author);

        when(bookModel.isComplete()).thenReturn(true);
        when(bookModel.getIsbn()).thenReturn("12345");
        when(bookModel.getTitle()).thenReturn("Some Book");
        when(tokenHandler.getUsername()).thenReturn("librarian");

        doThrow(new NetworkException("network down"))
                .when(bookService).addBook(any(AddBookDTO.class));

        assertThrows(NetworkException.class, command::runCommand);
        assertInstanceOf(NetworkException.class, command.getLastException());
    }

    @Test
    void onSuccess_shouldClearFormAndResetData() {
        AuthorModel author = new AuthorModel();
        authorListProperty.add(author);
        GenreModel genre = new GenreModel("SciFi", false);
        genre.setSelected(true);
        genreList.add(genre);

        AddBookCommand spyCommand = spy(command);
        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onSuccess();

        verify(spyCommand).setAlert(Alerts.BOOK_ADDED_SUCCESS);
        verify(alertMock).getModal("The book was added successfully");
        verify(bookModel).clear();
        assertEquals(1, authorListProperty.size()); // one empty AuthorModel
        assertFalse(genre.isSelected());
    }

    @Test
    void onFailure_shouldShowEmptyFieldsAlert() {
        AddBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new EmptyFieldsException("Add Book Form: there are empty fields"));

        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.EMPTY_FIELDS_WARN);
        verify(mockAlert).getModal("Add Book Form: there are empty fields");
    }

    @Test
    void onFailure_shouldShowNetworkErrorAlert() {
        AddBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new NetworkException("no connection"));

        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.NETWORK_ERROR);
        verify(mockAlert).getModal("no connection");
    }

    @Test
    void onFailure_shouldShowServerErrorAlert() {
        AddBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new ServerErrorException("Server Error"));

        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.SERVER_ERROR);
        verify(mockAlert).getModal("Server Error");
    }

    @Test
    void onFailure_shouldShowInvalidISBNAlert() {
        AddBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new InvalidISBNException("Invalid ISBN Error"));

        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.INVALID_ISBN_ERROR);
        verify(mockAlert).getModal("Invalid ISBN Error");
    }

    @Test
    void onFailure_shouldShowExpiredSessionAlertAndOpenLoginView() {
        AddBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new ExpiredSessionException("session expired"));

        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.EXPIRED_SESSION_ERROR);
        verify(alertMock).getModal("session expired");

        verify(openLoginViewCommand).execute();
    }

    @Test
    void onFailure_shouldShowBookAlreadyExist() {
        AddBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new BookAlreadyExistException("This book already exists."));

        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.EXISTING_BOOK_ERROR);
        verify(mockAlert).getModal("This book already exists.");
    }


    @Test
    void onFailure_withUnexpectedException_shouldThrowIllegalStateException() {
        command.setLastException(new RuntimeException("unexpected"));
        assertThrows(IllegalStateException.class, command::onFailure);
    }
}
