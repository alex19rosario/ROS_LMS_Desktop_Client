package com.ros.lmsdesktopclient.view_models.commands;

import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.Views;
import javafx.concurrent.Task;

public class LogOutCommand extends Command{

    public LogOutCommand(){
        setOnCommandSuccess(this::onSuccess);
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                TokenHandler.getInstance().removeAll();
                return null;
            }
        };
    }

    private void onSuccess(){
        double width = ViewHandler.getInstance().getSceneWidth();
        double height = ViewHandler.getInstance().getSceneHeight();
        ViewHandler.switchTo(Views.LOGIN.getView(), width, height);
    }
}
