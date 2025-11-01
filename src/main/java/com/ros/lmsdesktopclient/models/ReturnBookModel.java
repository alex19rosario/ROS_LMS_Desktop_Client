package com.ros.lmsdesktopclient.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ReturnBookModel implements Clearable, Completable {

    private final StringProperty isbn;

    @Inject
    public ReturnBookModel() {
        isbn = new SimpleStringProperty("");
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

    @Override
    public void clear() {
        this.setIsbn("");
    }

    @Override
    public boolean isComplete() {
        return !this.getIsbn().isEmpty();
    }
}
