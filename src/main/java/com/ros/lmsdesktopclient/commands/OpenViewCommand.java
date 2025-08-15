package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.Views;
import javafx.application.Platform;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

public class OpenViewCommand extends Command{

    private final Views view;

    @Inject
    public OpenViewCommand(Views view, ExecutorService executorService){
        super(executorService);
        this.view = view;
    }

    @Override
    protected void runCommand() throws Exception {
        Platform.runLater(() -> ViewHandler.switchTo(this.view));
    }
}
