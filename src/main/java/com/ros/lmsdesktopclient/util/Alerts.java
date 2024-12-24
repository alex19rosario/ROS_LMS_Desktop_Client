package com.ros.lmsdesktopclient.util;

import javafx.scene.control.Alert;

public enum Alerts {
    AUTHENTICATION_ERROR("Authentication Failed", "Bad Credentials", "Invalid username or password.", Alert.AlertType.ERROR),
    SERVER_ERROR("Server Error", "Server Error", "Internal error occurred. \nThe server may be off.", Alert.AlertType.ERROR),
    NETWORK_ERROR("Network Error", "Network Error", "Local network is not working.", Alert.AlertType.ERROR),
    EMPTY_FIELDS_WARN("Empty Fields", "Empty Fields", "Please fill out all required fields.", Alert.AlertType.WARNING);

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
