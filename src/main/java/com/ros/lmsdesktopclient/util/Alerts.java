package com.ros.lmsdesktopclient.util;

import javafx.scene.control.Alert;

public enum Alerts {
    AUTHENTICATION_ERROR("Authentication Failed", "Bad Credentials", "Invalid username or password.", Alert.AlertType.ERROR),
    SERVER_ERROR("Server Error", "Server Error", "Internal error occurred. \nThe server may be off.", Alert.AlertType.ERROR),
    NETWORK_ERROR("Network Error", "Network Error", "Local network is not working.", Alert.AlertType.ERROR),
    EMPTY_FIELDS_WARN("Empty Fields", "Empty Fields", "Please fill out all required fields.", Alert.AlertType.WARNING),
    EXPIRED_SESSION_ERROR("Session Expired", "Session Timeout", "Your session has expired. Please log in again to continue.", Alert.AlertType.ERROR),
    EXISTING_BOOK_ERROR("Book Already Exists", "Book Already Exists", "A book with the specified ISBN already exists", Alert.AlertType.ERROR),
    INVALID_ISBN_ERROR("Invalid ISBN", "Invalid ISBN", "The provided ISBN format is incorrect.", Alert.AlertType.ERROR),
    BOOK_ADDED_SUCCESS("Success","Book Added Successfully" ,"The books was added successfully" , Alert.AlertType.INFORMATION),
    ACCESS_DENIED_ERROR("Access Denied","Access Denied", "The user does not have access to this resource", Alert.AlertType.ERROR),
    MEMBER_ADDED_SUCCESS("Success", "Member Added Successfully", "The member was added successfully", Alert.AlertType.INFORMATION),
    INVALID_ID_ERROR("Invalid ID", "Invalid Government ID", "The government ID is not valid", Alert.AlertType.ERROR),
    INVALID_EMAIL_ERROR("Invalid Email", "Invalid Email", "The email is not valid", Alert.AlertType.ERROR),
    INVALID_PASSWORD_ERROR("Invalid Password", "Invalid Password", "The password is not valid", Alert.AlertType.ERROR),
    UNMATCHED_PASSWORDS_ERROR("Passwords Do Not Match", "Passwords Do Not Match", "The passwords do not match", Alert.AlertType.ERROR),
    EXISTING_MEMBER_ERROR("Member Already Exists", "Member Already Exists", "A member with the specified government ID already exists", Alert.AlertType.ERROR),
    EXISTING_EMAIL_ERROR("Email Already Exists", "Email Already Exists", "A member with the specified email already exists", Alert.AlertType.ERROR),
    EXISTING_USERNAME_ERROR("Username Already Exists", "Username Already Exists", "A member with the specified username already exists", Alert.AlertType.ERROR),
    INVALID_PHONE_ERROR("Invalid Phone Number", "Invalid Phone Number", "The phone number is not valid", Alert.AlertType.ERROR);

    private final String title;
    private final String header;
    private final String content;
    private final Alert.AlertType type;

    Alerts(String title, String header, String content, Alert.AlertType type){
        this.title = title;
        this.header = header;
        this.content = content;
        this.type = type;
    }

    public void getModal(){
        Alert alert = new Alert(this.type);
        alert.setTitle(this.title);
        alert.setHeaderText(this.header);
        alert.setContentText(this.content);

        alert.showAndWait();
    }
}
