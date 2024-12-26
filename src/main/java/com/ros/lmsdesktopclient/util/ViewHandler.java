package com.ros.lmsdesktopclient.util;

import com.ros.lmsdesktopclient.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewHandler {

    private Stage stage;
    private static FXMLLoader fxmlLoader;
    private static Scene scene;

    private ViewHandler(){}

    private static class SingletonHelper{
        private static final ViewHandler _viewHandler = new ViewHandler();
    }

    public static ViewHandler getInstance(Stage stage){
        SingletonHelper._viewHandler.setStage(stage);
        return SingletonHelper._viewHandler;
    }

    public static ViewHandler getInstance(){
        return SingletonHelper._viewHandler;
    }

    private void setStage(Stage stage){
        this.stage = stage;
    }

    private Stage getStage(){
        return this.stage;
    }

    public double getSceneWidth(){
        return scene.getWidth();
    }
    public double getSceneHeight(){
        return scene.getHeight();
    }

    public static void switchTo(String view)  {
        fxmlLoader = new FXMLLoader(Application.class.getResource(view));
        try {
            scene = new Scene(fxmlLoader.load(), 1000, 700);
            SingletonHelper._viewHandler.getStage().setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void switchTo(String view, double width, double height)  {
        fxmlLoader = new FXMLLoader(Application.class.getResource(view));
        try {
            scene = new Scene(fxmlLoader.load(), width, height);
            SingletonHelper._viewHandler.getStage().setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(){
        SingletonHelper._viewHandler.getStage().show();
    }







}
