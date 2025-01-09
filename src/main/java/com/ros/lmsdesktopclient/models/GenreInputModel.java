package com.ros.lmsdesktopclient.models;

import javafx.scene.control.ComboBox;

import java.util.Set;

public class GenreInputModel {

    private ComboBox<String> cbGenres;
    private final Set<String> genres;

    public GenreInputModel(Set<String> genres){
        this.cbGenres = new ComboBox<>();
        this.genres = genres;
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
        genres.stream()
                .sorted()
                .forEach(genre -> cbGenres.getItems().add(genre));
    }
}
