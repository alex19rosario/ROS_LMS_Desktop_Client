package com.ros.lmsdesktopclient.util;

import com.ros.lmsdesktopclient.util.enums.ApiUrls;
import com.ros.lmsdesktopclient.util.exceptions.NetworkException;
import com.ros.lmsdesktopclient.util.exceptions.ServerErrorException;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DynamicInvocationHandler implements InvocationHandler {

    private final Object target;

    public DynamicInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Perform pre-checks
        checkNetwork();
        checkServer();
        // Invoke the actual method on the target
        try {
            // Invoke the actual method on the target
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            // Unwrap and re-throw the actual exception
            throw e.getCause();
        }
    }

    private void checkNetwork() throws NetworkException {
        try {
            HttpClient client = HttpClient.newBuilder()
                .connectTimeout(java.time.Duration.ofSeconds(2))
                .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.google.com"))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .timeout(java.time.Duration.ofSeconds(2))
                    .build();

            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() >= 400) {
                throw new NetworkException("Internet is not reachable. Status code: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            throw new NetworkException("No internet connection available");
        }
    }

    private void checkServer() throws ServerErrorException{
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(java.time.Duration.ofSeconds(2))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(ApiUrls.HEALTH_CHECK.getUrl()))
                    .GET()
                    .build();

            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            int statusCode = response.statusCode();

            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new ServerErrorException("Server is not reachable. Response code: " + statusCode);
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new ServerErrorException("Failed to check server availability " + e);
        }
    }


}
