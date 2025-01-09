package com.ros.lmsdesktopclient.services.service_impl;

import com.ros.lmsdesktopclient.services.service.GenreService;
import com.ros.lmsdesktopclient.util.Genres;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class GenreServiceImpl implements GenreService {
    @Override
    public Set<String> getAllGenres() {
        return Arrays.stream(Genres.values())
                .map(Genres::getStr)
                .sorted() // Sort the stream alphabetically
                .collect(Collectors.toCollection(java.util.LinkedHashSet::new)); // Maintain order in a Set
    }
}
