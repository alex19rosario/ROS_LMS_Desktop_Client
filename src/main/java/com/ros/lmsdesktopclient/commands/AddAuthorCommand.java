package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.models.AuthorModel;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.concurrent.Task;
import javax.inject.Inject;


public class AddAuthorCommand extends Command {

    private final ListProperty<AuthorModel> authorModelListProperty;

    @Inject
    public AddAuthorCommand(ListProperty<AuthorModel> authorModelListProperty){
        this.authorModelListProperty = authorModelListProperty;
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() ->
                        authorModelListProperty.addFirst(new AuthorModel())
                );
                return null;
            }
        };
    }
}
