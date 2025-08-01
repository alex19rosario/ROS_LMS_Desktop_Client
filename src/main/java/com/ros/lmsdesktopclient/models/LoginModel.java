package com.ros.lmsdesktopclient.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;

@Singleton
public class LoginModel implements Clearable, Completable {
    private StringProperty username;
    private StringProperty password;

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
        this.setPassword("");
    }

    @Override
    public boolean isComplete() {
        return !this.getUsername().isEmpty() && !this.getPassword().isEmpty() && Arrays.stream(this.getUsername().split("")).noneMatch(c -> c.equalsIgnoreCase(" "));
    }
}
