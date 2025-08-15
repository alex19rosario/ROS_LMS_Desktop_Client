package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.Views;

import javax.inject.Inject;

public class LogoutCommand extends Command{

    @Inject
    public LogoutCommand(){
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
