package com.ros.lmsdesktopclient.view_models.commands;

import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.models.GenreInputModel;
import com.ros.lmsdesktopclient.util.Genres;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.Set;

public class AddGenreCommand extends Command{

    private final ObservableList<GenreInputModel> genreInputs;
    private final BookModel book;
    private final Set<String> genres;

    public AddGenreCommand(ObservableList<GenreInputModel> genreInputs, BookModel book, Set<String> genres){
        this.genreInputs = genreInputs;
        this.book = book;
        this.genres = genres;
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                GenreInputModel genreInputModel = new GenreInputModel(genres);
                book.getGenres().addFirst(new SimpleStringProperty(Genres.SCIENCE.getStr()));
                genreInputModel.getCbGenres().valueProperty().bindBidirectional(book.getGenres().getFirst());
                genreInputs.addFirst(genreInputModel);
                return null;
            }
        };
    }


}
