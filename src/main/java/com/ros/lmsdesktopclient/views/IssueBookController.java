package com.ros.lmsdesktopclient.views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class IssueBookController implements Initializable {



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void issueBook() {
        System.out.println("Issuing book...");   // Issue book
    }

    @FXML
    private void goBack() {
        System.out.println("Going back...");   // Go back
    }

    @FXML
    private void scanMember() {
        System.out.println("Scanning member...");   // Search book
    }
}
