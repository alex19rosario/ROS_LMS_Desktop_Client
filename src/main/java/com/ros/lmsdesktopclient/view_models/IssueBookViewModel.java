package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.commands.ClearFilterCommand;
import com.ros.lmsdesktopclient.commands.Command;
import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.models.SearchBookModel;
import com.ros.lmsdesktopclient.models.SelectedBookModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class IssueBookViewModel {

    private final StringProperty isbn;
    private final SearchBookModel searchBookModel;
    private final BookDisplayModel bookDisplayModel;
    private final SelectedBookModel selectedBookModel;
    private final StringProperty memberUsername;

//    private final Command searchBooksCommand;
    private final Command clearFilterCommand;
//    private final Command openMainViewCommand;
//    private final Command issueBookCommand;

    public IssueBookViewModel() {
        isbn = new SimpleStringProperty();
        searchBookModel = new SearchBookModel();
        bookDisplayModel = new BookDisplayModel();
        selectedBookModel = new SelectedBookModel();
        memberUsername = new SimpleStringProperty();
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

    public BookDisplayModel getBookDisplayModel() {
        return bookDisplayModel;
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

    public void executeClearFilterCommand() {
        this.clearFilterCommand.execute();
    }
}
