package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.models.AuthorInputModel;
import com.ros.lmsdesktopclient.models.AuthorModel;
import com.ros.lmsdesktopclient.util.Views;
import com.ros.lmsdesktopclient.view_models.commands.AddAuthorCommand;
import com.ros.lmsdesktopclient.view_models.commands.Command;
import com.ros.lmsdesktopclient.view_models.commands.OpenViewCommand;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class AddBookViewModel {
    private final Command openMainViewCommand;
    private final Command addAuthorCommand;
    private final ListProperty<AuthorInputModel> authorInputs;
    private List<AuthorModel> authors;

    public AddBookViewModel(){
        openMainViewCommand = new OpenViewCommand(Views.MAIN_MENU);
        authorInputs = new SimpleListProperty<>(FXCollections.observableArrayList());
        authors = new ArrayList<>();
        AuthorInputModel authorInputModel = new AuthorInputModel();
        AuthorModel author = new AuthorModel();
        authorInputModel.getTfFirstName().textProperty().bindBidirectional(author.firstNameProperty());
        authorInputModel.getTfLastName().textProperty().bindBidirectional(author.lastNameProperty());
        authorInputs.addFirst(authorInputModel);
        authors.addFirst(author);
        addAuthorCommand = new AddAuthorCommand(authorInputs.get(), authors);
    }

    public void executeOpenMainViewCommand(){
        this.openMainViewCommand.execute();
    }

    public void executeAddAuthorCommand(){
        this.addAuthorCommand.execute();
    }

    public ObservableList<AuthorInputModel> getAuthorInputs() {
        return authorInputs.get();
    }

    public ListProperty<AuthorInputModel> authorInputsProperty() {
        return authorInputs;
    }

    public void testBinding(){
        authors.forEach(System.out::println);
    }
}
