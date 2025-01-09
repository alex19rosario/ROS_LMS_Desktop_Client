package com.ros.lmsdesktopclient.util;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class UpFrontDataHandler {

    private static final String GENRES_KEY = "genres";
    private final Preferences preferences;

    private UpFrontDataHandler(){
        this.preferences = Preferences.userNodeForPackage(UpFrontDataHandler.class);
    }

    private static class SingletonHelper{
        private static final UpFrontDataHandler _upFrontDataHandler = new UpFrontDataHandler();
    }

    public static UpFrontDataHandler getInstance(){
        return SingletonHelper._upFrontDataHandler;
    }

    private final Function<Set<String>, String> getGenresStr = genres ->
            genres.stream()
                    .collect(Collectors.joining(" "));

    private final Function<String, Set<String>> getGenresSet = genresStr ->
            Arrays.stream(genresStr.split(" "))
                    .collect(Collectors.toSet());

    public void saveGenres(Set<String> genres){
        preferences.put(GENRES_KEY, getGenresStr.apply(genres));
    }

    public Set<String> getGenres(){
        return getGenresSet.apply(preferences.get(GENRES_KEY, null));
    }

}
