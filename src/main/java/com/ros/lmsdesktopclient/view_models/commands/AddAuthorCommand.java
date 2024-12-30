package com.ros.lmsdesktopclient.view_models.commands;

import com.ros.lmsdesktopclient.models.AuthorInputModel;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class AddAuthorCommand extends Command {

    private ObservableList<AuthorInputModel> authorInputs;

    public AddAuthorCommand(ObservableList<AuthorInputModel> authorInputs){
        this.authorInputs = authorInputs;
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                authorInputs.add(new AuthorInputModel());
                return null;
            }
        };
    }
}
