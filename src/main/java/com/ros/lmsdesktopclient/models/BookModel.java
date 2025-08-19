package com.ros.lmsdesktopclient.models;
import com.ros.lmsdesktopclient.util.enums.GenreType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;

/**
 * Holds JavaFX properties representing a book within the LMS desktop application.
 *
 * <p>This model is used in JavaFX desktop applications to manage book-related data
 * such as ISBN, title, genres, and cover image information.</p>
 *
 * Fields:
 * <ul>
 *   <li><b>isbn</b>: A JavaFX StringProperty representing the book's ISBN number.</li>
 *   <li><b>title</b>: A JavaFX StringProperty representing the book's title.</li>
 *   <li><b>genres</b>: A JavaFX ListProperty containing observable StringProperties for each genre associated with the book.</li>
 *   <li><b>coverImage</b>: A JavaFX ObjectProperty holding the JavaFX Image used as the book's cover.</li>
 *   <li><b>coverImageFile</b>: A JavaFX ObjectProperty referring to the File used for the uploaded book cover image.</li>
 * </ul>
 */
@Singleton
public class BookModel implements Clearable, Completable{
    private final StringProperty isbn;
    private final StringProperty title;
    private final ListProperty<StringProperty> genres;
    private final ObjectProperty<Image> coverImage;
    private final ObjectProperty<File> coverImageFile;
    private final Image defaultCover;

    @Inject
    public  BookModel(Image defaultCover){
        this.isbn = new SimpleStringProperty("");
        this.title = new SimpleStringProperty("");
        this.genres = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.genres.addFirst(new SimpleStringProperty(GenreType.SCIENCE.getStr()));
        this.coverImage = new SimpleObjectProperty<>();
        this.defaultCover = defaultCover;
        this.coverImage.set(defaultCover);
        this.coverImageFile = new SimpleObjectProperty<>();
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

    public void setGenres(ObservableList<StringProperty> genres) {
        this.genres.set(genres);
    }

    public Image getCoverImage() {
        return coverImage.get();
    }

    public ObjectProperty<Image> coverImageProperty() {
        return coverImage;
    }

    public void setCoverImage(Image coverImage){
        this.coverImage.set(coverImage);
    }

    public File getCoverImageFile() {
        return coverImageFile.get();
    }

    public ObjectProperty<File> coverImageFileProperty() {
        return coverImageFile;
    }

    public void setCoverImageFile(File coverImageFile){this.coverImageFile.set(coverImageFile);}

    @Override
    public void clear() {
        this.setIsbn("");
        this.setTitle("");
        this.getGenres().clear();
        this.setGenres(FXCollections.observableArrayList(new SimpleStringProperty(GenreType.SCIENCE.getStr())));
        this.coverImage.set(defaultCover);
        this.coverImageFile.set(null);
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
        return !this.genres.isEmpty() && this.genres.stream().noneMatch(genre -> genre.get().trim().isEmpty());
        // All checks passed
    }

    @Override
    public String toString() {
        return "BookModel{" +
                "isbn=" + isbn +
                ", title=" + title +
                ", genres=" + genres +
                ", coverImage=" + coverImage +
                ", coverImageFile=" + coverImageFile +
                '}';
    }
}
