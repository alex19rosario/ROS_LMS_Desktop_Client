package com.ros.lmsdesktopclient.util;

import com.ros.lmsdesktopclient.util.exceptions.NetworkException;
import com.ros.lmsdesktopclient.util.exceptions.ServerErrorException;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
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

    private void checkNetwork() throws NetworkException{
        try {
            InetAddress address = InetAddress.getByName("8.8.8.8"); // Google's public DNS
            if (!address.isReachable(2000)) { // 2 seconds timeout
                throw new NetworkException("Network is not reachable");
            }
        } catch (IOException e) {
            throw new NetworkException("Failed to check network connectivity " + e);
        }
    }

    private void checkServer() throws ServerErrorException{
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(java.time.Duration.ofSeconds(2))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(ApiUrls.LOGIN.getUrl()))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            int statusCode = response.statusCode();

            if (statusCode != HttpURLConnection.HTTP_OK && statusCode != HttpURLConnection.HTTP_UNAUTHORIZED) {
                throw new ServerErrorException("Server is not reachable. Response code: " + statusCode);
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new ServerErrorException("Failed to check server availability " + e);
        }
    }


}
