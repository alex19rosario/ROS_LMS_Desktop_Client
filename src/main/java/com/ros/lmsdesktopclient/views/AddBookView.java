package com.ros.lmsdesktopclient.views;

import com.ros.lmsdesktopclient.models.AuthorModel;
import com.ros.lmsdesktopclient.models.GenreModel;
import com.ros.lmsdesktopclient.util.LoadingOverlay;
import com.ros.lmsdesktopclient.view_models.AddBookViewModel;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import javax.inject.Inject;


public class AddBookView implements BaseView {

    private AddBookViewModel addBookViewModel;

    private Label lblHeaderTitle;
    private Label lblIsbn;
    private TextField tfIsbn;
    private Label lblTitle;
    private TextField tfTitle;
    private Label lblAuthors;
    private VBox authorsContainer;
    private Button btnAddAuthor;
    private Label lblCoverImage;
    private ImageView coverImageView;
    private ListView<GenreModel> genreModelListView;
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
        lblAuthors = new Label("Authors");
        btnAddAuthor = new Button("+");
        lblCoverImage = new Label("Cover Image");
        coverImageView = new ImageView();
        genreModelListView = new ListView<>();
        btnAddBook = new Button("Add Book");
        btnGoBack = new Button("Go Back");
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
    }

    private void bindComponents() {
        tfIsbn.textProperty().bindBidirectional(addBookViewModel.getBookModel().isbnProperty());
        tfTitle.textProperty().bindBidirectional(addBookViewModel.getBookModel().titleProperty());

        coverImageView.imageProperty().bindBidirectional(addBookViewModel.getBookModel().coverImageProperty());
        genreModelListView.setItems(addBookViewModel.getGenreModelObservableList());

        genreModelListView.setCellFactory(CheckBoxListCell.forListView(
                GenreModel::selectedProperty,
                new StringConverter<>() {
                    @Override
                    public String toString(GenreModel genreModel) {
                        return genreModel == null ? "" : genreModel.getGenre();
                    }

                    @Override
                    public GenreModel fromString(String s) {
                        throw new UnsupportedOperationException("Not needed");
                    }
                }
        ));

        // Bind authors list to container
        bindAuthorContainer(addBookViewModel.getAuthorModelListProperty());

        btnAddAuthor.setOnAction(actionEvent -> addBookViewModel.executeAddAuthorCommand());
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
        VBox vBox = new VBox(25, createFirstSection(), createAuthorsContainer(), createFooter());
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private Node createFirstSection() {
        GridPane gridPane = new GridPane(25, 25);
        gridPane.add(lblIsbn, 1, 0 );
        gridPane.add(tfIsbn, 2, 0);
        gridPane.add(lblTitle, 3, 0);
        gridPane.add(tfTitle, 4, 0);
        gridPane.add(createListViewGenre(), 2, 1);
        gridPane.add(lblCoverImage, 3, 1);
        gridPane.add(createAttachFileButton(), 4, 1);
        gridPane.setAlignment(Pos.TOP_CENTER);
        return gridPane;
    }

    private Node createAuthorsContainer() {

        authorsContainer = new VBox(5);
        HBox header = new HBox(10, lblAuthors, btnAddAuthor);
        VBox wrapper = new VBox(10, header, authorsContainer);
        wrapper.setPadding(new Insets(10));
        wrapper.setMaxWidth(400);
        return wrapper;
    }

    private void bindAuthorContainer(ObservableList<AuthorModel> authors) {
        authorsContainer.getChildren().clear();

        // Add rows for existing authors
        for (AuthorModel author : authors) {
            authorsContainer.getChildren().add(createAuthorRow(author));
        }

        // Listen for changes in the list
        authors.addListener((javafx.collections.ListChangeListener<AuthorModel>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (AuthorModel added : change.getAddedSubList()) {
                        authorsContainer.getChildren().add(createAuthorRow(added));
                    }
                }
                if (change.wasRemoved()) {
                    for (AuthorModel removed : change.getRemoved()) {
                        authorsContainer.getChildren().removeIf(node -> node.getUserData() == removed);
                    }
                }
            }
        });
    }

    private HBox createAuthorRow(AuthorModel author) {
        TextField tfFirstName = new TextField();
        tfFirstName.setPromptText("First Name");
        tfFirstName.textProperty().bindBidirectional(author.firstNameProperty());

        TextField tfLastName = new TextField();
        tfLastName.setPromptText("Last Name");
        tfLastName.textProperty().bindBidirectional(author.lastNameProperty());

        Button removeBtn = new Button("x");
        removeBtn.setOnAction(e -> addBookViewModel.getAuthorModelListProperty().remove(author));

        HBox row = new HBox(10, tfFirstName, tfLastName, removeBtn);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setUserData(author);

        return row;
    }

    private Node createListViewGenre() {
        HBox hBox = new HBox(genreModelListView);
        hBox.setPrefHeight(180);
        hBox.setPrefWidth(200);
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
