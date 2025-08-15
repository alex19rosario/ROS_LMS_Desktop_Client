package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.ViewType;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

public class LogoutCommand extends Command{

    private final ViewHandler viewHandler;

    @Inject
    public LogoutCommand(ExecutorService executorService, ViewHandler viewHandler){
        super(executorService);
        this.viewHandler = viewHandler;
        setOnCommandSuccess(this::onSuccess);
    }

    @Override
    protected void runCommand() throws Exception {
        TokenHandler.getInstance().removeAll();
    }

    private void onSuccess(){
        viewHandler.switchTo(ViewType.LOGIN);
    }
}
