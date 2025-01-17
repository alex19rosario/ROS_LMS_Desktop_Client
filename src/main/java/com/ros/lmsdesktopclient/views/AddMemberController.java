package com.ros.lmsdesktopclient.views;

import com.ros.lmsdesktopclient.util.Sex;
import com.ros.lmsdesktopclient.view_models.AddMemberViewModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AddMemberController implements Initializable {

    private AddMemberViewModel addMemberViewModel;
    @FXML private TextField tfGovernmentID;
    @FXML private TextField tfFirstName;
    @FXML private TextField tfLastName;
    @FXML private TextField tfPhone;
    @FXML private TextField tfAge;
    @FXML private ComboBox<Sex> cbSex;
    @FXML private TextField tfEmail;
    @FXML private TextField tfUsername;
    @FXML private PasswordField tfPassword;
    @FXML private PasswordField tfRepeatedPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addMemberViewModel = new AddMemberViewModel();


    }

    @FXML
    private void goBack(){
        addMemberViewModel.executeOpenMainViewCommand();
    }

    @FXML
    private void addMember(){
        System.out.println("Adding member");
    }
}
