package com.ros.lmsdesktopclient.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

/**
 * A model class representing a selected book with properties for its attributes.
 * This class implements the {@link Clearable} interface to support clearing its data.
 * <p>
 * Fields:
 * <ul>
 *     <li><b>coverImage</b>: The cover image of the book, stored as an {@link Image} object.</li>
 *     <li><b>isbn</b>: The International Standard Book Number (ISBN) of the book.</li>
 *     <li><b>title</b>: The title of the book.</li>
 *     <li><b>authors</b>: The author(s) of the book.</li>
 *     <li><b>genres</b>: The genre(s) of the book.</li>
 *     <li><b>status</b>: The status of the book.</li>
 * </ul>
 */
@Singleton
public class SelectedBookModel implements Clearable{

    private final ObjectProperty<Image> coverImage;
    private final StringProperty isbn;
    private final StringProperty title;
    private final StringProperty authors;
    private final StringProperty genres;
    private final StringProperty status;

    @Inject
    public SelectedBookModel(Image defaultImage) {
        coverImage = new SimpleObjectProperty<>();
        this.coverImage.set(defaultImage);
        isbn = new SimpleStringProperty();
        title = new SimpleStringProperty();
        authors = new SimpleStringProperty();
        genres = new SimpleStringProperty();
        status = new SimpleStringProperty();
    }

    public Image getCoverImage() {
        return coverImage.get();
    }

    public ObjectProperty<Image> coverImageProperty() {
        return coverImage;
    }

    public void setCoverImage(Image coverImage) {
        this.coverImage.set(coverImage);
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

    @Override
    public String toString() {
        return "SelectedBookModel{" +
                "coverImage=" + coverImage +
                ", isbn=" + isbn +
                ", title=" + title +
                ", authors=" + authors +
                ", genres=" + genres +
                ", status=" + status +
                '}';
    }

    @Override
    public void clear() {

    }
}
