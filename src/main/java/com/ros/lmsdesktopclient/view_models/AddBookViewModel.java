package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.dtos.GetAllGenresDTO;
import com.ros.lmsdesktopclient.models.AuthorInputModel;
import com.ros.lmsdesktopclient.models.AuthorModel;
import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.models.GenreInputModel;
import com.ros.lmsdesktopclient.services.ServiceFactory;
import com.ros.lmsdesktopclient.services.service.AddBookService;
import com.ros.lmsdesktopclient.services.service_impl.AddBookServiceImpl;
import com.ros.lmsdesktopclient.util.Genres;
import com.ros.lmsdesktopclient.util.Views;
import com.ros.lmsdesktopclient.view_models.commands.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddBookViewModel {
    private final Command openMainViewCommand;
    private final Command addAuthorCommand;
    private final Command addGenreCommand;
    private final Command addBookCommand;
    private final ListProperty<AuthorInputModel> authorInputs;
    private final ListProperty<GenreInputModel> genreInputs;
    private BookModel book;

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
        //Consume the getAllGenres service here
        GetAllGenresDTO getAllGenresDTO = new GetAllGenresDTO(
                Arrays.stream(Genres.values())
                        .map(Genres::getStr)
                        .collect(Collectors.toSet())
        );
        GenreInputModel genreInputModel = new GenreInputModel(getAllGenresDTO);
        book = new BookModel();
        genreInputModel.getCbGenres().valueProperty().bindBidirectional(book.getGenres().getFirst());
        genreInputs.addFirst(genreInputModel);
        addGenreCommand = new AddGenreCommand(genreInputs.get(), book, getAllGenresDTO);
        AddBookService addBookService = ServiceFactory.createProxy(AddBookService.class, new AddBookServiceImpl());
        addBookCommand = new AddBookCommand(book, authors, addBookService);
    }

    public ObservableList<AuthorInputModel> getAuthorInputs() {
        return authorInputs.get();
    }

    public ListProperty<AuthorInputModel> authorInputsProperty() {
        return authorInputs;
    }

    public ObservableList<GenreInputModel> getGenreInputs() {
        return genreInputs.get();
    }

    public ListProperty<GenreInputModel> genreInputsProperty() {
        return genreInputs;
    }

    public BookModel getBook() {
        return book;
    }

    public void setBook(BookModel book) {
        this.book = book;
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
