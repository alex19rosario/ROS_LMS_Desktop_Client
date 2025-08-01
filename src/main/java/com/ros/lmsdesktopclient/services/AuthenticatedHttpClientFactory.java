package com.ros.lmsdesktopclient.services;

import java.net.http.HttpClient;

@FunctionalInterface
public interface AuthenticatedHttpClientFactory {
    HttpClient create(String username, String password);
}
