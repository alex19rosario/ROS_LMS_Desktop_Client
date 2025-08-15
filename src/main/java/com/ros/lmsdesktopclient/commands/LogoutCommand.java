package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.Views;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

public class LogoutCommand extends Command{

    @Inject
    public LogoutCommand(ExecutorService executorService){
        super(executorService);
        setOnCommandSuccess(this::onSuccess);
    }

    @Override
    protected void runCommand() throws Exception {
        TokenHandler.getInstance().removeAll();
    }

    private void onSuccess(){
        ViewHandler.switchTo(Views.LOGIN);
    }
}
