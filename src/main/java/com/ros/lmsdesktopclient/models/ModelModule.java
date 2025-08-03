package com.ros.lmsdesktopclient.models;

import com.ros.lmsdesktopclient.util.GenreType;
import com.ros.lmsdesktopclient.util.UpFrontDataHandler;
import dagger.Module;
import dagger.Provides;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Module
public abstract class ModelModule {

    @Provides
    @Singleton
    static ListProperty<AuthorInputModel> authorInputs() {
        return new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    @Provides
    @Singleton
    static ListProperty<GenreInputModel> genreInputs() {
        return new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    @Provides
    @Singleton
    static List<AuthorModel> authors() {
        return new ArrayList<>();
    }

    @Provides
    static Set<GenreType> genres() {
        return UpFrontDataHandler.getInstance().getGenres().stream()
                .map(String::toUpperCase)
                .filter(name -> {
                    try {
                        GenreType.valueOf(name);
                        return true;
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                })
                .map(GenreType::valueOf)
                .collect(Collectors.toSet());
    }
}
