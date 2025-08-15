package com.ros.lmsdesktopclient;

import com.ros.lmsdesktopclient.di.factories.AppFactory;
import com.ros.lmsdesktopclient.di.factories.DaggerAppFactory;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.ViewType;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Ros Library Management System");
        AppFactory appFactory = DaggerAppFactory.create();
        ViewHandler viewHandler = appFactory.viewHandler();
        viewHandler.setStage(stage);
        viewHandler.switchTo(ViewType.LOGIN);
    }

    public static void main(String[] args) {
        launch();
    }
}