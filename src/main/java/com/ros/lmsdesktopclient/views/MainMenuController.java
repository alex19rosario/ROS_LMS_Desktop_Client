package com.ros.lmsdesktopclient.views;

import com.ros.lmsdesktopclient.util.Roles;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.view_models.MainMenuViewModel;
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
    private MainMenuViewModel mainMenuViewModel;

    @FXML
    private void addBook(ActionEvent actionEvent) {
        mainMenuViewModel.executeOpenAddBookViewCommand();
    }
    @FXML
    private void addMember(ActionEvent actionEvent) {
        mainMenuViewModel.executeOpenAddMemberViewCommand();
    }
    @FXML
    private void issueBook(ActionEvent actionEvent) {
        mainMenuViewModel.executeOpenIssueBookViewCommand();
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
        mainMenuViewModel = new MainMenuViewModel();
        MenuItem updateAccountItem = new MenuItem("Update Account Information");
        MenuItem logoutItem = new MenuItem("Log Out");
        menuBtnAccount.getItems().addAll(updateAccountItem, logoutItem);
        menuBtnAccount.setText(TokenHandler.getInstance().getUsername());

        //Render GUI according to role
        if(!TokenHandler.getInstance().getAuthorities().contains(Roles.ADMIN.str())){
            btnManageAccounts.setVisible(false);
            btnAddStaff.setVisible(false);
        }

        updateAccountItem.setOnAction(event -> {
            System.out.println("Updating account...");
        });

        logoutItem.setOnAction(event -> {
            mainMenuViewModel.executeLogOutCommand();
        });
    }
}
