package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.models.AuthorInputModel;
import com.ros.lmsdesktopclient.models.AuthorModel;
import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.models.GenreInputModel;
import com.ros.lmsdesktopclient.commands.*;
import com.ros.lmsdesktopclient.util.enums.CommandType;
import com.ros.lmsdesktopclient.util.enums.GenreType;
import javafx.beans.property.ListProperty;

import javax.inject.Inject;
import java.util.*;

public class AddBookViewModel {
    private final Command openMainViewCommand;
    private final Command addAuthorCommand;
    private final Command addGenreCommand;
    private final Command addBookCommand;
    private final Command selectFileCommand;
    private final ListProperty<AuthorInputModel> authorInputs;
    private final ListProperty<GenreInputModel> genreInputs;
    private final BookModel bookModel;

    @Inject
    public AddBookViewModel(
            Map<CommandType, Command> commands,
            ListProperty<AuthorInputModel> authorInputs,
            ListProperty<GenreInputModel> genreInputs,
            BookModel bookModel,
            List<AuthorModel> authors,
            Set<GenreType> genres
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

        this.genreInputs = genreInputs;

        this.bookModel = bookModel;

        //Inserting the first empty genre to display the combo-box in the table-view
        GenreInputModel genreInputModel = new GenreInputModel(genres);
        genreInputModel.getCbGenres().valueProperty().bindBidirectional(bookModel.getGenres().getFirst());
        this.genreInputs.addFirst(genreInputModel);

        addGenreCommand = commands.get(CommandType.ADD_GENRE);

        //BookService bookService = ServiceFactory.createProxy(BookService.class, new BookServiceImpl());
        this.addBookCommand = commands.get(CommandType.ADD_BOOK);

        this.selectFileCommand = commands.get(CommandType.SELECT_FILE);
    }

    public ListProperty<AuthorInputModel> authorInputsProperty() {
        return authorInputs;
    }

    public ListProperty<GenreInputModel> genreInputsProperty() {
        return genreInputs;
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

    public void executeAddGenreCommand(){
        this.addGenreCommand.execute();
    }

    public void executeAddBookCommand(){
        this.addBookCommand.execute();
    }

    public void executeSelectCoverImageCommand() {
        this.selectFileCommand.execute();
    }

}
