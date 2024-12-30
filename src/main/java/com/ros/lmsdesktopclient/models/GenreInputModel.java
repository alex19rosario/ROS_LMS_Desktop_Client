package com.ros.lmsdesktopclient.models;

import javafx.scene.control.ComboBox;

public class GenreInputModel {

    private ComboBox<String> cbGenres;

    public GenreInputModel(){
        this.cbGenres = new ComboBox<>();
    }

    public ComboBox<String> getCbGenres() {
        return cbGenres;
    }

    public void setCbGenres(ComboBox<String> cbGenres) {
        this.cbGenres = cbGenres;
    }
}
