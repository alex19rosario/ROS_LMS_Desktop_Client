package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.models.AuthorInputModel;
import com.ros.lmsdesktopclient.models.AuthorModel;
import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.models.GenreInputModel;
import com.ros.lmsdesktopclient.services.ServiceFactory;
import com.ros.lmsdesktopclient.services.service.BookService;
import com.ros.lmsdesktopclient.services.service_impl.BookServiceImpl;
import com.ros.lmsdesktopclient.util.UpFrontDataHandler;
import com.ros.lmsdesktopclient.util.Views;
import com.ros.lmsdesktopclient.view_models.commands.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.*;


public class AddBookViewModel {
    private final Command openMainViewCommand;
    private final Command addAuthorCommand;
    private final Command addGenreCommand;
    private final Command addBookCommand;
    private final ListProperty<AuthorInputModel> authorInputs;
    private final ListProperty<GenreInputModel> genreInputs;
    private final BookModel bookModel;

    public AddBookViewModel(){
        openMainViewCommand = new OpenViewCommand(Views.MAIN_MENU);

        authorInputs = new SimpleListProperty<>(FXCollections.observableArrayList());
        List<AuthorModel> authors = new ArrayList<>();
        AuthorInputModel authorInputModel = new AuthorInputModel();
        AuthorModel author = new AuthorModel();
        authorInputModel.getTfFirstName().textProperty().bindBidirectional(author.firstNameProperty());
        authorInputModel.getTfLastName().textProperty().bindBidirectional(author.lastNameProperty());
        authorInputs.addFirst(authorInputModel);
        authors.addFirst(author);
        addAuthorCommand = new AddAuthorCommand(authorInputs.get(), authors);

        genreInputs = new SimpleListProperty<>(FXCollections.observableArrayList());
        Set<String> genres = UpFrontDataHandler.getInstance().getGenres();

        GenreInputModel genreInputModel = new GenreInputModel(genres);
        bookModel = new BookModel();
        genreInputModel.getCbGenres().valueProperty().bindBidirectional(bookModel.getGenres().getFirst());
        genreInputs.addFirst(genreInputModel);
        addGenreCommand = new AddGenreCommand(genreInputs.get(), bookModel, genres);
        BookService bookService = ServiceFactory.createProxy(BookService.class, new BookServiceImpl());
        addBookCommand = new AddBookCommand(bookModel, authors, bookService);
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

}
