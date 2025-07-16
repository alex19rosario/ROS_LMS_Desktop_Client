package com.ros.lmsdesktopclient.util;

import javafx.scene.control.Alert;

public enum Alerts {
    AUTHENTICATION_ERROR("Authentication Failed", "Bad Credentials", Alert.AlertType.ERROR),
    SERVER_ERROR("Server Error", "Server Error", Alert.AlertType.ERROR),
    NETWORK_ERROR("Network Error", "Network Error", Alert.AlertType.ERROR),
    EMPTY_FIELDS_WARN("Empty Fields", "Empty Fields", Alert.AlertType.WARNING),
    EXPIRED_SESSION_ERROR("Session Expired", "Session Timeout", Alert.AlertType.ERROR),
    EXISTING_BOOK_ERROR("Book Already Exists", "Book Already Exists", Alert.AlertType.ERROR),
    INVALID_ISBN_ERROR("Invalid ISBN", "Invalid ISBN", Alert.AlertType.ERROR),
    BOOK_ADDED_SUCCESS("Success","Book Added Successfully", Alert.AlertType.INFORMATION),
    ACCESS_DENIED_ERROR("Access Denied","Access Denied", Alert.AlertType.ERROR),
    MEMBER_ADDED_SUCCESS("Success", "Member Added Successfully", Alert.AlertType.INFORMATION),
    INVALID_ID_ERROR("Invalid ID", "Invalid Government ID", Alert.AlertType.ERROR),
    INVALID_EMAIL_ERROR("Invalid Email", "Invalid Email", Alert.AlertType.ERROR),
    INVALID_PASSWORD_ERROR("Invalid Password", "Invalid Password",  Alert.AlertType.ERROR),
    UNMATCHED_PASSWORDS_ERROR("Passwords Do Not Match", "Passwords Do Not Match", Alert.AlertType.ERROR),
    EXISTING_MEMBER_ERROR("Member Already Exists", "Member Already Exists", Alert.AlertType.ERROR),
    EXISTING_EMAIL_ERROR("Email Already Exists", "Email Already Exists", Alert.AlertType.ERROR),
    EXISTING_USERNAME_ERROR("Username Already Exists", "Username Already Exists", Alert.AlertType.ERROR),
    INVALID_PHONE_ERROR("Invalid Phone Number", "Invalid Phone Number", Alert.AlertType.ERROR),
    INVALID_DATE_OF_BIRTH("Invalid Date of Birth", "Invalid Date of Birth", Alert.AlertType.ERROR);

    private final String title;
    private final String header;
    private final Alert.AlertType type;

    Alerts(String title, String header, Alert.AlertType type){
        this.title = title;
        this.header = header;
        this.type = type;
    }

    public void getModal(String content) {
        Alert alert = new Alert(this.type);
        alert.setTitle(this.title);
        alert.setHeaderText(this.header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
