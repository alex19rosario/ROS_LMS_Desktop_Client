package com.ros.lmsdesktopclient.views;

import com.ros.lmsdesktopclient.models.AuthorInputModel;
import com.ros.lmsdesktopclient.models.GenreInputModel;
import com.ros.lmsdesktopclient.util.Effects;
import com.ros.lmsdesktopclient.view_models.AddBookViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class AddBookController implements Initializable {

    private AddBookViewModel addBookViewModel;
    @FXML private TextField tfISBN;
    @FXML private TextField tfTitle;
    @FXML private TableView<AuthorInputModel> tableViewAuthor;
    @FXML private TableColumn<AuthorInputModel, TextField> columnFirstName;
    @FXML private TableColumn<AuthorInputModel, TextField> columnLastName;
    @FXML private TableView<GenreInputModel> tableViewGenre;
    @FXML private TableColumn<GenreInputModel, ComboBox<String>> columnGenre;
    @FXML private Button btnAddBook;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBookViewModel = new AddBookViewModel();
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("tfFirstName"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("tfLastName"));
        tableViewAuthor.itemsProperty().bindBidirectional(addBookViewModel.authorInputsProperty());
        columnGenre.setCellValueFactory(new PropertyValueFactory<>("cbGenres"));
        tableViewGenre.itemsProperty().bindBidirectional(addBookViewModel.genreInputsProperty());
        tfISBN.textProperty().bindBidirectional(addBookViewModel.getBook().isbnProperty());
        tfTitle.textProperty().bindBidirectional(addBookViewModel.getBook().titleProperty());

        // Register key pressed event handler for relevant fields
        tfISBN.setOnKeyPressed(this::handleKeyPressed);
        tfTitle.setOnKeyPressed(this::handleKeyPressed);
        tableViewAuthor.setOnKeyPressed(this::handleKeyPressed);
        tableViewGenre.setOnKeyPressed(this::handleKeyPressed);
    }

    @FXML
    private void goBack(ActionEvent actionEvent) {
        addBookViewModel.executeOpenMainViewCommand();
    }

    @FXML
    private void addBook(ActionEvent actionEvent) {
        addBookViewModel.executeAddBookCommand();
    }

    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Effects.applyHover(btnAddBook, "-fx-background-color: #005fa3;", 150);
            addBookViewModel.executeAddBookCommand();// Trigger login on Enter key press
        }
    }

    @FXML
    private void addAuthor(ActionEvent actionEvent) {
        addBookViewModel.executeAddAuthorCommand();
    }

    @FXML
    private void addGenre(ActionEvent actionEvent) {
        addBookViewModel.executeAddGenreCommand();
    }
}
