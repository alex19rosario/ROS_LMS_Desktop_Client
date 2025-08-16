package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.ViewType;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

public class LogoutCommand extends Command{

    private final ViewHandler viewHandler;
    private final TokenHandler tokenHandler;

    @Inject
    public LogoutCommand(
            ExecutorService executorService,
            ViewHandler viewHandler,
            TokenHandler tokenHandler
    ){
        super(executorService);
        this.viewHandler = viewHandler;
        this.tokenHandler = tokenHandler;
        setOnCommandSuccess(this::onSuccess);
    }

    @Override
    protected void runCommand() throws Exception {
        tokenHandler.removeAll();
    }

    private void onSuccess(){
        viewHandler.switchTo(ViewType.LOGIN);
    }
}
