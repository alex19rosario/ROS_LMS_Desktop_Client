package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.models.AuthorInputModel;
import com.ros.lmsdesktopclient.util.Views;
import com.ros.lmsdesktopclient.view_models.commands.AddAuthorCommand;
import com.ros.lmsdesktopclient.view_models.commands.Command;
import com.ros.lmsdesktopclient.view_models.commands.OpenViewCommand;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AddBookViewModel {
    private final Command openMainViewCommand;
    private final Command addAuthorCommand;
    private final ListProperty<AuthorInputModel> authorInputs;

    public AddBookViewModel(){
        openMainViewCommand = new OpenViewCommand(Views.MAIN_MENU);
        authorInputs = new SimpleListProperty<>(FXCollections.observableArrayList());
        authorInputs.addFirst(new AuthorInputModel());
        addAuthorCommand = new AddAuthorCommand(authorInputs.get());
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
}
