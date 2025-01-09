package com.ros.lmsdesktopclient.services.service;

import java.net.http.HttpClient;
import java.util.Set;

public interface GenreService {
    Set<String> getAllGenres(HttpClient client);
}
