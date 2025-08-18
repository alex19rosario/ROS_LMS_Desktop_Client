package com.ros.lmsdesktopclient.util;

import javafx.application.Platform;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JavaFxUiExecutor implements UiExecutor {

    @Inject
    public JavaFxUiExecutor(){}

    @Override
    public void runLater(Runnable runnable) {
        Platform.runLater(runnable);
    }
}
