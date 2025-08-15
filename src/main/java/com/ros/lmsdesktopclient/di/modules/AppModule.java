package com.ros.lmsdesktopclient.di.modules;

import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.ViewType;
import com.ros.lmsdesktopclient.views.BaseView;
import dagger.Module;
import dagger.Provides;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Map;

@Module
public class AppModule {

    @Provides
    @Singleton
    static ViewHandler provideViewHandler(Map<ViewType, Provider<BaseView>> views) {
        return new ViewHandler(views);
    }
}
