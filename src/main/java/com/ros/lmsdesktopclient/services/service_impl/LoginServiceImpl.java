package com.ros.lmsdesktopclient.services.service_impl;


import com.ros.lmsdesktopclient.dtos.LoginDTO;
import com.ros.lmsdesktopclient.di.factories.AuthenticatedHttpClientFactory;
import com.ros.lmsdesktopclient.services.service.GenreService;
import com.ros.lmsdesktopclient.services.service.LoginService;
import com.ros.lmsdesktopclient.util.enums.ApiUrls;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.UpFrontDataHandler;
import com.ros.lmsdesktopclient.util.exceptions.*;

import javax.inject.Inject;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginServiceImpl implements LoginService {

    private final TokenHandler tokenHandler;
    private final UpFrontDataHandler upFrontDataHandler;
    private final GenreService genreService;
    private final AuthenticatedHttpClientFactory clientFactory;

    @Inject
    public LoginServiceImpl(AuthenticatedHttpClientFactory clientFactory, GenreService genreService){
        tokenHandler = TokenHandler.getInstance();
        upFrontDataHandler = UpFrontDataHandler.getInstance();
        this.genreService = genreService;
        this.clientFactory = clientFactory;
    }

    @Override
    public void login(LoginDTO loginDTO) throws NetworkException, ServerErrorException, AuthenticationException, AccessDeniedException {
        try{
            HttpClient client = clientFactory.create(loginDTO.username(), loginDTO.password());

            // Build the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(ApiUrls.LOGIN.getUrl()))
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody()) // No body required for Basic Auth
                    .build();

            // Send the request and capture the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            tokenHandler.saveToken(response.body());

            if (tokenHandler.getToken().isEmpty())
                throw new RuntimeException("Token was not saved properly!");
            
            upFrontDataHandler.saveGenres(genreService.getAllGenres());

        } catch (InterruptedException | URISyntaxException | IOException e) {
            throw new AuthenticationException("The username or password are incorrect.");
        }
    }


}
