package com.ros.lmsdesktopclient.view_models;


import com.ros.lmsdesktopclient.commands.Command;
import com.ros.lmsdesktopclient.models.AuthorModel;
import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.models.GenreModel;
import com.ros.lmsdesktopclient.util.enums.CommandType;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddBookViewModelTest {

    @Mock private Command openMainViewCommand;
    @Mock private Command addAuthorCommand;
    @Mock private Command addBookCommand;
    @Mock private Command selectFileCommand;

    @Mock private BookModel bookModel;

    private ObservableList<GenreModel> genreList;
    private ListProperty<AuthorModel> authorListProperty;

    private AddBookViewModel viewModel;

    @BeforeEach
    void setUp() {
        // Initialize genre list and author list
        genreList = FXCollections.observableArrayList();
        authorListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());

        // Command map
        Map<CommandType, Command> commands = new EnumMap<>(CommandType.class);
        commands.put(CommandType.OPEN_VIEW_MAIN_MENU, openMainViewCommand);
        commands.put(CommandType.ADD_AUTHOR, addAuthorCommand);
        commands.put(CommandType.ADD_BOOK, addBookCommand);
        commands.put(CommandType.SELECT_FILE, selectFileCommand);

        viewModel = new AddBookViewModel(commands, bookModel, genreList, authorListProperty);
    }

    @Test
    void constructor_shouldAddInitialAuthorModel() {
        assertThat(authorListProperty).hasSize(1);
        assertThat(authorListProperty.getFirst()).isInstanceOf(AuthorModel.class);
    }

    @Test
    void getGenreModelObservableList_shouldReturnGenreList() {
        assertThat(viewModel.getGenreModelObservableList()).isSameAs(genreList);
    }

    @Test
    void getAuthorModelListProperty_shouldReturnAuthorList() {
        assertThat(viewModel.getAuthorModelListProperty()).isSameAs(authorListProperty.get());
    }

    @Test
    void getBookModel_shouldReturnBookModel() {
        assertThat(viewModel.getBookModel()).isSameAs(bookModel);
    }

    @Test
    void getAddBookCommand_shouldReturnAddBookCommand() {
        assertThat(viewModel.getAddBookCommand()).isSameAs(addBookCommand);
    }

    @Test
    void executeOpenMainViewCommand_shouldCallExecute() {
        viewModel.executeOpenMainViewCommand();
        verify(openMainViewCommand, times(1)).execute();
    }

    @Test
    void executeAddAuthorCommand_shouldCallExecute() {
        viewModel.executeAddAuthorCommand();
        verify(addAuthorCommand, times(1)).execute();
    }

    @Test
    void executeAddBookCommand_shouldCallExecute() {
        viewModel.executeAddBookCommand();
        verify(addBookCommand, times(1)).execute();
    }

    @Test
    void executeSelectCoverImageCommand_shouldCallExecute() {
        viewModel.executeSelectCoverImageCommand();
        verify(selectFileCommand, times(1)).execute();
    }
}
