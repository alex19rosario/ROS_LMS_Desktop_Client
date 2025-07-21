package com.ros.lmsdesktopclient.views;

import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.util.BookStatus;
import com.ros.lmsdesktopclient.util.UpFrontDataHandler;
import com.ros.lmsdesktopclient.view_models.IssueBookViewModel;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Arrays;

public class IssueBookView implements BaseView {

    private IssueBookViewModel issueBookViewModel;

    private Label lblHeaderTitle;
    private Label lblIsbn;
    private TextField tfIsbn;
    private Label lblTitle;
    private TextField tfTitle;
    private Label lblAuthorFirstName;
    private TextField tfAuthorFirstName;
    private Label lblAuthorLastName;
    private TextField tfAuthorLastName;
    private Label lblGenre;
    private ComboBox<String> cbGenre;
    private Label lblStatus;
    private ComboBox<String> cbStatus;
    private Button btnSearch;
    private Button btnClear;
    private TableView<BookDisplayModel> tableViewBook;
    private TableColumn<BookDisplayModel, String> columnIsbn;
    private TableColumn<BookDisplayModel, String> columnTitle;
    private TableColumn<BookDisplayModel, String> columnAuthor;
    private TableColumn<BookDisplayModel, String> columnGenre;
    private TableColumn<BookDisplayModel, String> columnStatus;
    private ImageView imageSelectedBookCover;
    private Label lblSelectedBookIsbn;
    private Label lblSelectedBookTitle;
    private Label lblSelectedBookAuthors;
    private Label lblSelectedBookGenres;
    private Label lblSelectedBookStatus;
    private Label lblMemberUsername;
    private TextField tfMemberUsername;
    private Button btnGoBack;
    private Button btnIssueBook;


    @Override
    public void start(Stage stage) {
        issueBookViewModel = new IssueBookViewModel();
        initComponents();
        Scene scene = new Scene(createContent(), stage.getScene().getWidth(), stage.getScene().getHeight());
        bindComponents();
        stage.setScene(scene);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        lblHeaderTitle = new Label("Issuing a book");
        lblIsbn = new Label("ISBN");
        tfIsbn = new TextField();
        lblTitle = new Label("Title");
        tfTitle = new TextField();
        lblAuthorFirstName = new Label("Author's First Name");
        tfAuthorFirstName = new TextField();
        lblAuthorLastName = new Label("Author's Last Name");
        tfAuthorLastName = new TextField();
        lblGenre = new Label("Genre");
        cbGenre = new ComboBox<>();
        lblStatus = new Label("Status");
        cbStatus = new ComboBox<>();
        btnSearch = new Button("Search");
        btnClear = new Button("Clear");
        tableViewBook = new TableView<>();
        columnIsbn = new TableColumn<>("ISBN");
        columnIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        columnTitle = new TableColumn<>("Title");
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnAuthor = new TableColumn<>("Authors");
        columnAuthor.setCellValueFactory(new PropertyValueFactory<>("authors"));
        columnGenre = new TableColumn<>("Genres");
        columnGenre.setCellValueFactory(new PropertyValueFactory<>("genres"));
        columnStatus = new TableColumn<>("Status");
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewBook.getColumns().addAll(columnIsbn, columnTitle, columnAuthor, columnGenre, columnStatus);
        imageSelectedBookCover = new ImageView();
        lblSelectedBookIsbn = new Label("");
        lblSelectedBookTitle = new Label("");
        lblSelectedBookAuthors = new Label("");
        lblSelectedBookGenres = new Label("");
        lblSelectedBookStatus = new Label("");
        lblMemberUsername = new Label("Member Username");
        tfMemberUsername = new TextField();
        btnGoBack = new Button("Go Back");
        btnIssueBook = new Button("Issue Book");

        // Loading data to comboBoxes
        cbGenre.setItems(FXCollections
                .observableList(UpFrontDataHandler.getInstance().getGenres()
                        .stream().toList()));

        cbStatus.setItems(FXCollections
                .observableList(
                        Arrays.stream(BookStatus.values())
                                .map(BookStatus::toString)
                                .toList()));
    }

