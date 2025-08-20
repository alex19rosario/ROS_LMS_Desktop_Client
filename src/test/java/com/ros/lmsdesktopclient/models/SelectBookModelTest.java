package com.ros.lmsdesktopclient.models;

import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SelectBookModelTest {

    private SelectedBookModel model;

    @Mock
    private Image defaultImage;

    @BeforeEach
    void setUp() {
        model = new SelectedBookModel(defaultImage);
    }

    @Test
    void shouldInitializeWithDefaultValues() {
        assertEquals(defaultImage, model.getCoverImage());
        assertNull(model.getIsbn());
        assertNull(model.getTitle());
        assertNull(model.getAuthors());
        assertNull(model.getGenres());
        assertNull(model.getStatus());
    }

    @Test
    void shouldSetAndGetCoverImage() {
        Image newImage = mock(Image.class);
        model.setCoverImage(newImage);
        assertEquals(newImage, model.getCoverImage());
        assertEquals(newImage, model.coverImageProperty().get());
    }

    @Test
    void shouldSetAndGetIsbn() {
        model.setIsbn("12345");
        assertEquals("12345", model.getIsbn());
        assertEquals("12345", model.isbnProperty().get());
    }

    @Test
    void shouldSetAndGetTitle() {
        model.setTitle("Test Book");
        assertEquals("Test Book", model.getTitle());
        assertEquals("Test Book", model.titleProperty().get());
    }

    @Test
    void shouldSetAndGetAuthors() {
        model.setAuthors("John Doe");
        assertEquals("John Doe", model.getAuthors());
        assertEquals("John Doe", model.authorsProperty().get());
    }

    @Test
    void shouldSetAndGetGenres() {
        model.setGenres("Science, Math");
        assertEquals("Science, Math", model.getGenres());
        assertEquals("Science, Math", model.genresProperty().get());
    }

    @Test
    void shouldSetAndGetStatus() {
        model.setStatus("Available");
        assertEquals("Available", model.getStatus());
        assertEquals("Available", model.statusProperty().get());
    }

    @Test
    void toStringShouldContainAllFields() {
        model.setIsbn("12345");
        model.setTitle("Test Book");
        model.setAuthors("John Doe");
        model.setGenres("Science");
        model.setStatus("Available");

        String str = model.toString();

        assertTrue(str.contains("coverImage"));
        assertTrue(str.contains("isbn"));
        assertTrue(str.contains("title"));
        assertTrue(str.contains("authors"));
        assertTrue(str.contains("genres"));
        assertTrue(str.contains("status"));
    }

    @Test
    void clearShouldNotThrow() {
        // currently clear() is empty
        assertDoesNotThrow(model::clear);
    }

}
