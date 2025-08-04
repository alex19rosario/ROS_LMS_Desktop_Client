package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.Views;
import javafx.concurrent.Task;

import javax.inject.Inject;

public class LogoutCommand extends Command{

    @Inject
    public LogoutCommand(){
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
        ViewHandler.switchTo(Views.LOGIN);
    }
}
