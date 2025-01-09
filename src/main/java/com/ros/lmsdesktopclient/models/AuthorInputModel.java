package com.ros.lmsdesktopclient.models;

import javafx.scene.control.TextField;


public class AuthorInputModel {

    private TextField tfFirstName;
    private TextField tfLastName;

    public AuthorInputModel(){
        this.tfFirstName = new TextField();
        this.tfLastName = new TextField();
    }

    public TextField getTfFirstName() {
        return tfFirstName;
    }

    public void setTfFirstName(TextField tfFirstName) {
        this.tfFirstName = tfFirstName;
    }

    public TextField getTfLastName() {
        return tfLastName;
    }

    public void setTfLastName(TextField tfLastName) {
        this.tfLastName = tfLastName;
    }
}
