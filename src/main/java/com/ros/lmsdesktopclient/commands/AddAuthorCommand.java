package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.models.AuthorModel;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

public class AddAuthorCommand extends Command {

    private final ListProperty<AuthorModel> authorModelListProperty;

    @Inject
    public AddAuthorCommand(ListProperty<AuthorModel> authorModelListProperty, ExecutorService executorService){
        super(executorService);
        this.authorModelListProperty = authorModelListProperty;
    }

    @Override
    protected void runCommand() throws Exception {
        Platform.runLater(() -> {
            authorModelListProperty.addFirst(new AuthorModel());
        });
    }
}
