package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.models.AuthorInputModel;
import com.ros.lmsdesktopclient.models.AuthorModel;
import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import javax.inject.Inject;
import java.util.List;

public class AddAuthorCommand extends Command {

    private final ObservableList<AuthorInputModel> authorInputs;
    private final List<AuthorModel> authors;

    @Inject
    public AddAuthorCommand(ListProperty<AuthorInputModel> authorInputs, List<AuthorModel> authors){
        this.authorInputs = authorInputs.get();
        this.authors = authors;
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                AuthorInputModel authorInputModel = new AuthorInputModel();
                AuthorModel authorModel = new AuthorModel();
                authorInputModel.getTfFirstName().textProperty().bindBidirectional(authorModel.firstNameProperty());
                authorInputModel.getTfLastName().textProperty().bindBidirectional(authorModel.lastNameProperty());
                authorInputs.addFirst(authorInputModel);
                authors.addFirst(authorModel);
                return null;
            }
        };
    }
}
