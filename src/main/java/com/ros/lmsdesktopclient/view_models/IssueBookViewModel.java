package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.commands.*;
import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.models.SearchBookModel;
import com.ros.lmsdesktopclient.models.SelectedBookModel;
import com.ros.lmsdesktopclient.util.enums.CommandType;
import com.ros.lmsdesktopclient.util.enums.PropertyType;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

import javax.inject.Inject;
import java.util.Map;

public class IssueBookViewModel {

    private final StringProperty isbn;
    private final SearchBookModel searchBookModel;
    private final ListProperty<BookDisplayModel> books;
    private final IntegerProperty totalPages;
    private final SelectedBookModel selectedBookModel;
    private final ObjectProperty<BookDisplayModel> selectedRowModel;
    private final StringProperty memberUsername;

    private final Command searchBooksCommand;
    private final Command clearFilterCommand;
    private final Command loadBooksCommand;
    private final Command selectBookCommand;
    private final Command openMainViewCommand;
    private final Command issueBookCommand;
    private final static int FIRST_PAGE = 0;

    @Inject
    public IssueBookViewModel(
            Map<PropertyType, Property> properties,
            SearchBookModel searchBookModel,
            ListProperty<BookDisplayModel> books,
            SelectedBookModel selectedBookModel,
            ObjectProperty<BookDisplayModel> selectedRowModel,
            Map<CommandType, Command> commands
    ) {
        this.isbn = (StringProperty) properties.get(PropertyType.ISBN);
        this.searchBookModel = searchBookModel;
        this.books = books;
        this.totalPages = (IntegerProperty) properties.get(PropertyType.TOTAL_PAGES);
        this.selectedBookModel = selectedBookModel;
        this.selectedRowModel = selectedRowModel;
        this.memberUsername = (StringProperty) properties.get(PropertyType.MEMBER_USERNAME);

        this.searchBooksCommand = commands.get(CommandType.SEARCH_BOOKS);
        this.clearFilterCommand = commands.get(CommandType.CLEAR_FILTER);
        this.loadBooksCommand = commands.get(CommandType.LOAD_BOOKS);
        this.selectBookCommand = commands.get(CommandType.SELECT_BOOK);
        this.openMainViewCommand = commands.get(CommandType.OPEN_VIEW_MAIN_MENU);
        this.issueBookCommand = commands.get(CommandType.ISSUE_BOOK);
    }

    public String getIsbn() {
        return isbn.get();
    }

    public StringProperty isbnProperty() {
        return isbn;
    }

    public SearchBookModel getSearchBookModel() {
        return searchBookModel;
    }

    public ObservableList<BookDisplayModel> getBooks() {
        return books.get();
    }

    public ListProperty<BookDisplayModel> booksProperty() {
        return books;
    }

    public SelectedBookModel getSelectedBookModel() {
        return selectedBookModel;
    }

    public StringProperty memberUsernameProperty() {
        return memberUsername;
    }

    public IntegerProperty totalPagesProperty() {
        return totalPages;
    }

    public BookDisplayModel getSelectedRowModel() {
        return selectedRowModel.get();
    }

    public ObjectProperty<BookDisplayModel> selectedRowModelProperty() {
        return selectedRowModel;
    }

    public void setSelectedRowModel(BookDisplayModel selectedRowModel) {
        this.selectedRowModel.set(selectedRowModel);
    }

    public void executeSearchBooksCommand() {
        // Reset pagination to the first page before executing a new search.
        // This ensures the results always start from the beginning when search criteria changes.
        Platform.runLater(() -> searchBookModel.setPage(FIRST_PAGE));

        this.searchBooksCommand.execute();
    }

    public void executeClearFilterCommand() {
        clearFilterCommand.execute();

        clearFilterCommand.getCommandTask().setOnSucceeded(event -> {
            loadBooksCommand.execute();
        });
    }

    public Command getSearchBooksCommand() {
        return searchBooksCommand;
    }

    public Command getIssueBookCommand() {
        return issueBookCommand;
    }

    public Command getLoadBooksCommand() {
        return loadBooksCommand;
    }

    public Command getSelectBookCommand() {
        return selectBookCommand;
    }

    public void executeLoadPageCommand() {
        loadBooksCommand.execute();
    }

    public void executeSelectBookCommand() {
        selectBookCommand.execute();
    }

    public void executeOpenMainViewCommand(){
        openMainViewCommand.execute();
    }

    public void executeIssueBookCommand() {
        issueBookCommand.execute();
    }

}
