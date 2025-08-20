package com.ros.lmsdesktopclient.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginModelTest {

    private LoginModel loginModel;

    @BeforeEach
    void setUp() {
        loginModel = new LoginModel();
    }

    @Test
    void shouldInitializeWithEmptyUsernameAndPassword() {
        assertEquals("", loginModel.getUsername());
        assertEquals("", loginModel.getPassword());
    }

    @Test
    void shouldSetAndGetUsername() {
        loginModel.setUsername("john_doe");
        assertEquals("john_doe", loginModel.getUsername());
        assertEquals("john_doe", loginModel.usernameProperty().get());
    }

    @Test
    void shouldSetAndGetPassword() {
        loginModel.setPassword("secret123");
        assertEquals("secret123", loginModel.getPassword());
        assertEquals("secret123", loginModel.passwordProperty().get());
    }

    @Test
    void clearShouldResetUsernameAndPassword() {
        loginModel.setUsername("john_doe");
        loginModel.setPassword("secret123");

        loginModel.clear();

        assertEquals("", loginModel.getUsername());
        assertEquals("", loginModel.getPassword());
    }

    @Test
    void isCompleteShouldReturnFalseWhenUsernameIsEmpty() {
        loginModel.setUsername("");
        loginModel.setPassword("password");

        assertFalse(loginModel.isComplete());
    }

    @Test
    void isCompleteShouldReturnFalseWhenPasswordIsEmpty() {
        loginModel.setUsername("john_doe");
        loginModel.setPassword("");

        assertFalse(loginModel.isComplete());
    }

    @Test
    void isCompleteShouldReturnFalseWhenUsernameContainsSpaces() {
        loginModel.setUsername("john doe");
        loginModel.setPassword("password");

        assertFalse(loginModel.isComplete());
    }

    @Test
    void isCompleteShouldReturnTrueWhenUsernameAndPasswordAreValid() {
        loginModel.setUsername("john_doe");
        loginModel.setPassword("password");

        assertTrue(loginModel.isComplete());
    }

}
