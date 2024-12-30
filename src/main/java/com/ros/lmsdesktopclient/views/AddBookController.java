package com.ros.lmsdesktopclient.views;

import com.ros.lmsdesktopclient.view_models.AddBookViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AddBookController implements Initializable {

    private AddBookViewModel addBookViewModel;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBookViewModel = new AddBookViewModel();
    }

    @FXML
    private void goBack(ActionEvent actionEvent) {
        addBookViewModel.executeOpenMainViewCommand();
    }

    @FXML
    private void addBook(ActionEvent actionEvent) {
        System.out.println("Adding book...");
    }

    @FXML
    private void addAuthor(ActionEvent actionEvent) {
        System.out.println("Adding author...");
    }
}
