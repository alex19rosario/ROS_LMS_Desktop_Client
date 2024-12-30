package com.ros.lmsdesktopclient.views;

import com.ros.lmsdesktopclient.models.AuthorInputModel;
import com.ros.lmsdesktopclient.models.GenreInputModel;
import com.ros.lmsdesktopclient.view_models.AddBookViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
    }

    @FXML
    private void goBack(ActionEvent actionEvent) {
        addBookViewModel.executeOpenMainViewCommand();
    }

    @FXML
    private void addBook(ActionEvent actionEvent) {
        addBookViewModel.executeAddBookCommand();
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
