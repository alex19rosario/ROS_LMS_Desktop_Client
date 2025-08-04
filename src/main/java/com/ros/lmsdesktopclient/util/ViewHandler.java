package com.ros.lmsdesktopclient.util;

import com.ros.lmsdesktopclient.di.factories.DaggerViewFactory;
import com.ros.lmsdesktopclient.util.enums.Views;
import com.ros.lmsdesktopclient.views.BaseView;
import com.ros.lmsdesktopclient.di.factories.ViewFactory;
import javafx.stage.Stage;

public class ViewHandler {

    private Stage stage;

    private static final ViewFactory factory = DaggerViewFactory.create();


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
        BaseView baseView = view.getViewInstance(factory); // pass factory here
        baseView.start(getInstance().getStage());
    }
}
