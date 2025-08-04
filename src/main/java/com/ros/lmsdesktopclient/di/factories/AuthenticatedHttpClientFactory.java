package com.ros.lmsdesktopclient.di.factories;

import java.net.http.HttpClient;

@FunctionalInterface
public interface AuthenticatedHttpClientFactory {
    HttpClient create(String username, String password);
}
