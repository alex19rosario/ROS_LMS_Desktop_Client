package com.ros.lmsdesktopclient.models;

import javafx.scene.control.TextField;

import javax.inject.Inject;


/**
 * Holds JavaFX {@link TextField} components used for author input in a UI form.
 *
 * <p>This model is meant for use in JavaFX desktop applications to represent
 * and manage the input fields for an author's first and last names.</p>
 *
 * Fields:
 * <ul>
 *   <li><b>tfFirstName</b>: JavaFX TextField component for the author's first name input.</li>
 *   <li><b>tfLastName</b>: JavaFX TextField component for the author's last name input.</li>
 * </ul>
 */
public class AuthorInputModel {

    private TextField tfFirstName;
    private TextField tfLastName;

    @Inject
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
