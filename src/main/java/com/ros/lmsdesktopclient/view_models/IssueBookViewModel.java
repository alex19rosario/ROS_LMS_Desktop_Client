package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.commands.*;
import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.models.SearchBookModel;
import com.ros.lmsdesktopclient.models.SelectedBookModel;
import com.ros.lmsdesktopclient.services.ServiceFactory;
import com.ros.lmsdesktopclient.services.service.BookService;
import com.ros.lmsdesktopclient.services.service.LoanService;
import com.ros.lmsdesktopclient.services.service.StorageService;
import com.ros.lmsdesktopclient.services.service_impl.BookServiceImpl;
import com.ros.lmsdesktopclient.services.service_impl.LoanServiceImpl;
import com.ros.lmsdesktopclient.services.service_impl.StorageServiceImpl;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.Views;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.http.HttpClient;

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

    public IssueBookViewModel() {
        BookService bookService = ServiceFactory.createProxy(BookService.class, new BookServiceImpl(HttpClient.newHttpClient()));
        StorageService storageService = ServiceFactory.createProxy(StorageService.class, new StorageServiceImpl());
        LoanService loanService = ServiceFactory.createProxy(LoanService.class, new LoanServiceImpl());

        isbn = new SimpleStringProperty();
        searchBookModel = new SearchBookModel();
        books = new SimpleListProperty<>(FXCollections.observableArrayList());
        totalPages = new SimpleIntegerProperty();
        selectedBookModel = new SelectedBookModel();
        selectedRowModel = new SimpleObjectProperty<>();
        memberUsername = new SimpleStringProperty();

        searchBooksCommand = new SearchBooksCommand(isbn, totalPages, searchBookModel, books, bookService);
        clearFilterCommand = new ClearFilterCommand(isbn, searchBookModel);
        loadBooksCommand = new LoadBooksCommand(totalPages, searchBookModel, books, bookService);
        selectBookCommand = new SelectBookCommand(selectedBookModel, selectedRowModel, storageService);
        openMainViewCommand = new OpenViewCommand(Views.MAIN_MENU);
        issueBookCommand = new IssueBookCommand(selectedRowModel, memberUsername, loanService);
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

    public void executeLoadPageCommand() {
        loadBooksCommand.execute();
    }

    public void executeSelectBookCommand() {
        selectBookCommand.execute();
    }

    public void executeOpenMainViewCommand(){
        this.openMainViewCommand.execute();
    }

    public void executeIssueBookCommand() {
        this.issueBookCommand.execute();
    }

}
