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
        // Check if ISBN and Title are non-empty and do not contain spaces
        if (this.getIsbn().isEmpty() || this.getIsbn().contains(" ")) {
            return false;
        }
        if (this.getTitle().isEmpty() || this.getTitle().isBlank()) {
            return false;
        }
        // Check if all genres are non-empty and do not contain spaces
        if (this.genres.isEmpty() || this.genres.stream().anyMatch(genre -> genre.get().trim().isEmpty())) {
            return false;
        }
        // All checks passed
        return true;
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
