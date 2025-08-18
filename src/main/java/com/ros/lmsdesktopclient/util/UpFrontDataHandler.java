package com.ros.lmsdesktopclient.util;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

@Singleton
public class UpFrontDataHandler {

    private static final String GENRES_KEY = "genres";
    private final Preferences preferences;

    public UpFrontDataHandler(){
        this.preferences = Preferences.userNodeForPackage(UpFrontDataHandler.class);
    }

//    private static class SingletonHelper{
//        private static final UpFrontDataHandler _upFrontDataHandler = new UpFrontDataHandler();
//    }
//
//    public static UpFrontDataHandler getInstance(){
//        return SingletonHelper._upFrontDataHandler;
//    }

    private final Function<Set<String>, String> getGenresStr = genres ->
            genres.stream()
                    .sorted()
                    .collect(Collectors.joining("|"));

    private final Function<String, Set<String>> getGenresSet = genresStr ->
            Arrays.stream(genresStr.split("\\|"))
                    .collect(Collectors.toCollection(LinkedHashSet::new));

    public void saveGenres(Set<String> genres){
        preferences.put(GENRES_KEY, getGenresStr.apply(genres));
    }

    public Set<String> getGenres(){
        return getGenresSet.apply(preferences.get(GENRES_KEY, null));
    }

}
