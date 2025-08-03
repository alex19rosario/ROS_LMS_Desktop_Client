package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.models.GenreInputModel;
import com.ros.lmsdesktopclient.util.CommandType;
import com.ros.lmsdesktopclient.util.GenreType;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

public class AddGenreCommand extends Command{

    private final ObservableList<GenreInputModel> genreInputs;
    private final BookModel book;
    private final Set<String> genres;

    @Inject
    public AddGenreCommand(ListProperty<GenreInputModel> genreInputs, BookModel book, Set<GenreType> genres){
        this.genreInputs = genreInputs.get();
        this.book = book;
        this.genres = genres.stream()
                .map(GenreType::getStr)
                .collect(Collectors.toSet());
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                GenreInputModel genreInputModel = new GenreInputModel(
                        genres.stream()
                        .map(GenreType::valueOf)
                        .collect(Collectors.toSet())
                );
                book.getGenres().addFirst(new SimpleStringProperty(GenreType.SCIENCE.getStr()));
                genreInputModel.getCbGenres().valueProperty().bindBidirectional(book.getGenres().getFirst());
                genreInputs.addFirst(genreInputModel);
                return null;
            }
        };
    }


}
