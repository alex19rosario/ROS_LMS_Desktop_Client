package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.JavaFxExtension;
import com.ros.lmsdesktopclient.commands.Command;
import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.models.SearchBookModel;
import com.ros.lmsdesktopclient.models.SelectedBookModel;
import com.ros.lmsdesktopclient.util.enums.CommandType;
import com.ros.lmsdesktopclient.util.enums.PropertyType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(JavaFxExtension.class)
class IssueBookViewModelTest {

    @Mock private Command searchBooksCommand;
    @Mock private Command clearFilterCommand;
    @Mock private Command loadBooksCommand;
    @Mock private Command selectBookCommand;
    @Mock private Command openMainViewCommand;
    @Mock private Command issueBookCommand;
    @Mock private SearchBookModel searchBookModel;
    @Mock private SelectedBookModel selectedBookModel;
    @Mock private Task<Void> mockTask;

    private ListProperty<BookDisplayModel> books;
    private ObjectProperty<BookDisplayModel> selectedRowModel;
    private StringProperty isbn;
    private IntegerProperty totalPages;
    private StringProperty memberUsername;

    private IssueBookViewModel viewModel;

    @BeforeEach
    void setUp() {
        isbn = new SimpleStringProperty("12345");
        books = new SimpleListProperty<>(FXCollections.observableArrayList());
        totalPages = new SimpleIntegerProperty(5);
        memberUsername = new SimpleStringProperty("john_doe");
        selectedRowModel = new SimpleObjectProperty<>(null);

        Map<PropertyType, Property> properties = new EnumMap<>(PropertyType.class);
        properties.put(PropertyType.ISBN, isbn);
        properties.put(PropertyType.TOTAL_PAGES, totalPages);
        properties.put(PropertyType.MEMBER_USERNAME, memberUsername);

        Map<CommandType, Command> commands = new EnumMap<>(CommandType.class);
        commands.put(CommandType.SEARCH_BOOKS, searchBooksCommand);
        commands.put(CommandType.CLEAR_FILTER, clearFilterCommand);
        commands.put(CommandType.LOAD_BOOKS, loadBooksCommand);
        commands.put(CommandType.SELECT_BOOK, selectBookCommand);
        commands.put(CommandType.OPEN_VIEW_MAIN_MENU, openMainViewCommand);
        commands.put(CommandType.ISSUE_BOOK, issueBookCommand);

        viewModel = new IssueBookViewModel(
                properties,
                searchBookModel,
                books,
                selectedBookModel,
                selectedRowModel,
                commands
        );
    }

    @Test
    void getIsbn_shouldReturnPropertyValue() {
        assertThat(viewModel.getIsbn()).isEqualTo("12345");
        assertThat(viewModel.isbnProperty()).isSameAs(isbn);
    }

    @Test
    void executeSearchBooksCommand_shouldSetPageToFirstAndExecuteCommand() {
        viewModel.executeSearchBooksCommand();
        verify(searchBookModel, timeout(100)).setPage(0);
        verify(searchBooksCommand, times(1)).execute();
    }

    @Test
    void executeClearFilterCommand_shouldExecuteClearAndThenLoadBooks() {
        // Arrange
        // no more mockTask, we just need to mock Command behavior
        doNothing().when(clearFilterCommand).execute();

        // Act
        viewModel.executeClearFilterCommand();

        // Assert that clearFilterCommand.execute() was called
        verify(clearFilterCommand, times(1)).execute();

        // Capture the Runnable that was set as onCommandSuccess
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(clearFilterCommand).setOnCommandSuccess(runnableCaptor.capture());

        // Simulate success by running the captured Runnable
        runnableCaptor.getValue().run();

        // Verify that loadBooksCommand.execute() gets called after success
        verify(loadBooksCommand, times(1)).execute();
    }

    @Test
    void executeLoadPageCommand_shouldExecuteLoadBooksCommand() {
        viewModel.executeLoadPageCommand();
        verify(loadBooksCommand, times(1)).execute();
    }

    @Test
    void executeSelectBookCommand_shouldExecuteSelectBookCommand() {
        viewModel.executeSelectBookCommand();
        verify(selectBookCommand, times(1)).execute();
    }

    @Test
    void executeOpenMainViewCommand_shouldExecuteOpenMainViewCommand() {
        viewModel.executeOpenMainViewCommand();
        verify(openMainViewCommand, times(1)).execute();
    }

    @Test
    void executeIssueBookCommand_shouldExecuteIssueBookCommand() {
        viewModel.executeIssueBookCommand();
        verify(issueBookCommand, times(1)).execute();
    }

    @Test
    void getSearchBookModel_shouldReturnInjectedInstance() {
        assertSame(searchBookModel, viewModel.getSearchBookModel());
    }

    @Test
    void getBooks_shouldReturnBooksList() {
        assertSame(books.get(), viewModel.getBooks());
    }

    @Test
    void booksProperty_shouldReturnBooksProperty() {
        assertSame(books, viewModel.booksProperty());
    }

    @Test
    void getSelectedBookModel_shouldReturnInjectedInstance() {
        assertSame(selectedBookModel, viewModel.getSelectedBookModel());
    }

    @Test
    void memberUsernameProperty_shouldReturnInjectedInstance() {
        assertSame(memberUsername, viewModel.memberUsernameProperty());
    }

    @Test
    void totalPagesProperty_shouldReturnInjectedInstance() {
        assertSame(totalPages, viewModel.totalPagesProperty());
    }

    @Test
    void getSelectedRowModel_shouldReturnCurrentlySetValue() {
        BookDisplayModel model = mock(BookDisplayModel.class);
        selectedRowModel.set(model);
        assertSame(model, viewModel.getSelectedRowModel());
    }

    @Test
    void selectedRowModelProperty_shouldReturnInjectedInstance() {
        assertSame(selectedRowModel, viewModel.selectedRowModelProperty());
    }

    @Test
    void setSelectedRowModel_shouldUpdateValue() {
        BookDisplayModel model = mock(BookDisplayModel.class);
        viewModel.setSelectedRowModel(model);
        assertSame(model, selectedRowModel.get());
    }

    @Test
    void getSearchBooksCommand_shouldReturnInjectedInstance() {
        assertSame(searchBooksCommand, viewModel.getSearchBooksCommand());
    }

    @Test
    void getIssueBookCommand_shouldReturnInjectedInstance() {
        assertSame(issueBookCommand, viewModel.getIssueBookCommand());
    }

    @Test
    void getLoadBooksCommand_shouldReturnInjectedInstance() {
        assertSame(loadBooksCommand, viewModel.getLoadBooksCommand());
    }

    @Test
    void getSelectBookCommand_shouldReturnInjectedInstance() {
        assertSame(selectBookCommand, viewModel.getSelectBookCommand());
    }
}
