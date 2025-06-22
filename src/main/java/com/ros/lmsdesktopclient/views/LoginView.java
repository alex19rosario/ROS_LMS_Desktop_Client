package com.ros.lmsdesktopclient.views;

import com.ros.lmsdesktopclient.models.LoginModel;
import com.ros.lmsdesktopclient.view_models.LoginViewModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;



public class LoginView implements BaseView {

    private LoginViewModel loginViewModel;

    private Label lblHeaderTitle;
    private Label lblUsername;
    private TextField tfUsername;
    private Label lblPassword;
    private PasswordField tfPassword;
    private Button btnLogin;

    @Override
    public void start(Stage stage) {
        loginViewModel = new LoginViewModel(new LoginModel());
        initComponents();
        Scene scene = stage.getScene() == null ?
                new Scene(createContent(), 800, 600) :
                new Scene(createContent(), stage.getScene().getWidth(), stage.getScene().getHeight());
        bindComponents();
        stage.setScene(scene);
        stage.show();
    }

    private void initComponents() {
        lblHeaderTitle = new Label("Log in");
        lblUsername = new Label("Username");
        tfUsername = new TextField();
        lblPassword = new Label("Password");
        tfPassword = new PasswordField();
        btnLogin = new Button("Log in");
    }

    private void bindComponents() {
        tfUsername.textProperty().bindBidirectional(loginViewModel.getLoginModel().usernameProperty());
        tfPassword.textProperty().bindBidirectional(loginViewModel.getLoginModel().passwordProperty());

        btnLogin.setOnAction(actionEvent -> loginViewModel.executeLoginCommand());
    }

    private Region createContent() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createHeader());
        borderPane.setCenter(createForm());
        return borderPane;
    }

    private Node createHeader() {
        HBox hBox = new HBox(lblHeaderTitle);
        hBox.setPadding(new Insets(25));
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private Node createForm() {
        VBox vBox = new VBox(25, createSection(), createButtonSection());
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private Node createSection() {
        GridPane gridPane = new GridPane(25, 25);
        gridPane.add(lblUsername, 1, 0);
        gridPane.add(tfUsername, 2, 0);
        gridPane.add(lblPassword, 1, 1);
        gridPane.add(tfPassword, 2, 1);
        gridPane.setAlignment(Pos.TOP_CENTER);
        return gridPane;
    }

    private Node createButtonSection() {
        HBox hBox = new HBox(btnLogin);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }
}