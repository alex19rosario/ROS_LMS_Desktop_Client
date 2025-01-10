package com.ros.lmsdesktopclient.services.service_impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ros.lmsdesktopclient.services.service.GenreService;
import com.ros.lmsdesktopclient.util.ApiUrls;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.exceptions.AccessDeniedException;
import com.ros.lmsdesktopclient.util.exceptions.BookAlreadyExistException;
import com.ros.lmsdesktopclient.util.exceptions.ExpiredSessionException;
import com.ros.lmsdesktopclient.util.exceptions.ServerErrorException;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;
import java.util.function.Function;

public class GenreServiceImpl implements GenreService {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Set<String> getAllGenres(HttpClient client) throws AccessDeniedException {

        String token = TokenHandler.getInstance()
                .getToken()
                .orElseThrow(() -> new RuntimeException("Token is missing"));

        try {
            // Create HTTP GET Request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ApiUrls.GENRES.getUrl()))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            // Send Request and Handle Response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 403){
                throw new AccessDeniedException("The user does not have access to this resource");
            }
            return mapToSet.apply(response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Function to deserialize JSON string into a Set<String>
    private final Function<String, Set<String>> mapToSet = json -> {
        try {
            return mapper.readValue(json, new TypeReference<Set<String>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error while mapping JSON to Set<String>", e);
        }
    };
}
