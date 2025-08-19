package com.ros.lmsdesktopclient.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class BookModelTest {

    private BookModel model;
    @Mock private Image mockImage;

    @BeforeEach
    void setUp() {
        model = new BookModel(mockImage);
    }

    @Test
    void shouldInitializeWithDefaultValues() {
        assertEquals("", model.getIsbn());
        assertEquals("", model.getTitle());
        assertNotNull(model.getGenres());
        assertEquals(1, model.getGenres().size());
        assertEquals("SCIENCE", model.getGenres().getFirst().get());
        assertEquals(mockImage, model.getCoverImage());
        assertNull(model.getCoverImageFile());
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
    void shouldSetAndGetGenres() {
        ObservableList<StringProperty> genres = FXCollections.observableArrayList(
                new SimpleStringProperty("History"),
                new SimpleStringProperty("Math")
        );
        model.setGenres(genres);

        assertEquals(2, model.getGenres().size());
        assertEquals("History", model.getGenres().get(0).get());
        assertEquals("Math", model.getGenres().get(1).get());
    }

    @Test
    void shouldSetAndGetCoverImage() {
        Image anotherMock = mock(Image.class);
        model.setCoverImage(anotherMock);
        assertEquals(anotherMock, model.getCoverImage());
        assertEquals(anotherMock, model.coverImageProperty().get());
    }

    @Test
    void shouldSetAndGetCoverImageFile() {
        File file = new File("cover.png");
        model.setCoverImageFile(file);
        assertEquals(file, model.getCoverImageFile());
        assertEquals(file, model.coverImageFileProperty().get());
    }

    @Test
    void clearShouldResetValuesToDefaults() {
        model.setIsbn("12345");
        model.setTitle("Title");
        model.setGenres(FXCollections.observableArrayList(new SimpleStringProperty("Math")));
        model.setCoverImage(mock(Image.class));
        model.setCoverImageFile(new File("cover.png"));

        model.clear();

        assertEquals("", model.getIsbn());
        assertEquals("", model.getTitle());
        assertEquals(1, model.getGenres().size());
        assertEquals("SCIENCE", model.getGenres().getFirst().get());
        assertEquals(mockImage, model.getCoverImage()); // default mocked image restored
        assertNull(model.getCoverImageFile());
    }

    @Test
    void isCompleteShouldReturnFalseWhenIsbnEmpty() {
        model.setIsbn("");
        model.setTitle("Title");
        assertFalse(model.isComplete());
    }

    @Test
    void isCompleteShouldReturnFalseWhenIsbnContainsSpaces() {
        model.setIsbn("12 345");
        model.setTitle("Title");
        assertFalse(model.isComplete());
    }

    @Test
    void isCompleteShouldReturnFalseWhenTitleEmptyOrBlank() {
        model.setIsbn("12345");
        model.setTitle("");
        assertFalse(model.isComplete());

        model.setTitle("   ");
        assertFalse(model.isComplete());
    }

    @Test
    void isCompleteShouldReturnFalseWhenGenresContainEmpty() {
        model.setIsbn("12345");
        model.setTitle("Title");
        model.setGenres(FXCollections.observableArrayList(new SimpleStringProperty("")));
        assertFalse(model.isComplete());
    }

    @Test
    void isCompleteShouldReturnTrueWhenAllValid() {
        model.setIsbn("12345");
        model.setTitle("Valid Title");
        model.setGenres(FXCollections.observableArrayList(new SimpleStringProperty("History")));
        assertTrue(model.isComplete());
    }

    @Test
    void toStringShouldContainAllFields() {
        model.setIsbn("12345");
        model.setTitle("Title");
        model.setGenres(FXCollections.observableArrayList(new SimpleStringProperty("History")));
        model.setCoverImageFile(new File("cover.png"));

        String str = model.toString();
        assertTrue(str.contains("isbn"));
        assertTrue(str.contains("title"));
        assertTrue(str.contains("genres"));
        assertTrue(str.contains("coverImage"));
        assertTrue(str.contains("coverImageFile"));
    }

}
