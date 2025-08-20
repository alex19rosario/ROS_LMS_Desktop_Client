package com.ros.lmsdesktopclient.util;

import com.ros.lmsdesktopclient.util.enums.ViewType;
import com.ros.lmsdesktopclient.views.BaseView;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class ViewHandler {
    private final Map<ViewType, Provider<BaseView>> views;
    private Stage stage;

    @Inject
    public ViewHandler(Map<ViewType, Provider<BaseView>> views) {
        this.views = views;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void switchTo(ViewType viewType) {
        BaseView view = views.get(viewType).get();
        view.start(stage);
    }
}
