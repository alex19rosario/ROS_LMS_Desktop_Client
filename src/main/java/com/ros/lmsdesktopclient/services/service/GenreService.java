package com.ros.lmsdesktopclient.services.service;

import com.ros.lmsdesktopclient.util.exceptions.AccessDeniedException;

import java.net.http.HttpClient;
import java.util.Set;

public interface GenreService {
    Set<String> getAllGenres() throws AccessDeniedException;
}
