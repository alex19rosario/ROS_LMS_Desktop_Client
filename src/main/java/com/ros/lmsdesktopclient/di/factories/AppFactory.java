package com.ros.lmsdesktopclient.di.factories;

import com.ros.lmsdesktopclient.di.modules.*;
import com.ros.lmsdesktopclient.util.ViewHandler;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        CommandModule.class,
        ServiceModule.class,
        NetworkModule.class,
        ModelModule.class,
        ViewModelModule.class,
        ConcurrencyModule.class,
        AppModule.class,
        ViewModule.class
} )
public interface AppFactory {
    ViewHandler viewHandler();
}
