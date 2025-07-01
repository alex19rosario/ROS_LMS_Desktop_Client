package com.ros.lmsdesktopclient.views;


import com.ros.lmsdesktopclient.util.Sex;
import com.ros.lmsdesktopclient.view_models.AddMemberViewModel;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Arrays;

public class AddMemberView implements BaseView {

    private AddMemberViewModel addMemberViewModel;

    private Label lblHeaderTitle;
    private Label lblGovernmentId;
    private TextField tfGovernmentId;
    private Label lblFirstName;
    private TextField tfFirstName;
    private Label lblLastName;
    private TextField tfLastName;
    private Label lblPhone;
    private TextField tfPhone;
    private Label lblDateOfBirth;
    private DatePicker dpDateOfBirth;
    private Label lblSex;
    private ComboBox<String> cbSex;
    private Label lblEmail;
    private TextField tfEmail;
    private Label lblUsername;
    private TextField tfUsername;
    private Label lblPassword;
    private PasswordField tfPassword;
    private Label lblRepeatedPassword;
    private PasswordField tfRepeatedPassword;
    private Button btnGoBack;
    private Button btnAddMember;
    private Label lblPersonalSectionTitle;
    private Label lblContactInfoSectionTitle;
    private Label lblGovernmentIdSectionTitle;
    private Label lblAccountCredentialSectionTitle;



    @Override
    public void start(Stage stage) {
        addMemberViewModel = new AddMemberViewModel();
        initComponents();
        Scene scene = new Scene(createContent(), stage.getScene().getWidth(), stage.getScene().getHeight());
        bindComponents();
        stage.setScene(scene);
    }

    private void initComponents() {
        lblHeaderTitle = new Label("Adding a new member");
        lblGovernmentId = new Label("Government ID");
        tfGovernmentId = new TextField();
        lblFirstName = new Label("First Name");
        tfFirstName = new TextField();
        lblLastName = new Label("Last Name");
        tfLastName = new TextField();
        lblPhone = new Label("Phone");
        tfPhone = new TextField();
        lblDateOfBirth = new Label("Date of Birth");
        dpDateOfBirth = new DatePicker();
        lblSex = new Label("Sex");
        cbSex = new ComboBox<>();
        lblEmail = new Label("Email");
        tfEmail = new TextField();
        lblUsername = new Label("Username");
        tfUsername = new TextField();
        lblPassword = new Label("Password");
        tfPassword = new PasswordField();
        lblRepeatedPassword = new Label("Repeat Password");
        tfRepeatedPassword = new PasswordField();
        btnGoBack = new Button("Go Back");
        btnAddMember = new Button("Add Member");
        lblPersonalSectionTitle = new Label("Personal Information");
        lblContactInfoSectionTitle = new Label("Contact Information");
        lblGovernmentIdSectionTitle = new Label("Government Identification");
        lblAccountCredentialSectionTitle = new Label("Account Credentials");

        //This is to enter the values of the enum into the comboBox
        cbSex.setItems(FXCollections
                .observableList(
                        Arrays.stream(Sex.values())
                                .map(Sex::toString)
                                .toList()
                )
        );
    }

    private void bindComponents() {
        tfGovernmentId.textProperty().bindBidirectional(addMemberViewModel.getMemberModel().governmentIDProperty());
        tfFirstName.textProperty().bindBidirectional(addMemberViewModel.getMemberModel().firstNameProperty());
        tfLastName.textProperty().bindBidirectional(addMemberViewModel.getMemberModel().lastNameProperty());
        tfPhone.textProperty().bindBidirectional(addMemberViewModel.getMemberModel().phoneProperty());
        dpDateOfBirth.valueProperty().bindBidirectional(addMemberViewModel.getMemberModel().dateOfBirthProperty());
        cbSex.valueProperty().bindBidirectional(addMemberViewModel.getMemberModel().sexProperty());
        tfEmail.textProperty().bindBidirectional(addMemberViewModel.getMemberModel().emailProperty());
        tfUsername.textProperty().bindBidirectional(addMemberViewModel.getMemberModel().usernameProperty());
        tfPassword.textProperty().bindBidirectional(addMemberViewModel.getMemberModel().passwordProperty());
        tfRepeatedPassword.textProperty().bindBidirectional(addMemberViewModel.getMemberModel().repeatedPasswordProperty());

        btnAddMember.setOnAction(actionEvent -> addMemberViewModel.executeAddMemberCommand());
        btnGoBack.setOnAction(actionEvent -> addMemberViewModel.executeOpenMainViewCommand());
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
        VBox vBox = new VBox(
                25,
                createPersonalInfoSection(),
                createContactInfoSection(),
                createGovernmentIdSection(),
                createAccountCredentialSection(),
                createFooter());
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private Node createPersonalInfoSection() {
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox(lblPersonalSectionTitle);
        hBox.setPadding(new Insets(15));
        hBox.setAlignment(Pos.CENTER);
        borderPane.setTop(hBox);
        GridPane gridPane = new GridPane(25, 25);
        gridPane.add(lblFirstName, 1, 0);
        gridPane.add(tfFirstName, 2, 0);
        gridPane.add(lblLastName, 3, 0);
        gridPane.add(tfLastName, 4, 0);
        gridPane.add(lblDateOfBirth, 1, 1);
        gridPane.add(dpDateOfBirth, 2, 1);
        gridPane.add(lblSex, 3, 1);
        gridPane.add(cbSex, 4, 1);
        gridPane.setAlignment(Pos.CENTER);
        borderPane.setCenter(gridPane);
        return borderPane;
    }

    private Node createContactInfoSection() {
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox(lblContactInfoSectionTitle);
        hBox.setPadding(new Insets(15));
        hBox.setAlignment(Pos.CENTER);
        borderPane.setTop(hBox);
        GridPane gridPane = new GridPane(25, 25);
        gridPane.add(lblPhone, 1, 0);
        gridPane.add(tfPhone, 2, 0);
        gridPane.add(lblEmail, 1, 1);
        gridPane.add(tfEmail, 2, 1);
        gridPane.setAlignment(Pos.CENTER);
        borderPane.setCenter(gridPane);
        return borderPane;
    }

    private Node createGovernmentIdSection() {
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox(lblGovernmentIdSectionTitle);
        hBox.setPadding(new Insets(15));
        hBox.setAlignment(Pos.CENTER);
        borderPane.setTop(hBox);
        GridPane gridPane = new GridPane(25, 25);
        gridPane.add(lblGovernmentId, 1, 0);
        gridPane.add(tfGovernmentId, 2, 0);
        gridPane.setAlignment(Pos.CENTER);
        borderPane.setCenter(gridPane);
        return borderPane;
    }

    private Node createAccountCredentialSection() {
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox(lblAccountCredentialSectionTitle);
        hBox.setPadding(new Insets(15));
        hBox.setAlignment(Pos.CENTER);
        borderPane.setTop(hBox);
        GridPane gridPane = new GridPane(25, 25);
        gridPane.add(lblUsername, 1, 0);
        gridPane.add(tfUsername, 2, 0);
        gridPane.add(lblPassword, 1, 1);
        gridPane.add(tfPassword, 2, 1);
        gridPane.add(lblRepeatedPassword, 1, 2);
        gridPane.add(tfRepeatedPassword, 2, 2);
        gridPane.setAlignment(Pos.CENTER);
        borderPane.setCenter(gridPane);
        return borderPane;
    }

    private Node createFooter() {
        HBox hBox = new HBox(400, btnGoBack, btnAddMember);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(25));
        return hBox;
    }
}
