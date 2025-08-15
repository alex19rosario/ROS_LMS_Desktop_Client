package com.ros.lmsdesktopclient.di.modules;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Module
public class ConcurrencyModule {

    @Provides
    @Singleton
    static ExecutorService provideExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
