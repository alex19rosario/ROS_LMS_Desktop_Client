package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.models.AuthorModel;
import com.ros.lmsdesktopclient.util.UiExecutor;
import javafx.beans.property.ListProperty;
import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

public class AddAuthorCommand extends Command {

    private final ListProperty<AuthorModel> authorModelListProperty;

    @Inject
    public AddAuthorCommand(
            ListProperty<AuthorModel> authorModelListProperty,
            ExecutorService executorService,
            UiExecutor uiExecutor
    ){
        super(executorService, uiExecutor);
        this.authorModelListProperty = authorModelListProperty;
    }

    @Override
    protected void runCommand() throws Exception {
        getUiExecutor().runLater(() -> authorModelListProperty.addFirst(new AuthorModel()));
    }
}
