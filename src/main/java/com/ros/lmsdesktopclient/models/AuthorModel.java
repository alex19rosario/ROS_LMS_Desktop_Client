package com.ros.lmsdesktopclient.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AuthorModel implements Clearable, Completable {

    private final StringProperty firstName;
    private final StringProperty lastName;

    public AuthorModel(){
        this.firstName = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
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
        if(this.getFirstName().isEmpty() || this.getFirstName().isBlank())
            return false;
        return !this.getLastName().isEmpty() && !this.getLastName().isBlank();
    }

    @Override
    public String toString() {
        return "AuthorModel{" +
                "firstName=" + firstName.get() +
                ", lastName=" + lastName.get() +
                '}';
    }
}
