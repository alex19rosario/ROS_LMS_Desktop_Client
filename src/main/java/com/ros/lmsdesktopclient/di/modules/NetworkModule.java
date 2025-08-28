package com.ros.lmsdesktopclient.di.modules;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.http.HttpClient;

@Module
public abstract class NetworkModule {
    @Provides
    @Singleton
    public static HttpClient provideHttpClient() {
        return HttpClient.newHttpClient();
    }
}
