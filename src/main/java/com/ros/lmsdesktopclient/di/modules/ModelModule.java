package com.ros.lmsdesktopclient.di.modules;

import com.ros.lmsdesktopclient.models.AuthorModel;
import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.models.GenreModel;
import com.ros.lmsdesktopclient.util.enums.PropertyType;
import com.ros.lmsdesktopclient.util.annotations.PropertyTypeKey;
import com.ros.lmsdesktopclient.util.UpFrontDataHandler;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.inject.Singleton;
import java.util.List;

@Module
public abstract class ModelModule {

    @Provides
    @Singleton
    static ObservableList<GenreModel> genreModelObservableList(UpFrontDataHandler upFrontDataHandler) {

        List<GenreModel> list = upFrontDataHandler.getGenres().stream()
                .map(genre -> new GenreModel(genre, false))
                .toList();

        return FXCollections.observableArrayList(list);
    }

    @Provides
    @Singleton
    static ListProperty<AuthorModel> authorModelListProperty() {
        return new SimpleListProperty<>(FXCollections.observableArrayList());
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
