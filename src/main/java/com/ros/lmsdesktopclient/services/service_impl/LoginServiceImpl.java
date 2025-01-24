package com.ros.lmsdesktopclient.services.service_impl;


import com.ros.lmsdesktopclient.models.LoginModel;
import com.ros.lmsdesktopclient.services.service.GenreService;
import com.ros.lmsdesktopclient.services.service.LoginService;
import com.ros.lmsdesktopclient.util.ApiUrls;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.UpFrontDataHandler;
import com.ros.lmsdesktopclient.util.exceptions.*;

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
    public void login(LoginModel loginModel) throws EmptyFieldsException, NetworkException, ServerErrorException, AuthenticationException, AccessDeniedException {
        try(HttpClient client = HttpClient.newBuilder()
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                loginModel.getUsername(),
                                loginModel.getPassword().toCharArray()
                        );
                    }
                })
                .build()){

            checkForm(loginModel);

            // Build the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(ApiUrls.LOGIN.getUrl()))
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody()) // No body required for Basic Auth
                    .build();

            // Send the request and capture the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            tokenHandler.saveToken(response.body());
            upFrontDataHandler.saveGenres(genreService.getAllGenres());

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
