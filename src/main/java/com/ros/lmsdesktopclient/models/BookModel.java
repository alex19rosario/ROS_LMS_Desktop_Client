package com.ros.lmsdesktopclient.models;
import com.ros.lmsdesktopclient.util.Genres;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

public class BookModel implements Model {
    private final StringProperty isbn;
    private final StringProperty title;
    private final ListProperty<StringProperty> genres;

    public  BookModel(){
        this.isbn = new SimpleStringProperty("");
        this.title = new SimpleStringProperty("");
        this.genres = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.genres.addFirst(new SimpleStringProperty(Genres.SCIENCE.getStr()));
    }

    public String getIsbn() {
        return isbn.get();
    }

    public StringProperty isbnProperty() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public ObservableList<StringProperty> getGenres() {
        return genres.get();
    }

    public ListProperty<StringProperty> genresProperty() {
        return genres;
    }

    public void setGenres(ObservableList<StringProperty> genres) {
        this.genres.set(genres);
    }

    @Override
    public void clear() {
        this.setIsbn("");
        this.setTitle("");
        this.getGenres().clear();
        this.setGenres(FXCollections.observableArrayList(new SimpleStringProperty(Genres.SCIENCE.getStr())));
    }

    @Override
    public boolean isComplete() {
        return !this.getIsbn().isEmpty() &&
                !this.getTitle().isEmpty() &&
                this.genres.stream().noneMatch(genre -> Arrays.stream(genre.get().split(""))
                        .noneMatch(c -> c.equalsIgnoreCase(" "))) &&
                Arrays.stream(this.getIsbn().split(""))
                        .noneMatch(c -> c.equalsIgnoreCase(" ")) &&
                Arrays.stream(this.getTitle().split(""))
                        .noneMatch(c -> c.equalsIgnoreCase(" "));
    }

    @Override
    public String toString() {
        return "BookModel{" +
                "isbn=" + isbn.get() +
                ", title=" + title.get() +
                ", genres=" + genres.get().stream().toList() +
                '}';
    }
}
