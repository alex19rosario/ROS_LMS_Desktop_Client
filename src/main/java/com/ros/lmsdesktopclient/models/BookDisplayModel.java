package com.ros.lmsdesktopclient.models;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A model class representing the data of a book that is to be displayed in a table view
 * in the LMS Desktop Client application.
 */
public class BookDisplayModel {

    private final LongProperty id;
    private final StringProperty isbn;
    private final StringProperty title;
    private final StringProperty authors;
    private final StringProperty genres;
    private final StringProperty status;
    private final StringProperty imagePath;

    public BookDisplayModel() {
        this.id = new SimpleLongProperty();
        this.isbn = new SimpleStringProperty();
        this.title = new SimpleStringProperty();
        this.authors = new SimpleStringProperty();
        this.genres = new SimpleStringProperty();
        this.status = new SimpleStringProperty();
        this.imagePath = new SimpleStringProperty();
    }

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
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

    public String getAuthors() {
        return authors.get();
    }

    public StringProperty authorsProperty() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors.set(authors);
    }

    public String getGenres() {
        return genres.get();
    }

    public StringProperty genresProperty() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres.set(genres);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getImagePath() {
        return imagePath.get();
    }

    public StringProperty imagePathProperty() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath.set(imagePath);
    }

    @Override
    public String toString() {
        return "BookDisplayModel{" +
                "id=" + id +
                ", isbn=" + isbn +
                ", title=" + title +
                ", authors=" + authors +
                ", genres=" + genres +
                ", status=" + status +
                ", imagePath=" + imagePath +
                '}';
    }
}
