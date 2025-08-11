package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.models.AuthorInputModel;
import com.ros.lmsdesktopclient.models.AuthorModel;
import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.commands.*;
import com.ros.lmsdesktopclient.models.GenreModel;
import com.ros.lmsdesktopclient.util.enums.CommandType;
import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;

import javax.inject.Inject;
import java.util.*;

public class AddBookViewModel {
    private final Command openMainViewCommand;
    private final Command addAuthorCommand;
    private final Command addBookCommand;
    private final Command selectFileCommand;
    private final ListProperty<AuthorInputModel> authorInputs;
    private final BookModel bookModel;
    private final ObservableList<GenreModel> genreModelObservableList;

    @Inject
    public AddBookViewModel(
            Map<CommandType, Command> commands,
            ListProperty<AuthorInputModel> authorInputs,
            BookModel bookModel,
            List<AuthorModel> authors,
            ObservableList<GenreModel> genreModelObservableList
    ){
        openMainViewCommand = commands.get(CommandType.OPEN_VIEW_MAIN_MENU);

        this.authorInputs = authorInputs;

        //Inserting the first empty author to display the text-fields in table-view
        AuthorInputModel authorInputModel = new AuthorInputModel();
        AuthorModel author = new AuthorModel();
        authorInputModel.getTfFirstName().textProperty().bindBidirectional(author.firstNameProperty());
        authorInputModel.getTfLastName().textProperty().bindBidirectional(author.lastNameProperty());
        authorInputs.addFirst(authorInputModel);
        authors.addFirst(author);

        addAuthorCommand = commands.get(CommandType.ADD_AUTHOR);

        this.bookModel = bookModel;

        this.addBookCommand = commands.get(CommandType.ADD_BOOK);

        this.selectFileCommand = commands.get(CommandType.SELECT_FILE);

        this.genreModelObservableList = genreModelObservableList;
    }

    public ListProperty<AuthorInputModel> authorInputsProperty() {
        return authorInputs;
    }

    public ObservableList<GenreModel> getGenreModelObservableList() {
        return genreModelObservableList;
    }

    public BookModel getBookModel() {
        return bookModel;
    }

    public Command getAddBookCommand() {
        return addBookCommand;
    }

    public void executeOpenMainViewCommand(){
        this.openMainViewCommand.execute();
    }

    public void executeAddAuthorCommand(){
        this.addAuthorCommand.execute();
    }

    public void executeAddBookCommand(){
        this.addBookCommand.execute();
    }

    public void executeSelectCoverImageCommand() {
        this.selectFileCommand.execute();
    }

}
