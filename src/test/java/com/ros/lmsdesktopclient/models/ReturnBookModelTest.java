package com.ros.lmsdesktopclient.models;

import javafx.beans.property.StringProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReturnBookModelTest {

    private ReturnBookModel model;

    @BeforeEach
    void setUp() {
        model = new ReturnBookModel();
    }

    @Test
    void shouldInitializeWithDefaultValues() {
        assertNotNull(model.isbnProperty());
        assertEquals("", model.getIsbn());
        assertEquals("", model.isbnProperty().get());
    }

    @Test
    void shouldSetAndGetIsbn() {
        model.setIsbn("9876543210");
        assertEquals("9876543210", model.getIsbn());
        assertEquals("9876543210", model.isbnProperty().get());
    }

    @Test
    void clearShouldResetIsbnToEmptyString() {
        model.setIsbn("9876543210");
        model.clear();
        assertEquals("", model.getIsbn());
        assertEquals("", model.isbnProperty().get());
    }

    @Test
    void isCompleteShouldReturnFalseWhenIsbnEmpty() {
        model.setIsbn("");
        assertFalse(model.isComplete());
    }

    @Test
    void isCompleteShouldReturnTrueWhenIsbnNotEmpty() {
        model.setIsbn("1234567890");
        assertTrue(model.isComplete());
    }

    @Test
    void isbnPropertyShouldBeBoundable() {
        StringProperty prop = model.isbnProperty();
        ReturnBookModel anotherModel = new ReturnBookModel();

        prop.bind(anotherModel.isbnProperty());
        anotherModel.setIsbn("BOUND_ISBN");

        assertEquals("BOUND_ISBN", model.getIsbn());
        prop.unbind();
    }
}
