package com.ros.lmsdesktopclient.models;

import com.ros.lmsdesktopclient.util.GenreType;
import javafx.scene.control.ComboBox;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Holds JavaFX {@link ComboBox} component used for genre selection in a UI form.
 *
 * <p>This model is meant for use in JavaFX desktop applications to represent
 * and manage the genre selection field, initializing it with a provided set of genres.</p>
 *
 * Fields:
 * <ul>
 *   <li><b>cbGenres</b>: JavaFX ComboBox component for selecting a genre.</li>
 *   <li><b>genres</b>: A set of strings representing the available genres to populate the ComboBox.</li>
 * </ul>
 */

public class GenreInputModel {

    private ComboBox<String> cbGenres;
    private final Set<String> genres;

    @Inject
    public GenreInputModel(Set<GenreType> genres){
        this.cbGenres = new ComboBox<>();
        this.genres = genres.stream()
                .map(GenreType::getStr)
                .collect(Collectors.toSet());
        populateComboBox();
    }

    public ComboBox<String> getCbGenres() {
        return cbGenres;
    }

    public void setCbGenres(ComboBox<String> cbGenres) {
        this.cbGenres = cbGenres;
    }

    // Populate the ComboBox
    private void populateComboBox() {
        genres.forEach(genre -> cbGenres.getItems().add(genre));
    }
}
