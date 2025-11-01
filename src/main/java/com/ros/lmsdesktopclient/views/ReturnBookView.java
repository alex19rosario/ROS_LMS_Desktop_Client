package com.ros.lmsdesktopclient.views;

import com.ros.lmsdesktopclient.util.LoadingOverlay;
import com.ros.lmsdesktopclient.view_models.ReturnBookViewModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.util.Objects;

public class ReturnBookView implements BaseView {

    private final ReturnBookViewModel returnBookViewModel;

    private Label lblHeaderTitle;
    private Label lblBookIsbn;
    private TextField tfBookIsbn;
    private Button btnReturnBook;
    private Button btnGoBack;
    private ProgressIndicator progressIndicator;

    @Inject
    public ReturnBookView(ReturnBookViewModel returnBookViewModel) {
        this.returnBookViewModel = returnBookViewModel;
    }

    @Override
    public void start(Stage stage) {
        initComponents();
        Scene scene = new Scene(createContent(), stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/return-book-view.css")).toExternalForm());
        bindComponents();
        stage.setScene(scene);
    }

    private void initComponents() {
        lblHeaderTitle = new Label("Returning a book");
        lblBookIsbn = new Label("ISBN");
        tfBookIsbn = new TextField();
        btnReturnBook = new Button("Return Book");
        btnGoBack = new Button("Go Back");
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
    }

    private void bindComponents() {
        tfBookIsbn.textProperty().bindBidirectional(returnBookViewModel.getReturnBookModel().isbnProperty());
        btnReturnBook.setOnAction(_ -> returnBookViewModel.executeReturnBookCommand());
        btnGoBack.setOnAction(_ -> returnBookViewModel.executeOpenMainViewCommand());

        // Bind progress indicator visibility and progress
        progressIndicator.visibleProperty().bind(returnBookViewModel.getReturnBookCommand().runningProperty());
        progressIndicator.progressProperty().bind(returnBookViewModel.getReturnBookCommand().progressProperty());

    }

    private Region createContent() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createHeader());

        //Form section
        VBox formSection = new VBox(30, createForm());
         formSection.setAlignment(Pos.CENTER);
         formSection.setMaxWidth(900);

         //Wrap in a StackPane to center horizontally
        StackPane centeredWrapper = new StackPane(formSection);
        centeredWrapper.setPadding(new Insets(20));
        centeredWrapper.setAlignment(Pos.TOP_CENTER);

        borderPane.setCenter(centeredWrapper);

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

    private Node createForm() {
        GridPane gridPane = new GridPane(25, 15);
        gridPane.add(lblBookIsbn, 0, 0);
        gridPane.add(tfBookIsbn, 1, 0);
        gridPane.setAlignment(Pos.CENTER);

        VBox formCard = new VBox(gridPane);
        formCard.getStyleClass().add("form-card");
        formCard.setAlignment(Pos.CENTER);
        return formCard;
    }

    private Node createFooter() {
        HBox hBox = new HBox(40, btnGoBack, btnReturnBook);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(25));
        return hBox;
    }
}
