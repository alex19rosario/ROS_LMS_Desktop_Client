package com.ros.lmsdesktopclient;

import com.ros.lmsdesktopclient.util.Views;
import com.ros.lmsdesktopclient.util.ViewHandler;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Ros Library Management System");
        ViewHandler.getInstance(stage);
        ViewHandler.switchTo(Views.LOGIN.getView());
        ViewHandler.getInstance().start();
    }

    public static void main(String[] args) {
        launch();
    }
}