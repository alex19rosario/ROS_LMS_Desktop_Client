package com.ros.lmsdesktopclient.view_models;
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

    private final BookModel bookModel;
    private final ObservableList<GenreModel> genreModelObservableList;
    private final ListProperty<AuthorModel> authorModelListProperty;

    @Inject
    public AddBookViewModel(
            Map<CommandType, Command> commands,
            BookModel bookModel,
            ObservableList<GenreModel> genreModelObservableList,
            ListProperty<AuthorModel> authorModelListProperty
    ){
        openMainViewCommand = commands.get(CommandType.OPEN_VIEW_MAIN_MENU);
        this.authorModelListProperty = authorModelListProperty;

        this.authorModelListProperty.addFirst(new AuthorModel());

        addAuthorCommand = commands.get(CommandType.ADD_AUTHOR);

        this.bookModel = bookModel;

        this.addBookCommand = commands.get(CommandType.ADD_BOOK);

        this.selectFileCommand = commands.get(CommandType.SELECT_FILE);

        this.genreModelObservableList = genreModelObservableList;
    }

    public ObservableList<GenreModel> getGenreModelObservableList() {
        return genreModelObservableList;
    }

    public ObservableList<AuthorModel> getAuthorModelListProperty() {
        return authorModelListProperty.get();
    }

    public ListProperty<AuthorModel> authorModelListPropertyProperty() {
        return authorModelListProperty;
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
