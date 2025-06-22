package com.ros.lmsdesktopclient.util;

import javafx.stage.Stage;

public class ViewHandler {

    private Stage stage;

    private static class SingletonHelper {
        private static final ViewHandler INSTANCE = new ViewHandler();
    }

    public static void setStageToInstance(Stage stage){
        getInstance().setStage(stage);
    }

    public static ViewHandler getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return this.stage;
    }

    public static void switchTo(Views view) {
        view.getViewInstance().start(getInstance().getStage());
    }
}
