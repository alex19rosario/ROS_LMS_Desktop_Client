package com.ros.lmsdesktopclient.views;

import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.util.LoadingOverlay;
import com.ros.lmsdesktopclient.util.enums.BookStatus;
import com.ros.lmsdesktopclient.util.UpFrontDataHandler;
import com.ros.lmsdesktopclient.view_models.IssueBookViewModel;
import javafx.beans.binding.Bindings;
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

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Objects;

public class IssueBookView implements BaseView {

    private final IssueBookViewModel issueBookViewModel;
    private final UpFrontDataHandler upFrontDataHandler;

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
    private Pagination pagination;
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
    private ProgressIndicator progressIndicator;

    @Inject
    public IssueBookView(IssueBookViewModel issueBookViewModel, UpFrontDataHandler upFrontDataHandler) {
        this.issueBookViewModel = issueBookViewModel;
        this.upFrontDataHandler = upFrontDataHandler;
    }

    @Override
    public void start(Stage stage) {
        initComponents();
        Scene scene = new Scene(createContent(), stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/issue-book-view.css")).toExternalForm());
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
        pagination = new Pagination();
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
        btnGoBack.setId("btnGoBack");
        btnIssueBook.setId("btnIssueBook");
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);

        // Loading data to comboBoxes
        cbGenre.setItems(FXCollections
                .observableList(upFrontDataHandler.getGenres()
                        .stream().toList()));

        cbStatus.setItems(FXCollections
                .observableList(
                        Arrays.stream(BookStatus.values())
                                .map(BookStatus::toString)
                                .toList()));

        pagination.setPageFactory(pageIndex -> {
            issueBookViewModel.executeLoadPageCommand();
            return new Region(); // dummy node, never shown
        });
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
        btnSearch.setOnAction(actionEvent -> issueBookViewModel.executeSearchBooksCommand());

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

        // Table Section
        tableViewBook.itemsProperty().bindBidirectional(issueBookViewModel.booksProperty());
        pagination.pageCountProperty().bindBidirectional(issueBookViewModel.totalPagesProperty());
        pagination.currentPageIndexProperty().bindBidirectional(issueBookViewModel.getSearchBookModel().pageProperty());
        tableViewBook.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                issueBookViewModel.setSelectedRowModel(selected);
                issueBookViewModel.executeSelectBookCommand();
            }
        });

        // Selected Book Section
        imageSelectedBookCover.imageProperty().bindBidirectional(issueBookViewModel.getSelectedBookModel().coverImageProperty());
        lblSelectedBookIsbn.textProperty().bindBidirectional(issueBookViewModel.getSelectedBookModel().isbnProperty());
        lblSelectedBookTitle.textProperty().bindBidirectional(issueBookViewModel.getSelectedBookModel().titleProperty());
        lblSelectedBookAuthors.textProperty().bindBidirectional(issueBookViewModel.getSelectedBookModel().authorsProperty());
        lblSelectedBookGenres.textProperty().bindBidirectional(issueBookViewModel.getSelectedBookModel().genresProperty());
        lblSelectedBookStatus.textProperty().bindBidirectional(issueBookViewModel.getSelectedBookModel().statusProperty());

        // Selected Member Section
        tfMemberUsername.textProperty().bindBidirectional(issueBookViewModel.memberUsernameProperty());
        btnGoBack.setOnAction(actionEvent -> issueBookViewModel.executeOpenMainViewCommand());
        btnIssueBook.setOnAction(actionEvent -> issueBookViewModel.executeIssueBookCommand());

        // Bind progress indicator visibility and progress
        // Boolean binding for visibility: show if ANY command is running
        progressIndicator.visibleProperty().bind(
                Bindings.or(
                        issueBookViewModel.getSearchBooksCommand().runningProperty(),
                        Bindings.or(
                                issueBookViewModel.getIssueBookCommand().runningProperty(),
                                Bindings.or(
                                        issueBookViewModel.getLoadBooksCommand().runningProperty(),
                                        issueBookViewModel.getSelectBookCommand().runningProperty()
                                )
                        )
                )
        );
        // Double binding for progress: show average progress of both commands
        progressIndicator.progressProperty().bind(
            Bindings.when(issueBookViewModel.getSearchBooksCommand().runningProperty())
                .then(issueBookViewModel.getSearchBooksCommand().progressProperty())
                .otherwise(
                    Bindings.when(issueBookViewModel.getIssueBookCommand().runningProperty())
                        .then(issueBookViewModel.getIssueBookCommand().progressProperty())
                        .otherwise(
                                Bindings.when(issueBookViewModel.getLoadBooksCommand().runningProperty())
                                        .then(issueBookViewModel.getLoadBooksCommand().progressProperty())
                                        .otherwise(
                                                Bindings.when(issueBookViewModel.getSelectBookCommand().runningProperty())
                                                        .then(issueBookViewModel.getSelectBookCommand().progressProperty())
                                                        .otherwise(0.0)
                                        )
                        )
                )
        );
    }

    private Region createContent() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createHeader());

        // Form Sections (Filter, Table, Details)
        VBox formSections = new VBox(30,
                createSectionCard("Filter Books", createFilterSection()),
                createSectionCard("Books", createTableSection()),
                createSectionCard("Selected Book & Member", createDetailSection())
        );
        formSections.setAlignment(Pos.CENTER);
        formSections.setMaxWidth(1500);
        formSections.getStyleClass().add("form-card");

        // Wrap in StackPane to center horizontally
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

    private Node createDetailSection() {
        // Selected Book Card
        VBox bookCard = new VBox(10,
                imageSelectedBookCover,
                lblSelectedBookIsbn,
                lblSelectedBookTitle,
                lblSelectedBookAuthors,
                lblSelectedBookGenres,
                lblSelectedBookStatus
        );
        bookCard.setPadding(new Insets(15));
        bookCard.getStyleClass().add("section-card");

        imageSelectedBookCover.setFitWidth(200);
        imageSelectedBookCover.setPreserveRatio(true);
        imageSelectedBookCover.setSmooth(true);
        imageSelectedBookCover.setCache(true);

        // Selected Member Card
        VBox memberCard = new VBox(10,
                lblMemberUsername,
                tfMemberUsername
        );
        memberCard.setPadding(new Insets(15));
        memberCard.getStyleClass().add("section-card");

        // Side by side
        HBox hBox = new HBox(30, bookCard, memberCard);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private Node createTableSection() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(15));
        borderPane.setCenter(tableViewBook);
        borderPane.setBottom(pagination);
        tableViewBook.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        return borderPane;
    }

    private VBox createSectionCard(String title, Node content) {
        Label lblTitle = new Label(title);
        lblTitle.getStyleClass().add("section-title");

        VBox vBox = new VBox(15, lblTitle, content);
        vBox.setPadding(new Insets(20));
        vBox.getStyleClass().add("section-card");
        return vBox;
    }

    private Node createHeader() {
        HBox hBox = new HBox(lblHeaderTitle);
        lblHeaderTitle.setId("lblHeaderTitle");
        hBox.setPadding(new Insets(25));
        hBox.setAlignment(Pos.CENTER);
        hBox.setId("header");
        return hBox;
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

    private Node createFooter() {
        HBox hBox = new HBox(400, btnGoBack, btnIssueBook);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(25));
        return hBox;
    }

}
