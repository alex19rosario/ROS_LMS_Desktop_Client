package com.ros.lmsdesktopclient.views;

import com.ros.lmsdesktopclient.models.AuthorInputModel;
import com.ros.lmsdesktopclient.view_models.AddBookViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class AddBookController implements Initializable {

    private AddBookViewModel addBookViewModel;
    @FXML private TableView<AuthorInputModel> tableViewAuthor;
    @FXML private TableColumn<AuthorInputModel, TextField> columnFirstName;
    @FXML private TableColumn<AuthorInputModel, TextField> columnLastName;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBookViewModel = new AddBookViewModel();
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("tfFirstName"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("tfLastName"));
        tableViewAuthor.itemsProperty().bindBidirectional(addBookViewModel.authorInputsProperty());
    }

    @FXML
    private void goBack(ActionEvent actionEvent) {
        addBookViewModel.executeOpenMainViewCommand();
    }

    @FXML
    private void addBook(ActionEvent actionEvent) {

    }

    @FXML
    private void addAuthor(ActionEvent actionEvent) {
        addBookViewModel.executeAddAuthorCommand();
    }
}
