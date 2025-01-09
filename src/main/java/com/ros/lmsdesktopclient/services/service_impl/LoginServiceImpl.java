package com.ros.lmsdesktopclient.services.service_impl;


import com.ros.lmsdesktopclient.models.LoginModel;
import com.ros.lmsdesktopclient.services.service.GenreService;
import com.ros.lmsdesktopclient.services.service.LoginService;
import com.ros.lmsdesktopclient.util.ApiUrls;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.UpFrontDataHandler;
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

    private final TokenHandler tokenHandler;
    private final UpFrontDataHandler upFrontDataHandler;
    private final GenreService genreService;

    public LoginServiceImpl(){
        tokenHandler = TokenHandler.getInstance();
        upFrontDataHandler = UpFrontDataHandler.getInstance();
        genreService = new GenreServiceImpl();
    }

    @Override
    public void login(LoginModel loginModel, HttpClient client) throws EmptyFieldsException, NetworkException, ServerErrorException, AuthenticationException {
        try{
            checkForm(loginModel);
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
            upFrontDataHandler.saveGenres(genreService.getAllGenres(HttpClient.newHttpClient()));


        } catch (InterruptedException | URISyntaxException | IOException e) {
            throw new AuthenticationException("Invalid credentials");
        }
    }

    private void checkForm(LoginModel loginModel) throws EmptyFieldsException {
        if(!loginModel.isComplete()){
            throw new EmptyFieldsException("Login form: there are empty fields");
        }
    }
}
