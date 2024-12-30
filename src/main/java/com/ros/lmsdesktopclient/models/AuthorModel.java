package com.ros.lmsdesktopclient.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Arrays;

public class AuthorModel implements Model {

    private StringProperty firstName;
    private StringProperty lastName;

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
        return !this.getFirstName().isEmpty() &&
                !this.getLastName().isEmpty() &&
                Arrays.stream(this.getFirstName().split(""))
                        .noneMatch(c -> c.equalsIgnoreCase(" ")) &&
                Arrays.stream(this.getLastName().split(""))
                        .noneMatch(c -> c.equalsIgnoreCase(" "));
    }

    @Override
    public String toString() {
        return "AuthorModel{" +
                "firstName=" + firstName.get() +
                ", lastName=" + lastName.get() +
                '}';
    }
}
