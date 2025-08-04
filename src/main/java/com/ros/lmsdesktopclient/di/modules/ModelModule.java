package com.ros.lmsdesktopclient.di.modules;

import com.ros.lmsdesktopclient.models.AuthorInputModel;
import com.ros.lmsdesktopclient.models.AuthorModel;
import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.models.GenreInputModel;
import com.ros.lmsdesktopclient.util.enums.GenreType;
import com.ros.lmsdesktopclient.util.enums.PropertyType;
import com.ros.lmsdesktopclient.util.PropertyTypeKey;
import com.ros.lmsdesktopclient.util.UpFrontDataHandler;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import javafx.beans.property.*;
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

    @Provides
    @Singleton
    @IntoMap
    @PropertyTypeKey(PropertyType.ISBN)
    static Property provideIsbnProperty() {
        return new SimpleStringProperty();
    }

    @Provides
    @Singleton
    @IntoMap
    @PropertyTypeKey(PropertyType.TOTAL_PAGES)
    static Property provideTotalPagesProperty() {
        return new SimpleIntegerProperty();
    }

    @Provides
    @Singleton
    @IntoMap
    @PropertyTypeKey(PropertyType.MEMBER_USERNAME)
    static Property provideMemberUsernameProperty() {
        return new SimpleStringProperty();
    }

    @Provides
    @Singleton
    static ListProperty<BookDisplayModel> bookDisplayModels() {
        return new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    @Provides
    @Singleton
    static ObjectProperty<BookDisplayModel> selectedRowModel() {
        return new SimpleObjectProperty<>();
    }
}
