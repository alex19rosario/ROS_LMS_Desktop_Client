package com.ros.lmsdesktopclient.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.inject.Inject;

public class AuthorModel implements Clearable, Completable {
    private final StringProperty firstName;
    private final StringProperty lastName;

    @Inject
    public  AuthorModel() {
        firstName = new SimpleStringProperty();
        lastName = new SimpleStringProperty();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    @Override
    public void clear() {
        this.setFirstName("");
        this.setLastName("");
    }

    @Override
    public boolean isComplete() {
        return firstName.isNotEmpty().get() && firstName.isNotNull().get()
                && lastName.isNotEmpty().get() && lastName.isNotNull().get();
    }
}
