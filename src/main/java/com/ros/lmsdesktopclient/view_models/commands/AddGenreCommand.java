package com.ros.lmsdesktopclient.view_models.commands;

import com.ros.lmsdesktopclient.dtos.GetAllGenresDTO;
import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.models.GenreInputModel;
import com.ros.lmsdesktopclient.util.Genres;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class AddGenreCommand extends Command{

    private final ObservableList<GenreInputModel> genreInputs;
    private final BookModel book;
    private final GetAllGenresDTO dto;

    public AddGenreCommand(ObservableList<GenreInputModel> genreInputs, BookModel book, GetAllGenresDTO dto){
        this.genreInputs = genreInputs;
        this.book = book;
        this.dto = dto;
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                GenreInputModel genreInputModel = new GenreInputModel(dto);
                book.getGenres().addFirst(new SimpleStringProperty(Genres.SCIENCE.getStr()));
                genreInputModel.getCbGenres().valueProperty().bindBidirectional(book.getGenres().getFirst());
                genreInputs.addFirst(genreInputModel);
                return null;
            }
        };
    }


}
