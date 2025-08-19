package com.ros.lmsdesktopclient.di.modules;

import com.ros.lmsdesktopclient.util.JavaFxUiExecutor;
import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.UpFrontDataHandler;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.ViewType;
import com.ros.lmsdesktopclient.views.BaseView;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Objects;

@Module
public abstract class AppModule {

    @Provides
    @Singleton
    static ViewHandler provideViewHandler(Map<ViewType, Provider<BaseView>> views) {
        return new ViewHandler(views);
    }

    @Provides
    @Singleton
    static UpFrontDataHandler provideUpFrontDataHandler() {
        return new UpFrontDataHandler();
    }

    @Provides
    static FileChooser provideFileChooser() {
        return new FileChooser();
    }

    @Binds
    @Singleton
    abstract UiExecutor javaFxUiExecutor(JavaFxUiExecutor uiExecutor);

    @Provides
    static Image defaultCover() {
        return new Image(Objects.requireNonNull(AppModule.class.getResource("/images/default_image.jpg")).toExternalForm());
    }
}
