package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.Views;
import javafx.application.Platform;

import javax.inject.Inject;

public class OpenViewCommand extends Command{

    private final Views view;

    @Inject
    public OpenViewCommand(Views view){
        this.view = view;
    }

    @Override
    protected void runCommand() throws Exception {
        Platform.runLater(() -> ViewHandler.switchTo(this.view));
    }
}
