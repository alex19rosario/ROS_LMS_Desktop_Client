package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.commands.Command;
import com.ros.lmsdesktopclient.models.AuthorInputModel;
import com.ros.lmsdesktopclient.models.AuthorModel;
import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.models.GenreInputModel;
import com.ros.lmsdesktopclient.util.enums.CommandType;
import com.ros.lmsdesktopclient.util.enums.GenreType;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddBookViewModelTest {

    @Mock private Command openMainViewCommand;
    @Mock private Command addAuthorCommand;
    @Mock private Command addGenreCommand;
    @Mock private Command addBookCommand;
    @Mock private Command selectFileCommand;

    @Mock private ListProperty<AuthorInputModel> authorInputs;
    @Mock private ListProperty<GenreInputModel> genreInputs;

    @Mock private BookModel bookModel;

    private List<AuthorModel> authors;

    private AddBookViewModel viewModel;

    @BeforeEach
    void setUp() {
        Map<CommandType, Command> commands = new EnumMap<>(CommandType.class);
        commands.put(CommandType.OPEN_VIEW_MAIN_MENU, openMainViewCommand);
        commands.put(CommandType.ADD_AUTHOR, addAuthorCommand);
        commands.put(CommandType.ADD_GENRE, addGenreCommand);
        commands.put(CommandType.ADD_BOOK, addBookCommand);
        commands.put(CommandType.SELECT_FILE, selectFileCommand);

        authors = new ArrayList<>();
        Set<GenreType> genres = EnumSet.allOf(GenreType.class); // include all enum values

        // We mock these as partial mocks so we can verify addFirst calls
        doAnswer(invocation -> {
            AuthorInputModel model = invocation.getArgument(0);
            return null;
        }).when(authorInputs).addFirst(any());

        doAnswer(invocation -> {
            AuthorInputModel model = invocation.getArgument(0);
            return null;
        }).when(authorInputs).addFirst(any());

        // Also mock getGenres().getFirst() for BookModel
        ObservableList<StringProperty> genreList = FXCollections.observableArrayList();
        genreList.add(new javafx.beans.property.SimpleStringProperty("SCIENCE"));
        when(bookModel.getGenres()).thenReturn(new javafx.beans.property.SimpleListProperty<>(genreList));

        viewModel = new AddBookViewModel(commands, authorInputs, genreInputs, bookModel, authors, genres);
    }

    @Test
    void authorInputsProperty_shouldReturnAuthorInputs() {
        assertSame(authorInputs, viewModel.authorInputsProperty());
    }

    @Test
    void genreInputsProperty_shouldReturnGenreInputs() {
        assertSame(genreInputs, viewModel.genreInputsProperty());
    }

    @Test
    void getBookModel_shouldReturnInjectedBookModel() {
        assertSame(bookModel, viewModel.getBookModel());
    }

    @Test
    void getAddBookCommand_shouldReturnAddBookCommand() {
        assertSame(addBookCommand, viewModel.getAddBookCommand());
    }

    @Test
    void executeOpenMainViewCommand_shouldCallExecute() {
        viewModel.executeOpenMainViewCommand();
        verify(openMainViewCommand).execute();
    }

    @Test
    void executeAddAuthorCommand_shouldCallExecute() {
        viewModel.executeAddAuthorCommand();
        verify(addAuthorCommand).execute();
    }

    @Test
    void executeAddGenreCommand_shouldCallExecute() {
        viewModel.executeAddGenreCommand();
        verify(addGenreCommand).execute();
    }

    @Test
    void executeAddBookCommand_shouldCallExecute() {
        viewModel.executeAddBookCommand();
        verify(addBookCommand).execute();
    }

    @Test
    void executeSelectCoverImageCommand_shouldCallExecute() {
        viewModel.executeSelectCoverImageCommand();
        verify(selectFileCommand).execute();
    }

    @Test
    void constructor_shouldAddFirstAuthorModelAndInput() {
        verify(authorInputs, times(1)).addFirst(any(AuthorInputModel.class));
        assertEquals(1, authors.size());
    }

    @Test
    void constructor_shouldAddFirstGenreModel() {
        verify(genreInputs, times(1)).addFirst(any(GenreInputModel.class));
    }

}
