package com.ros.lmsdesktopclient.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorModelTest {

    private AuthorModel authorModel;

    @BeforeEach
    void setUp() {
        authorModel = new AuthorModel();
    }

    @Test
    void shouldInitializeWithNullFirstAndLastName() {
        assertNull(authorModel.getFirstName());
        assertNull(authorModel.getLastName());
    }

    @Test
    void shouldSetAndGetFirstName() {
        authorModel.setFirstName("John");
        assertEquals("John", authorModel.getFirstName());
        assertEquals("John", authorModel.firstNameProperty().get());
    }

    @Test
    void shouldSetAndGetLastName() {
        authorModel.setLastName("Doe");
        assertEquals("Doe", authorModel.getLastName());
        assertEquals("Doe", authorModel.lastNameProperty().get());
    }

    @Test
    void clearShouldResetFirstAndLastNameToEmptyStrings() {
        authorModel.setFirstName("John");
        authorModel.setLastName("Doe");

        authorModel.clear();

        assertEquals("", authorModel.getFirstName());
        assertEquals("", authorModel.getLastName());
    }

    @Test
    void isCompleteShouldReturnFalseWhenFirstNameIsNull() {
        authorModel.setFirstName(null);
        authorModel.setLastName("Doe");

        assertFalse(authorModel.isComplete());
    }

    @Test
    void isCompleteShouldReturnFalseWhenLastNameIsNull() {
        authorModel.setFirstName("John");
        authorModel.setLastName(null);

        assertFalse(authorModel.isComplete());
    }

    @Test
    void isCompleteShouldReturnFalseWhenFirstNameIsEmpty() {
        authorModel.setFirstName("");
        authorModel.setLastName("Doe");

        assertFalse(authorModel.isComplete());
    }

    @Test
    void isCompleteShouldReturnFalseWhenLastNameIsEmpty() {
        authorModel.setFirstName("John");
        authorModel.setLastName("");

        assertFalse(authorModel.isComplete());
    }

    @Test
    void isCompleteShouldReturnTrueWhenBothNamesAreNonEmpty() {
        authorModel.setFirstName("John");
        authorModel.setLastName("Doe");

        assertTrue(authorModel.isComplete());
    }

    @Test
    void propertiesShouldStayInSyncWithGettersAndSetters() {
        // Set via property
        authorModel.firstNameProperty().set("Alice");
        authorModel.lastNameProperty().set("Smith");

        assertEquals("Alice", authorModel.getFirstName());
        assertEquals("Smith", authorModel.getLastName());

        // Set via setter
        authorModel.setFirstName("Bob");
        authorModel.setLastName("Johnson");

        assertEquals("Bob", authorModel.firstNameProperty().get());
        assertEquals("Johnson", authorModel.lastNameProperty().get());
    }
}
