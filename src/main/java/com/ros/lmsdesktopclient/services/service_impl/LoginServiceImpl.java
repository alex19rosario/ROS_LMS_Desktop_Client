package com.ros.lmsdesktopclient.services.service_impl;


import com.ros.lmsdesktopclient.models.LoginModel;
import com.ros.lmsdesktopclient.services.service.LoginService;
import com.ros.lmsdesktopclient.util.ApiUrls;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.exceptions.AuthenticationException;
import com.ros.lmsdesktopclient.util.exceptions.EmptyFieldsException;
import com.ros.lmsdesktopclient.util.exceptions.NetworkException;
import com.ros.lmsdesktopclient.util.exceptions.ServerErrorException;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginServiceImpl implements LoginService {

    private TokenHandler tokenHandler;

    public LoginServiceImpl(){
        tokenHandler = TokenHandler.getInstance();
    }

    @Override
    public void login(LoginModel loginModel, HttpClient client) throws EmptyFieldsException, NetworkException, ServerErrorException, AuthenticationException {
        try{
            checkForm(loginModel);
            checkNetwork();
            checkServer();
            // Create an HTTP client with Basic Authentication
            client = HttpClient.newBuilder()
                    .authenticator(new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    loginModel.getUsername(),
                                    loginModel.getPassword().toCharArray()
                            );
                        }
                    })
                    .build();

            // Build the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(ApiUrls.LOGIN.getUrl()))
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody()) // No body required for Basic Auth
                    .build();

            // Send the request and capture the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            tokenHandler.saveToken(response.body());

        } catch (InterruptedException | URISyntaxException | IOException e) {
            throw new AuthenticationException("Invalid credentials");
        }
    }

    private void checkForm(LoginModel loginModel){
        if(!loginModel.isComplete()){
            throw new EmptyFieldsException("Login form: there are empty fields");
        }
    }

    private void checkNetwork(){
        try {
            InetAddress address = InetAddress.getByName("8.8.8.8"); // Google's public DNS
            if (!address.isReachable(2000)) { // 2 seconds timeout
                throw new NetworkException("Network is not reachable");
            }
        } catch (IOException e) {
            throw new NetworkException("Failed to check network connectivity " + e);
        }
    }

    private void checkServer(){
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
