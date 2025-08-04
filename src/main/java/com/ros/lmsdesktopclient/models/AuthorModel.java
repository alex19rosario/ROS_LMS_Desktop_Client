package com.ros.lmsdesktopclient.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.inject.Inject;


/**
 * Represents an author with observable first and last name properties.
 *
 * <p>This model is designed for use in JavaFX applications where UI binding is required.
 * It implements {@link Clearable} and {@link Completable} to support form resetting and completeness checks.</p>
 *
 * Fields:
 * <ul>
 *   <li><b>firstName</b>: Observable property representing the author's first name.</li>
 *   <li><b>lastName</b>: Observable property representing the author's last name.</li>
 * </ul>
 */
public class AuthorModel implements Clearable, Completable {

    private final StringProperty firstName;
    private final StringProperty lastName;

    @Inject
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
