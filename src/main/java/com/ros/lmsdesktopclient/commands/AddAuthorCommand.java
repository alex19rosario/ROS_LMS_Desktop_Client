package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.models.AuthorModel;
import com.ros.lmsdesktopclient.util.UiExecutor;
import javafx.beans.property.ListProperty;
import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

public class AddAuthorCommand extends Command {

    private final ListProperty<AuthorModel> authorModelListProperty;
    private final UiExecutor javaFxUiExecutor;

    @Inject
    public AddAuthorCommand(
            ListProperty<AuthorModel> authorModelListProperty,
            ExecutorService executorService,
            UiExecutor javaFxUiExecutor
    ){
        super(executorService);
        this.authorModelListProperty = authorModelListProperty;
        this.javaFxUiExecutor = javaFxUiExecutor;
    }

    @Override
    protected void runCommand() throws Exception {
        javaFxUiExecutor.runLater(() -> authorModelListProperty.addFirst(new AuthorModel()));
    }
}
