package com.ros.lmsdesktopclient.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchBookModelTest {

    private SearchBookModel model;

    @BeforeEach
    void setUp() {
        model = new SearchBookModel();
    }

    @Test
    void shouldInitializeWithDefaultValues() {
        assertEquals(0, model.getPage());
        assertEquals(10, model.getSize()); // default PAGE_SIZE
        assertNull(model.getTitle());
        assertNull(model.getAuthorFirstName());
        assertNull(model.getAuthorLastName());
        assertNull(model.getGenre());
        assertNull(model.getStatus());
    }

    @Test
    void shouldSetAndGetPage() {
        model.setPage(3);
        assertEquals(3, model.getPage());
        assertEquals(3, model.pageProperty().get());
    }

    @Test
    void shouldSetAndGetSize() {
        model.setSize(25);
        assertEquals(25, model.getSize());
        assertEquals(25, model.sizeProperty().get());
    }

    @Test
    void shouldSetAndGetTitle() {
        model.setTitle("Java Programming");
        assertEquals("Java Programming", model.getTitle());
        assertEquals("Java Programming", model.titleProperty().get());
    }

    @Test
    void shouldSetAndGetAuthorFirstName() {
        model.setAuthorFirstName("John");
        assertEquals("John", model.getAuthorFirstName());
        assertEquals("John", model.authorFirstNameProperty().get());
    }

    @Test
    void shouldSetAndGetAuthorLastName() {
        model.setAuthorLastName("Doe");
        assertEquals("Doe", model.getAuthorLastName());
        assertEquals("Doe", model.authorLastNameProperty().get());
    }

    @Test
    void shouldSetAndGetGenre() {
        model.setGenre("Science");
        assertEquals("Science", model.getGenre());
        assertEquals("Science", model.genreProperty().get());
    }

    @Test
    void shouldSetAndGetStatus() {
        model.setStatus("Available");
        assertEquals("Available", model.getStatus());
        assertEquals("Available", model.statusProperty().get());
    }

    @Test
    void clearShouldResetAllFilters() {
        model.setTitle("Java");
        model.setAuthorFirstName("Jane");
        model.setAuthorLastName("Smith");
        model.setGenre("Math");
        model.setStatus("Loaned");

        model.clear();

        assertEquals("", model.getTitle());
        assertEquals("", model.getAuthorFirstName());
        assertEquals("", model.getAuthorLastName());
        assertNull(model.getGenre());
        assertNull(model.getStatus());
    }

    @Test
    void isCompleteShouldReturnFalseWhenAllEmpty() {
        model.clear();
        assertFalse(model.isComplete());
    }

    @Test
    void isCompleteShouldReturnTrueWhenAnyFieldIsNotEmpty() {
        model.setTitle("Java");
        assertTrue(model.isComplete());

        model.clear();
        model.setAuthorFirstName("John");
        assertTrue(model.isComplete());

        model.clear();
        model.setAuthorLastName("Doe");
        assertTrue(model.isComplete());

        model.clear();
        model.setGenre("Science");
        assertTrue(model.isComplete());

        model.clear();
        model.setStatus("Available");
        assertTrue(model.isComplete());
    }

    @Test
    void toStringShouldContainAllFields() {
        model.setPage(1);
        model.setSize(20);
        model.setTitle("Java");
        model.setAuthorFirstName("John");
        model.setAuthorLastName("Doe");
        model.setGenre("Science");
        model.setStatus("Available");

        String str = model.toString();

        assertTrue(str.contains("page"));
        assertTrue(str.contains("size"));
        assertTrue(str.contains("title"));
        assertTrue(str.contains("authorFirstName"));
        assertTrue(str.contains("authorLastName"));
        assertTrue(str.contains("genre"));
        assertTrue(str.contains("status"));
    }
}
