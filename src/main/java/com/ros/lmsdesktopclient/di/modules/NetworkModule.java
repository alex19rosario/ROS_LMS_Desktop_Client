package com.ros.lmsdesktopclient.di.modules;

import com.ros.lmsdesktopclient.di.factories.AuthenticatedHttpClientFactory;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.http.HttpClient;

@Module
public abstract class NetworkModule {
    @Provides
    public static AuthenticatedHttpClientFactory provideHttpClientFactory() {
        return (username, password) -> HttpClient.newBuilder()
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                username,
                                password.toCharArray()
                        );
                    }
                })
                .build();
    }

    @Provides
    @Singleton
    public static HttpClient provideHttpClient() {
        return HttpClient.newHttpClient();
    }
}