    private void bindComponents() {
        // Filter Section
        tfIsbn.textProperty().bindBidirectional(issueBookViewModel.isbnProperty());
        tfTitle.textProperty().bindBidirectional(issueBookViewModel.getSearchBookModel().titleProperty());
        tfAuthorFirstName.textProperty().bindBidirectional(issueBookViewModel.getSearchBookModel().authorFirstNameProperty());
        tfAuthorLastName.textProperty().bindBidirectional(issueBookViewModel.getSearchBookModel().authorLastNameProperty());
        cbGenre.valueProperty().bindBidirectional(issueBookViewModel.getSearchBookModel().genreProperty());
        cbStatus.valueProperty().bindBidirectional(issueBookViewModel.getSearchBookModel().statusProperty());

        btnClear.setOnAction(actionEvent -> issueBookViewModel.executeClearFilterCommand());

        // Disable ISBN whenever ANY of the other fields is non‑empty --
        BooleanBinding disableIsbn =
                tfTitle.textProperty().isNotEmpty()
                        .or(tfAuthorFirstName.textProperty().isNotEmpty())
                        .or(tfAuthorLastName.textProperty().isNotEmpty())
                        .or(cbGenre.valueProperty().isNotNull())
                        .or(cbStatus.valueProperty().isNotNull());

        tfIsbn.disableProperty().bind(disableIsbn);

        // Disable those fields whenever ISBN is non‑empty --
        BooleanBinding disableOthers = tfIsbn.textProperty().isNotEmpty();
        tfTitle.disableProperty().bind(disableOthers);
        tfAuthorFirstName.disableProperty().bind(disableOthers);
        tfAuthorLastName.disableProperty().bind(disableOthers);
        cbGenre.disableProperty().bind(disableOthers);
        cbStatus.disableProperty().bind(disableOthers);

        

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
                createFilterSection(),
                createTableSection(),
                createDetailSection(),
                createFooter()
        );
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private Node createFilterSection() {
        GridPane gridPane = new GridPane(25, 25);
        gridPane.add(lblIsbn, 1, 0);
        gridPane.add(tfIsbn, 2, 0);
        gridPane.add(lblTitle, 1, 1);
        gridPane.add(tfTitle, 2, 1);
        gridPane.add(lblAuthorFirstName, 4, 0);
        gridPane.add(tfAuthorFirstName, 5, 0);
        gridPane.add(lblAuthorLastName, 4, 1);
        gridPane.add(tfAuthorLastName, 5, 1);
        gridPane.add(lblGenre, 7, 0);
        gridPane.add(cbGenre, 8, 0);
        gridPane.add(lblStatus, 7, 1);
        gridPane.add(cbStatus, 8, 1);
        gridPane.add(btnSearch, 10, 0);
        gridPane.add(btnClear, 10, 1);
        return gridPane;
    }

    private Node createTableSection() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(25));
        borderPane.setCenter(tableViewBook);
        return borderPane;
    }

    private Node createDetailSection() {
        return new HBox(50, createSelectedBookSection(), createMemberSection());
    }

    private Node createSelectedBookSection() {
        VBox vBox = new VBox(
                15,
                lblSelectedBookIsbn,
                lblSelectedBookTitle,
                lblSelectedBookAuthors,
                lblSelectedBookGenres,
                lblSelectedBookStatus
        );
        return new HBox(25, imageSelectedBookCover, vBox);
    }

    private Node createMemberSection() {
        return new HBox(
                25,
                lblMemberUsername,
                tfMemberUsername
        );
    }

    private Node createFooter() {
        HBox hBox = new HBox(400, btnGoBack, btnIssueBook);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(25));
        return hBox;
    }


}
