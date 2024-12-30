package com.ros.lmsdesktopclient.view_models.commands;

import com.ros.lmsdesktopclient.models.AuthorInputModel;
import com.ros.lmsdesktopclient.models.AuthorModel;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.List;

public class AddAuthorCommand extends Command {

    private final ObservableList<AuthorInputModel> authorInputs;
    private List<AuthorModel> authors;

    public AddAuthorCommand(ObservableList<AuthorInputModel> authorInputs, List<AuthorModel> authors){
        this.authorInputs = authorInputs;
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
                authorInputs.addFirst(authorInputModel);
                authors.addFirst(authorModel);
                return null;
            }
        };
    }
}
