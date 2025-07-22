package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.commands.ClearFilterCommand;
import com.ros.lmsdesktopclient.commands.Command;
import com.ros.lmsdesktopclient.commands.SearchBooksCommand;
import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.models.SearchBookModel;
import com.ros.lmsdesktopclient.models.SelectedBookModel;
import com.ros.lmsdesktopclient.services.ServiceFactory;
import com.ros.lmsdesktopclient.services.service.BookService;
import com.ros.lmsdesktopclient.services.service_impl.BookServiceImpl;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class IssueBookViewModel {

    private final StringProperty isbn;
    private final SearchBookModel searchBookModel;
    private final ListProperty<BookDisplayModel> books;
    private final SelectedBookModel selectedBookModel;
    private final StringProperty memberUsername;

    private final Command searchBooksCommand;
    private final Command clearFilterCommand;
//    private final Command openMainViewCommand;
//    private final Command issueBookCommand;

    public IssueBookViewModel() {
        BookService bookService = ServiceFactory.createProxy(BookService.class, new BookServiceImpl());

        isbn = new SimpleStringProperty();
        searchBookModel = new SearchBookModel();
        books = new SimpleListProperty<>(FXCollections.observableArrayList());
        selectedBookModel = new SelectedBookModel();
        memberUsername = new SimpleStringProperty();
        searchBooksCommand = new SearchBooksCommand(searchBookModel, books, bookService);
        clearFilterCommand = new ClearFilterCommand(searchBookModel, isbn);
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

    public void executeSearchBooksCommand() {
        this.searchBooksCommand.execute();
    }

    public void executeClearFilterCommand() {
        this.clearFilterCommand.execute();
    }


}
