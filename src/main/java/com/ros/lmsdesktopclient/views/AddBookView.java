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
        scene.getStylesheets().add(getClass().getResource("/styles/add-book-view.css").toExternalForm());
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

        // Form sections (basic info, image/genre, authors)
        VBox formSections = new VBox(30,
                createBasicInfoSection(),
                createImageAndGenreSection(),
                createAuthorsContainer()
        );
        formSections.setAlignment(Pos.CENTER);
        formSections.setMaxWidth(900); // prevent excessive stretching
        formSections.getStyleClass().add("form-card");

        // Wrap in a StackPane to center horizontally
        StackPane centeredWrapper = new StackPane(formSections);
        centeredWrapper.setPadding(new Insets(20));
        centeredWrapper.setAlignment(Pos.TOP_CENTER);

        // Scrollable area
        ScrollPane scrollPane = new ScrollPane(centeredWrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        borderPane.setCenter(scrollPane);

        // Footer stays fixed
        borderPane.setBottom(createFooter());

        return LoadingOverlay.wrap(borderPane, progressIndicator);
    }

    private Node createHeader() {
        HBox header = new HBox(lblHeaderTitle);
        lblHeaderTitle.setId("lblHeaderTitle");
        header.setPadding(new Insets(25));
        header.setAlignment(Pos.CENTER);
        header.setId("header");
        return header;
    }

    private Node createBasicInfoSection() {
        GridPane gridPane = new GridPane(25, 15);
        gridPane.add(lblIsbn, 0, 0);
        gridPane.add(tfIsbn, 1, 0);
        gridPane.add(lblTitle, 0, 1);
        gridPane.add(tfTitle, 1, 1);
        gridPane.setAlignment(Pos.CENTER);
        return gridPane;
    }

    private Node createImageAndGenreSection() {
        HBox box = new HBox(40, createAttachFileButton(), createListViewGenre());
        box.setAlignment(Pos.CENTER);
        return box;
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
        HBox hBox = new HBox(40, btnGoBack, btnAddBook);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(25));
        return hBox;
    }
}
