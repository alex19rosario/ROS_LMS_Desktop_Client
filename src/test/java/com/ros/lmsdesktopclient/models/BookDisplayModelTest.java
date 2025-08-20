package com.ros.lmsdesktopclient.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookDisplayModelTest {

    private BookDisplayModel model;

    @BeforeEach
    void setUp() {
        model = new BookDisplayModel();
    }

    @Test
    void shouldInitializeWithDefaultValues() {
        assertEquals(0L, model.getId());
        assertNull(model.getIsbn());
        assertNull(model.getTitle());
        assertNull(model.getAuthors());
        assertNull(model.getGenres());
        assertNull(model.getStatus());
        assertNull(model.getImagePath());
    }

    @Test
    void shouldSetAndGetId() {
        model.setId(42L);
        assertEquals(42L, model.getId());
        assertEquals(42L, model.idProperty().get());
    }

    @Test
    void shouldSetAndGetIsbn() {
        model.setIsbn("978-3-16-148410-0");
        assertEquals("978-3-16-148410-0", model.getIsbn());
        assertEquals("978-3-16-148410-0", model.isbnProperty().get());
    }

    @Test
    void shouldSetAndGetTitle() {
        model.setTitle("Effective Java");
        assertEquals("Effective Java", model.getTitle());
        assertEquals("Effective Java", model.titleProperty().get());
    }

    @Test
    void shouldSetAndGetAuthors() {
        model.setAuthors("Joshua Bloch");
        assertEquals("Joshua Bloch", model.getAuthors());
        assertEquals("Joshua Bloch", model.authorsProperty().get());
    }

    @Test
    void shouldSetAndGetGenres() {
        model.setGenres("Programming, Java");
        assertEquals("Programming, Java", model.getGenres());
        assertEquals("Programming, Java", model.genresProperty().get());
    }

    @Test
    void shouldSetAndGetStatus() {
        model.setStatus("Available");
        assertEquals("Available", model.getStatus());
        assertEquals("Available", model.statusProperty().get());
    }

    @Test
    void shouldSetAndGetImagePath() {
        model.setImagePath("/images/java.png");
        assertEquals("/images/java.png", model.getImagePath());
        assertEquals("/images/java.png", model.imagePathProperty().get());
    }

    @Test
    void propertiesShouldStayInSyncWithGettersAndSetters() {
        // Update via property
        model.titleProperty().set("Clean Code");
        assertEquals("Clean Code", model.getTitle());

        // Update via setter
        model.setGenres("Software Engineering");
        assertEquals("Software Engineering", model.genresProperty().get());
    }

    @Test
    void toStringShouldContainAllFields() {
        model.setId(101L);
        model.setIsbn("12345");
        model.setTitle("Test Title");
        model.setAuthors("Author1, Author2");
        model.setGenres("Genre1, Genre2");
        model.setStatus("Borrowed");
        model.setImagePath("/path/to/image.png");

        String result = model.toString();

        assertTrue(result.contains("id"));
        assertTrue(result.contains("isbn"));
        assertTrue(result.contains("title"));
        assertTrue(result.contains("authors"));
        assertTrue(result.contains("genres"));
        assertTrue(result.contains("status"));
        assertTrue(result.contains("imagePath"));
    }
}
