package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.commands.ClearFilterCommand;
import com.ros.lmsdesktopclient.commands.Command;
import com.ros.lmsdesktopclient.commands.LoadBooksCommand;
import com.ros.lmsdesktopclient.commands.SearchBooksCommand;
import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.models.SearchBookModel;
import com.ros.lmsdesktopclient.models.SelectedBookModel;
import com.ros.lmsdesktopclient.services.ServiceFactory;
import com.ros.lmsdesktopclient.services.service.BookService;
import com.ros.lmsdesktopclient.services.service_impl.BookServiceImpl;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class IssueBookViewModel {

    private final StringProperty isbn;
    private final SearchBookModel searchBookModel;
    private final ListProperty<BookDisplayModel> books;
    private final SelectedBookModel selectedBookModel;
    private final StringProperty memberUsername;
    private final IntegerProperty totalPages;

    private final Command searchBooksCommand;
    private final Command clearFilterCommand;
    private final Command loadBooksCommand;
//    private final Command openMainViewCommand;
//    private final Command issueBookCommand;

    public IssueBookViewModel() {
        BookService bookService = ServiceFactory.createProxy(BookService.class, new BookServiceImpl());

        isbn = new SimpleStringProperty();
        searchBookModel = new SearchBookModel();
        books = new SimpleListProperty<>(FXCollections.observableArrayList());
        selectedBookModel = new SelectedBookModel();
        memberUsername = new SimpleStringProperty();
        totalPages = new SimpleIntegerProperty();

        searchBooksCommand = new SearchBooksCommand(isbn, totalPages, searchBookModel, books, bookService);
        clearFilterCommand = new ClearFilterCommand(isbn, searchBookModel);
        loadBooksCommand = new LoadBooksCommand(totalPages, searchBookModel, books, bookService);
        loadBooksCommand.execute();
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

    public String getMemberUsername() {
        return memberUsername.get();
    }

    public StringProperty memberUsernameProperty() {
        return memberUsername;
    }

    public int getTotalPages() {
        return totalPages.get();
    }

    public IntegerProperty totalPagesProperty() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages.set(totalPages);
    }

    public void executeSearchBooksCommand() {
        // Reset pagination to the first page before executing a new search.
        // This ensures the results always start from the beginning when search criteria changes.
        Platform.runLater(() -> searchBookModel.setPage(0));
        
        this.searchBooksCommand.execute();
    }

    public void executeClearFilterCommand() {
        clearFilterCommand.execute();

        clearFilterCommand.getCommandTask().setOnSucceeded(event -> {
            loadBooksCommand.execute();
        });
    }

    public void executeLoadPageCommand() {
        loadBooksCommand.execute();
    }

}
