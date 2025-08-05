package com.ros.lmsdesktopclient.views;

import com.ros.lmsdesktopclient.models.AuthorInputModel;
import com.ros.lmsdesktopclient.models.GenreInputModel;
import com.ros.lmsdesktopclient.util.LoadingOverlay;
import com.ros.lmsdesktopclient.view_models.AddBookViewModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.inject.Inject;


public class AddBookView implements BaseView {

    private AddBookViewModel addBookViewModel;

    private Label lblHeaderTitle;
    private Label lblIsbn;
    private TextField tfIsbn;
    private Label lblTitle;
    private TextField tfTitle;
    private TableView<AuthorInputModel> tableViewAuthor;
    private TableColumn<AuthorInputModel, TextField> columnFirstName;
    private TableColumn<AuthorInputModel, TextField> columnLastName;
    private Button btnAddAuthor;
    private Label lblCoverImage;
    private ImageView coverImageView;
    private TableView<GenreInputModel> tableViewGenre;
    private TableColumn<GenreInputModel, ComboBox<String>> columnGenre;
    private Button btnAddGenre;
    private Button btnAddBook;
    private Button btnGoBack;
    private Button btnAttachCoverImage;
    private ProgressIndicator progressIndicator;

    @Inject
    public AddBookView(AddBookViewModel addBookViewModel) {
        this.addBookViewModel = addBookViewModel;
    }

    @Override
    public void start(Stage stage) {
        initComponents();
        Scene scene = new Scene(createContent(), stage.getScene().getWidth(), stage.getScene().getHeight());
        bindComponents();
        stage.setScene(scene);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        lblHeaderTitle = new Label("Adding a new book");
        lblIsbn = new Label("ISBN");
        tfIsbn = new TextField();
        lblTitle = new Label("Title");
        tfTitle = new TextField();
        tableViewAuthor = new TableView<>();
        columnFirstName = new TableColumn<>("First Name");
        columnLastName = new TableColumn<>("Last Name");
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("tfFirstName"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("tfLastName"));
        tableViewAuthor.getColumns().addAll(columnFirstName, columnLastName);
        btnAddAuthor = new Button("+");
        lblCoverImage = new Label("Cover Image");
        coverImageView = new ImageView();
        tableViewGenre = new TableView<>();
        columnGenre = new TableColumn<>("Genres");
        columnGenre.setCellValueFactory(new PropertyValueFactory<>("cbGenres"));
        tableViewGenre.getColumns().add(columnGenre);
        btnAddGenre = new Button("+");
        btnAddBook = new Button("Add Book");
        btnGoBack = new Button("Go Back");
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
    }

    private void bindComponents() {
        tfIsbn.textProperty().bindBidirectional(addBookViewModel.getBookModel().isbnProperty());
        tfTitle.textProperty().bindBidirectional(addBookViewModel.getBookModel().titleProperty());
        tableViewAuthor.itemsProperty().bindBidirectional(addBookViewModel.authorInputsProperty());
        coverImageView.imageProperty().bindBidirectional(addBookViewModel.getBookModel().coverImageProperty());
        tableViewGenre.itemsProperty().bindBidirectional(addBookViewModel.genreInputsProperty());

        btnAddAuthor.setOnAction(actionEvent -> addBookViewModel.executeAddAuthorCommand());
        btnAddGenre.setOnAction(actionEvent -> addBookViewModel.executeAddGenreCommand());
        btnAddBook.setOnAction(actionEvent -> addBookViewModel.executeAddBookCommand());
        btnAttachCoverImage.setOnAction(actionEvent -> addBookViewModel.executeSelectCoverImageCommand());
        btnGoBack.setOnAction(actionEvent -> addBookViewModel.executeOpenMainViewCommand());

        // Bind progress indicator visibility and progress
        progressIndicator.visibleProperty().bind(addBookViewModel.getAddBookCommand().runningProperty());
        progressIndicator.progressProperty().bind(addBookViewModel.getAddBookCommand().progressProperty());
    }

    private Region createContent() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createHeader());
        borderPane.setCenter(createForm());
        return LoadingOverlay.wrap(borderPane, progressIndicator);
    }

    private Node createHeader() {
        HBox hBox = new HBox(lblHeaderTitle);
        hBox.setPadding(new Insets(25));
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private Node createForm() {
        VBox vBox = new VBox(25, createFirstSection(), createSecondSection(), createFooter());
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private Node createFirstSection() {
        GridPane gridPane = new GridPane(25, 25);
        gridPane.add(lblIsbn, 1, 0 );
        gridPane.add(tfIsbn, 2, 0);
        gridPane.add(lblTitle, 3, 0);
        gridPane.add(tfTitle, 4, 0);
        gridPane.add(btnAddGenre, 1, 1);
        gridPane.add(createTableViewGenre(), 2, 1);
        gridPane.add(lblCoverImage, 3, 1);
        gridPane.add(createAttachFileButton(), 4, 1);
        gridPane.setAlignment(Pos.TOP_CENTER);
        return gridPane;
    }

    private Node createSecondSection() {
        tableViewAuthor.setPrefWidth(400);
        columnFirstName.setPrefWidth(200);
        columnLastName.setPrefWidth(200);
        HBox hBox = new HBox(25, btnAddAuthor, tableViewAuthor);
        hBox.setPrefHeight(200);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private Node createTableViewGenre() {
        HBox hBox = new HBox(tableViewGenre);
        hBox.setPrefHeight(180);
        hBox.setPrefWidth(200);
        columnGenre.setPrefWidth(200);
        return hBox;
    }

    private Node createAttachFileButton() {
        // Configure cover image view
        coverImageView.setFitWidth(200);
        coverImageView.setPreserveRatio(true);
        coverImageView.setSmooth(true);
        coverImageView.setCache(true);

        btnAttachCoverImage = new Button("", coverImageView);
        return btnAttachCoverImage;
    }

    private Node createFooter() {
        HBox hBox = new HBox(400, btnGoBack, btnAddBook);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(25));
        return hBox;
    }
}
