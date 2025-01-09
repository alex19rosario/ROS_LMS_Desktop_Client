package com.ros.lmsdesktopclient.models;

import com.ros.lmsdesktopclient.dtos.GetAllGenresDTO;
import com.ros.lmsdesktopclient.util.Genres;
import javafx.scene.control.ComboBox;

import java.util.Arrays;
import java.util.Comparator;

public class GenreInputModel {

    private ComboBox<String> cbGenres;
    private final GetAllGenresDTO dto;

    public GenreInputModel(GetAllGenresDTO dto){
        this.cbGenres = new ComboBox<>();
        this.dto = dto;
        populateComboBox();
    }

    public ComboBox<String> getCbGenres() {
        return cbGenres;
    }

    public void setCbGenres(ComboBox<String> cbGenres) {
        this.cbGenres = cbGenres;
    }

    // Populate the ComboBox with the enum values
    private void populateComboBox() {
        dto.genres().stream()
                .sorted()
                .forEach(genre -> cbGenres.getItems().add(genre));
    }
}
