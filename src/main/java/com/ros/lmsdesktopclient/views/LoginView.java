package com.ros.lmsdesktopclient.views;

import com.ros.lmsdesktopclient.util.LoadingOverlay;
import com.ros.lmsdesktopclient.view_models.LoginViewModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.inject.Inject;


public class LoginView implements BaseView {

    private final LoginViewModel loginViewModel;

    private Label lblHeaderTitle;
    private Label lblUsername;
    private TextField tfUsername;
    private Label lblPassword;
    private PasswordField tfPassword;
    private Button btnLogin;
    private ProgressIndicator progressIndicator;

    @Inject
    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void start(Stage stage) {
        initComponents();
        Scene scene = stage.getScene() == null ?
                new Scene(createContent(), 1200, 900) :
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
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
    }

    private void bindComponents() {
        tfUsername.textProperty().bindBidirectional(loginViewModel.getLoginModel().usernameProperty());
        tfPassword.textProperty().bindBidirectional(loginViewModel.getLoginModel().passwordProperty());

        btnLogin.setOnAction(actionEvent -> loginViewModel.executeLoginCommand());

        // Bind progress indicator visibility and progress
        progressIndicator.visibleProperty().bind(loginViewModel.getLoginCommand().runningProperty());
        progressIndicator.progressProperty().bind(loginViewModel.getLoginCommand().progressProperty());
    }

    private Region createContent() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createHeader());
        borderPane.setCenter(createForm());
        // Wrap content in overlay
        return LoadingOverlay.wrap(borderPane, progressIndicator);
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