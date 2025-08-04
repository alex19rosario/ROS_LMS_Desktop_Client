package com.ros.lmsdesktopclient.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;

/**
 * <p>
 * <strong>LoginModel</strong> represents the user's login credentials in the LMS desktop application.<br>
 * It uses JavaFX properties to enable data binding with the UI components.
 * </p>
 *
 * <h3>Fields:</h3>
 * <ul>
 *   <li><code>username</code>: A {@link javafx.beans.property.StringProperty} representing the user's login name.</li>
 *   <li><code>password</code>: A {@link javafx.beans.property.StringProperty} representing the user's password.</li>
 * </ul>
 *
 * <p>
 * This class is annotated with <code>@Singleton</code> and is designed for use with dependency injection.
 * </p>
 */

@Singleton
public class LoginModel implements Clearable, Completable {
    private final StringProperty username;
    private final StringProperty password;

    @Inject
    public LoginModel() {
        this.username = new SimpleStringProperty("");
        this.password = new SimpleStringProperty("");
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

    @Override
    public void clear() {
        this.setUsername("");
        this.setPassword("");
    }

    @Override
    public boolean isComplete() {
        return !this.getUsername().isEmpty() && !this.getPassword().isEmpty() && Arrays.stream(this.getUsername().split("")).noneMatch(c -> c.equalsIgnoreCase(" "));
    }
}
