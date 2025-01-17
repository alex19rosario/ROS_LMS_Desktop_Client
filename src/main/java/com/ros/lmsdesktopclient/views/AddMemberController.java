package com.ros.lmsdesktopclient.views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AddMemberController implements Initializable {




    @FXML
    private void goBack(){
        System.out.println("Going back");
    }

    @FXML
    private void addMember(){
        System.out.println("Adding member");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
