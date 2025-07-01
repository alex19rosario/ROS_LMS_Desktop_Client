package com.ros.lmsdesktopclient.models;
import com.ros.lmsdesktopclient.util.Genres;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.File;
import java.util.Objects;

public class BookModel implements Model {
    private final StringProperty isbn;
    private final StringProperty title;
    private final ListProperty<StringProperty> genres;
    private final ObjectProperty<Image> coverImage;
    private final ObjectProperty<File> coverImageFile;

    public  BookModel(){
        this.isbn = new SimpleStringProperty("");
        this.title = new SimpleStringProperty("");
        this.genres = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.genres.addFirst(new SimpleStringProperty(Genres.SCIENCE.getStr()));
        this.coverImage = new SimpleObjectProperty<>();
        Image defaultImage = new Image(Objects.requireNonNull(getClass().getResource("/images/upload_image.png")).toExternalForm());
        this.coverImage.set(defaultImage);
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

    public ListProperty<StringProperty> genresProperty() {
        return genres;
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
        this.setGenres(FXCollections.observableArrayList(new SimpleStringProperty(Genres.SCIENCE.getStr())));
        this.coverImage.set(null);
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
        if (this.genres.isEmpty() || this.genres.stream().anyMatch(genre -> genre.get().trim().isEmpty())) {
            return false;
        }
        // All checks passed
        return true;
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
