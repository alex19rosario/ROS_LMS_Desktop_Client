package com.ros.lmsdesktopclient.views;

import com.ros.lmsdesktopclient.util.Roles;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML private MenuButton menuBtnAccount;
    @FXML private Button btnManageAccounts;
    @FXML private Button btnAddStaff;

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
        menuBtnAccount.setText(TokenHandler.getInstance().getUsername());

        System.out.println(TokenHandler.getInstance().getAuthorities());
        //Render GUI according to role
        if(!TokenHandler.getInstance().getAuthorities().contains(Roles.ADMIN.str())){
            btnManageAccounts.setVisible(false);
            btnAddStaff.setVisible(false);
        }

        updateAccountItem.setOnAction(event -> {
            System.out.println("Updating account...");
        });

        logoutItem.setOnAction(event -> {
            System.out.println("Logging out...");
            //Delete token
            //move to log in screen
            TokenHandler.getInstance().clear();
            double width = ViewHandler.getInstance().getSceneWidth();
            double height = ViewHandler.getInstance().getSceneHeight();
            ViewHandler.switchTo(Views.LOGIN.getView(), width, height);
        });
    }
}
