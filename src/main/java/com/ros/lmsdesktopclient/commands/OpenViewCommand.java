package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.Views;
import javafx.concurrent.Task;

import javax.inject.Inject;

public class OpenViewCommand extends Command{

    private final Views view;

    @Inject
    public OpenViewCommand(Views view){
        this.view = view;
        this.setOnCommandSuccess(this::onSuccess);
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                return null;
            }
        };
    }

    private void onSuccess(){
        ViewHandler.switchTo(this.view);
    }
}
