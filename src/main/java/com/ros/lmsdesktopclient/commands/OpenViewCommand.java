package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.ViewType;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

public class OpenViewCommand extends Command{

    private final ViewType view;
    private final ViewHandler viewHandler;

    @Inject
    public OpenViewCommand(
            ViewType view,
            ExecutorService executorService,
            ViewHandler viewHandler,
            UiExecutor uiExecutor
    ){
        super(executorService, uiExecutor);
        this.view = view;
        this.viewHandler = viewHandler;
    }

    @Override
    protected void runCommand() throws Exception {
        getUiExecutor().runLater(() -> viewHandler.switchTo(this.view));
    }
}
