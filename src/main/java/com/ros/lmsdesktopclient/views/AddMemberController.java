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
    @FXML private ComboBox<String> cbSex;
    @FXML private TextField tfEmail;
    @FXML private TextField tfUsername;
    @FXML private PasswordField tfPassword;
    @FXML private PasswordField tfRepeatedPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addMemberViewModel = new AddMemberViewModel();
        cbSex.setItems(FXCollections.observableList(Arrays.stream(Sex.values()).map(Sex::toString).toList()));
        tfGovernmentID.textProperty().bindBidirectional(addMemberViewModel.getMemberModel().governmentIDProperty());
        tfFirstName.textProperty().bindBidirectional(addMemberViewModel.getMemberModel().firstNameProperty());
        tfLastName.textProperty().bindBidirectional(addMemberViewModel.getMemberModel().lastNameProperty());
        tfPhone.textProperty().bindBidirectional(addMemberViewModel.getMemberModel().phoneProperty());
        tfAge.textProperty().bindBidirectional(addMemberViewModel.getMemberModel().ageProperty());
        cbSex.valueProperty().bindBidirectional(addMemberViewModel.getMemberModel().sexProperty());
        tfEmail.textProperty().bindBidirectional(addMemberViewModel.getMemberModel().emailProperty());
        tfUsername.textProperty().bindBidirectional(addMemberViewModel.getMemberModel().usernameProperty());
        tfPassword.textProperty().bindBidirectional(addMemberViewModel.getMemberModel().passwordProperty());
        tfRepeatedPassword.textProperty().bindBidirectional(addMemberViewModel.getMemberModel().repeatedPasswordProperty());
    }

    @FXML
    private void goBack(){
        addMemberViewModel.executeOpenMainViewCommand();
    }

    @FXML
    private void addMember(){
        addMemberViewModel.executeAddMemberCommand();
    }
}
