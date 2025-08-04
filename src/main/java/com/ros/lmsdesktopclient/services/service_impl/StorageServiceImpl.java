package com.ros.lmsdesktopclient.services.service_impl;

import com.ros.lmsdesktopclient.services.service.StorageService;
import com.ros.lmsdesktopclient.util.enums.ApiUrls;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.exceptions.ExpiredSessionException;
import com.ros.lmsdesktopclient.util.exceptions.ImageNotFoundException;
import com.ros.lmsdesktopclient.util.exceptions.NetworkException;
import com.ros.lmsdesktopclient.util.exceptions.ServerErrorException;
import javafx.scene.image.Image;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class StorageServiceImpl implements StorageService {

    private final HttpClient client;

    @Inject
    public StorageServiceImpl(HttpClient client) {
        this.client = client;
    }

    @Override
    public Image getCoverImage(String filename) throws NetworkException, ServerErrorException, ExpiredSessionException, ImageNotFoundException, IOException {
        try {

            String token = TokenHandler.getInstance().getToken()
                    .orElseThrow(() -> new ExpiredSessionException("No token found. Please log in again."));

            String imageUrl = ApiUrls.IMAGES.getUrl() + filename;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(imageUrl))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            return switch (response.statusCode()) {
                case 200 -> new Image(new ByteArrayInputStream(response.body()));
                case 404, 403 -> throw new ImageNotFoundException("Cover image " + filename + " not found on the server.");
                case 401 -> throw new ExpiredSessionException("Session expired. Please log in again.");
                default -> throw new ServerErrorException("Server returned unexpected status: " + response.statusCode());
            };

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new NetworkException("Request interrupted. " + e.getMessage());
        } catch (IOException e) {
            throw new IOException("Failed to read image data. " + e.getMessage());
        }
    }
}
