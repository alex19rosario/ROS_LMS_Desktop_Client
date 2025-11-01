package com.ros.lmsdesktopclient.views;


import com.ros.lmsdesktopclient.util.LoadingOverlay;
import com.ros.lmsdesktopclient.util.enums.Sex;
import com.ros.lmsdesktopclient.view_models.AddMemberViewModel;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.inject.Inject;
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
    private ProgressIndicator progressIndicator;

    @Inject
    public AddMemberView(AddMemberViewModel addMemberViewModel) {
        this.addMemberViewModel = addMemberViewModel;
    }

    @Override
    public void start(Stage stage) {
        initComponents();
        Scene scene = new Scene(createContent(), stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/styles/add-member-view.css").toExternalForm());
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
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);

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

        btnAddMember.setOnAction(_ -> addMemberViewModel.executeAddMemberCommand());
        btnGoBack.setOnAction(_ -> addMemberViewModel.executeOpenMainViewCommand());

        // Bind progress indicator visibility and progress
        progressIndicator.visibleProperty().bind(addMemberViewModel.getAddMemberCommand().runningProperty());
        progressIndicator.progressProperty().bind(addMemberViewModel.getAddMemberCommand().progressProperty());
    }

    private Region createContent() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createHeader());

        // Scrollable container for the form sections
        VBox formSections = new VBox(
                30,
                createSectionCard("Personal Information", createPersonalInfoGrid()),
                createSectionCard("Contact Information", createContactInfoGrid()),
                createSectionCard("Government Identification", createGovernmentIdGrid()),
                createSectionCard("Account Credentials", createAccountCredentialGrid())
        );
        formSections.setAlignment(Pos.CENTER);
        formSections.setMaxWidth(900); // max width to keep form from stretching too much
        formSections.getStyleClass().add("form-card");

        // Centering wrapper
        StackPane centeredWrapper = new StackPane(formSections);
        centeredWrapper.setPadding(new Insets(20)); // optional padding
        centeredWrapper.setAlignment(Pos.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane(centeredWrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        borderPane.setCenter(scrollPane);

        // Footer stays outside scroll pane
        borderPane.setBottom(createFooter());

        return LoadingOverlay.wrap(borderPane, progressIndicator);
    }

    private Node createHeader() {
        HBox hBox = new HBox(lblHeaderTitle);
        lblHeaderTitle.setId("lblHeaderTitle");
        hBox.setPadding(new Insets(25));
        hBox.setAlignment(Pos.CENTER);
        hBox.setId("header");
        return hBox;
    }

    private VBox createSectionCard(String title, Node content) {
        Label lblTitle = new Label(title);
        lblTitle.getStyleClass().add("section-title");

        VBox vBox = new VBox(15, lblTitle, content);
        vBox.setPadding(new Insets(20));
        vBox.getStyleClass().add("section-card");
        return vBox;
    }

    private GridPane createPersonalInfoGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.add(lblFirstName, 0, 0);
        grid.add(tfFirstName, 1, 0);
        grid.add(lblLastName, 2, 0);
        grid.add(tfLastName, 3, 0);
        grid.add(lblDateOfBirth, 0, 1);
        grid.add(dpDateOfBirth, 1, 1);
        grid.add(lblSex, 2, 1);
        grid.add(cbSex, 3, 1);
        return grid;
    }

    private GridPane createContactInfoGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.add(lblPhone, 0, 0);
        grid.add(tfPhone, 1, 0);
        grid.add(lblEmail, 0, 1);
        grid.add(tfEmail, 1, 1);
        return grid;
    }

    private GridPane createGovernmentIdGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.add(lblGovernmentId, 0, 0);
        grid.add(tfGovernmentId, 1, 0);
        return grid;
    }

    private GridPane createAccountCredentialGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.add(lblUsername, 0, 0);
        grid.add(tfUsername, 1, 0);
        grid.add(lblPassword, 0, 1);
        grid.add(tfPassword, 1, 1);
        grid.add(lblRepeatedPassword, 0, 2);
        grid.add(tfRepeatedPassword, 1, 2);
        return grid;
    }

    private Node createFooter() {
        HBox hBox = new HBox(400, btnGoBack, btnAddMember);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(25));
        return hBox;
    }
}
