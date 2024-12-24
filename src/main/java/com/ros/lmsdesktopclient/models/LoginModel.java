package com.ros.lmsdesktopclient.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Arrays;

public class LoginModel implements Model {
    private StringProperty username;
    private StringProperty password;

    public LoginModel() {
        this.username = new SimpleStringProperty("");
        this.password = new SimpleStringProperty("");
    }

    public LoginModel(String username, String password){
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
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
