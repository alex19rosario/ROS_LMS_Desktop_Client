package com.ros.lmsdesktopclient.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenreModelTest {

    @Test
    void constructorShouldInitializeFields() {
        GenreModel model = new GenreModel("Fantasy", true);

        assertEquals("Fantasy", model.getGenre());
        assertTrue(model.isSelected());
    }

    @Test
    void shouldSetAndGetGenre() {
        GenreModel model = new GenreModel("Fantasy", false);

        model.setGenre("Sci-Fi");

        assertEquals("Sci-Fi", model.getGenre());
        assertEquals("Sci-Fi", model.genreProperty().get());
    }

    @Test
    void shouldSetAndGetSelected() {
        GenreModel model = new GenreModel("Fantasy", false);

        model.setSelected(true);

        assertTrue(model.isSelected());
        assertTrue(model.selectedProperty().get());
    }

    @Test
    void propertiesShouldStayInSyncWithGettersAndSetters() {
        GenreModel model = new GenreModel("Mystery", false);

        // Update via property
        model.genreProperty().set("Thriller");
        model.selectedProperty().set(true);

        assertEquals("Thriller", model.getGenre());
        assertTrue(model.isSelected());

        // Update via setter
        model.setGenre("Drama");
        model.setSelected(false);

        assertEquals("Drama", model.genreProperty().get());
        assertFalse(model.selectedProperty().get());
    }
}
