package com.ros.lmsdesktopclient.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDate;

/**
 * Represents a member's data in the LMS desktop application.
 * <p>
 * Fields:
 * <ul>
 *   <li><b>governmentID</b> - The member's government-issued identification number.</li>
 *   <li><b>firstName</b> - The member's first name.</li>
 *   <li><b>lastName</b> - The member's last name.</li>
 *   <li><b>phone</b> - The member's phone number.</li>
 *   <li><b>dateOfBirth</b> - The member's date of birth.</li>
 *   <li><b>sex</b> - The member's gender/sex.</li>
 *   <li><b>email</b> - The member's email address.</li>
 *   <li><b>username</b> - The username for member login.</li>
 *   <li><b>password</b> - The member's password.</li>
 *   <li><b>repeatedPassword</b> - The repeated password for confirmation.</li>
 * </ul>
 */
@Singleton
public class MemberModel implements Clearable, Completable {
    private final StringProperty governmentID;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty phone;
    private final ObjectProperty<LocalDate> dateOfBirth;
    private final StringProperty sex;
    private final StringProperty email;
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty repeatedPassword;

    @Inject
    public MemberModel(){
        governmentID = new SimpleStringProperty("");
        firstName = new SimpleStringProperty("");
        lastName = new SimpleStringProperty("");
        phone = new SimpleStringProperty("");
        dateOfBirth = new SimpleObjectProperty<>(null);
        sex = new SimpleStringProperty("");
        email = new SimpleStringProperty("");
        username = new SimpleStringProperty("");
        password = new SimpleStringProperty("");
        repeatedPassword = new SimpleStringProperty("");
    }

    public String getGovernmentID() {
        return governmentID.get();
    }

    public StringProperty governmentIDProperty() {
        return governmentID;
    }

    public void setGovernmentID(String governmentID) {
        this.governmentID.set(governmentID);
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

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth.get();
    }

    public ObjectProperty<LocalDate> dateOfBirthProperty() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public String getSex() {
        return sex.get();
    }

    public StringProperty sexProperty() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex.set(sex);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getRepeatedPassword() {
        return repeatedPassword.get();
    }

    public StringProperty repeatedPasswordProperty() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword.set(repeatedPassword);
    }

    @Override
    public void clear() {
        this.setGovernmentID("");
        this.setFirstName("");
        this.setLastName("");
        this.setPhone("");
        this.setDateOfBirth(null);
        this.setSex("");
        this.setEmail("");
        this.setUsername("");
        this.setPassword("");
        this.setRepeatedPassword("");
    }

    @Override
    public boolean isComplete() {

        return !this.getGovernmentID().isEmpty() && !this.getGovernmentID().contains(" ") &&
                !this.getFirstName().isEmpty() &&
                !this.getLastName().isEmpty() &&
                !this.getPhone().isEmpty() && !this.getPhone().contains(" ") &&
                this.getDateOfBirth() != null &&
                !this.getSex().isEmpty() && !this.getSex().contains(" ") &&
                !this.getEmail().isEmpty() && !this.getEmail().contains(" ") &&
                !this.getUsername().isEmpty() && !this.getUsername().contains(" ") &&
                !this.getPassword().isEmpty() && !this.getPassword().contains(" ") &&
                !this.getRepeatedPassword().isEmpty() && !this.getRepeatedPassword().contains(" ");
    }
}
