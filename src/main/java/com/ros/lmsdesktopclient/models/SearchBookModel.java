package com.ros.lmsdesktopclient.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SearchBookModel implements Clearable{

    private final IntegerProperty page;
    private final IntegerProperty size;
    private final StringProperty title;
    private final StringProperty authorFirstName;
    private final StringProperty authorLastName;
    private final StringProperty genre;
    private final StringProperty status;

    public SearchBookModel() {
        this.page = new SimpleIntegerProperty();
        this.size = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.authorFirstName = new SimpleStringProperty();
        this.authorLastName = new SimpleStringProperty();
        this.genre = new SimpleStringProperty();
        this.status = new SimpleStringProperty();
    }

    public int getPage() {
        return page.get();
    }

    public IntegerProperty pageProperty() {
        return page;
    }

    public void setPage(int page) {
        this.page.set(page);
    }

    public int getSize() {
        return size.get();
    }

    public IntegerProperty sizeProperty() {
        return size;
    }

    public void setSize(int size) {
        this.size.set(size);
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

    public String getAuthorFirstName() {
        return authorFirstName.get();
    }

    public StringProperty authorFirstNameProperty() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName.set(authorFirstName);
    }

    public String getAuthorLastName() {
        return authorLastName.get();
    }

    public StringProperty authorLastNameProperty() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName.set(authorLastName);
    }

    public String getGenre() {
        return genre.get();
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
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
        return "SearchBookModel{" +
                "page=" + page +
                ", size=" + size +
                ", title=" + title +
                ", authorFirstName=" + authorFirstName +
                ", authorLastName=" + authorLastName +
                ", genre=" + genre +
                ", status=" + status +
                '}';
    }

    @Override
    public void clear() {
        title.setValue("");
        authorFirstName.setValue("");
        authorLastName.setValue("");
        genre.setValue(null);
        status.setValue(null);
    }
}
