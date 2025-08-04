package com.ros.lmsdesktopclient.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Model class used for searching books with pagination and filter criteria in the LMS desktop application.
 * <p>
 * Fields:
 * <ul>
 *   <li><b>page</b> - The current page number for pagination.</li>
 *   <li><b>size</b> - The number of results per page (default 10).</li>
 *   <li><b>title</b> - The book title filter.</li>
 *   <li><b>authorFirstName</b> - The first name of the author filter.</li>
 *   <li><b>authorLastName</b> - The last name of the author filter.</li>
 *   <li><b>genre</b> - The genre filter.</li>
 *   <li><b>status</b> - The book status filter (e.g., available, loaned).</li>
 * </ul>
 */
@Singleton
public class SearchBookModel implements Clearable, Completable{

    private final IntegerProperty page;
    private final IntegerProperty size;
    private final StringProperty title;
    private final StringProperty authorFirstName;
    private final StringProperty authorLastName;
    private final StringProperty genre;
    private final StringProperty status;
    private final static int PAGE_SIZE = 10;

    @Inject
    public SearchBookModel() {
        this.page = new SimpleIntegerProperty();
        this.size = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.authorFirstName = new SimpleStringProperty();
        this.authorLastName = new SimpleStringProperty();
        this.genre = new SimpleStringProperty();
        this.status = new SimpleStringProperty();

        this.size.set(PAGE_SIZE);
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

    @Override
    public boolean isComplete() {
        return isNotEmpty(title.get()) ||
                isNotEmpty(authorFirstName.get()) ||
                isNotEmpty(authorLastName.get()) ||
                isNotEmpty(genre.get()) ||
                isNotEmpty(status.get());
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
