package com.ros.lmsdesktopclient;

import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.Views;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Ros Library Management System");
        ViewHandler.setStageToInstance(stage);
        ViewHandler.switchTo(Views.LOGIN);
    }

    public static void main(String[] args) {
        launch();
    }
}