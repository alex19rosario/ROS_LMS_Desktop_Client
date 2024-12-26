package com.ros.lmsdesktopclient.views;


import com.ros.lmsdesktopclient.models.LoginModel;
import com.ros.lmsdesktopclient.view_models.LoginViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    private LoginViewModel loginViewModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginViewModel = new LoginViewModel(new LoginModel());
        tfUsername.textProperty().bindBidirectional(loginViewModel.getLoginModel().usernameProperty());
        tfPassword.textProperty().bindBidirectional(loginViewModel.getLoginModel().passwordProperty());

        // Add key press event listener for both username and password fields
        tfUsername.setOnKeyPressed(this::handleKeyPressed);
        tfPassword.setOnKeyPressed(this::handleKeyPressed);
    }

    @FXML
    private void handleLogin() {
        this.loginViewModel.executeLoginCommand();
    }

    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin(); // Trigger login on Enter key press
        }
    }


}
