package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.UiExecutor;
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
            TokenHandler tokenHandler,
            UiExecutor uiExecutor
    ){
        super(executorService, uiExecutor);
        this.viewHandler = viewHandler;
        this.tokenHandler = tokenHandler;
        setOnCommandSuccess(this::onSuccess);
    }

    @Override
    protected void runCommand() throws Exception {
        tokenHandler.removeAll();
    }

    void onSuccess(){
        viewHandler.switchTo(ViewType.LOGIN);
    }
}
