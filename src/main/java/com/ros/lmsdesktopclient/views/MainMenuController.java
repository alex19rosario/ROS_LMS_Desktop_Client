package com.ros.lmsdesktopclient.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML private MenuButton menuBtnAccount;

    @FXML
    private void addBook(ActionEvent actionEvent) {
    }
    @FXML
    private void addMember(ActionEvent actionEvent) {
    }
    @FXML
    private void issueBook(ActionEvent actionEvent) {
    }
    @FXML
    private void returnBook(ActionEvent actionEvent) {
    }
    @FXML
    private void manageBooks(ActionEvent actionEvent) {
    }
    @FXML
    private void monitor(ActionEvent actionEvent) {
    }
    @FXML
    private void manageAccounts(ActionEvent actionEvent) {
    }
    @FXML
    private void addStaff(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MenuItem updateAccountItem = new MenuItem("Update Account Information");
        MenuItem logoutItem = new MenuItem("Log Out");
        menuBtnAccount.getItems().addAll(updateAccountItem, logoutItem);

        updateAccountItem.setOnAction(event -> {
            System.out.println("Updating account...");
        });

        logoutItem.setOnAction(event -> {
            System.out.println("Logging out...");
        });
    }
}
