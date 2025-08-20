package com.ros.lmsdesktopclient.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MemberModelTest {

    private MemberModel member;

    @BeforeEach
    void setUp() {
        member = new MemberModel();
    }

    @Test
    void shouldInitializeWithDefaultValues() {
        assertEquals("", member.getGovernmentID());
        assertEquals("", member.getFirstName());
        assertEquals("", member.getLastName());
        assertEquals("", member.getPhone());
        assertNull(member.getDateOfBirth());
        assertEquals("", member.getSex());
        assertEquals("", member.getEmail());
        assertEquals("", member.getUsername());
        assertEquals("", member.getPassword());
        assertEquals("", member.getRepeatedPassword());
    }

    @Test
    void shouldSetAndGetAllProperties() {
        member.setGovernmentID("GOV123");
        member.setFirstName("John");
        member.setLastName("Doe");
        member.setPhone("1234567890");
        LocalDate dob = LocalDate.of(2000, 1, 1);
        member.setDateOfBirth(dob);
        member.setSex("M");
        member.setEmail("john.doe@example.com");
        member.setUsername("johndoe");
        member.setPassword("pass123");
        member.setRepeatedPassword("pass123");

        assertEquals("GOV123", member.getGovernmentID());
        assertEquals("John", member.getFirstName());
        assertEquals("Doe", member.getLastName());
        assertEquals("1234567890", member.getPhone());
        assertEquals(dob, member.getDateOfBirth());
        assertEquals("M", member.getSex());
        assertEquals("john.doe@example.com", member.getEmail());
        assertEquals("johndoe", member.getUsername());
        assertEquals("pass123", member.getPassword());
        assertEquals("pass123", member.getRepeatedPassword());
    }

    @Test
    void clearShouldResetAllFields() {
        member.setGovernmentID("GOV123");
        member.setFirstName("John");
        member.setLastName("Doe");
        member.setPhone("1234567890");
        member.setDateOfBirth(LocalDate.of(2000,1,1));
        member.setSex("M");
        member.setEmail("john.doe@example.com");
        member.setUsername("johndoe");
        member.setPassword("pass123");
        member.setRepeatedPassword("pass123");

        member.clear();

        assertEquals("", member.getGovernmentID());
        assertEquals("", member.getFirstName());
        assertEquals("", member.getLastName());
        assertEquals("", member.getPhone());
        assertNull(member.getDateOfBirth());
        assertEquals("", member.getSex());
        assertEquals("", member.getEmail());
        assertEquals("", member.getUsername());
        assertEquals("", member.getPassword());
        assertEquals("", member.getRepeatedPassword());
    }

    @Test
    void isCompleteShouldReturnFalseForEmptyFields() {
        assertFalse(member.isComplete());

        member.setGovernmentID("GOV 123"); // has space
        member.setFirstName("");
        member.setLastName("");
        member.setPhone("123 456"); // has space
        member.setDateOfBirth(null);
        member.setSex(" ");
        member.setEmail("");
        member.setUsername("");
        member.setPassword("");
        member.setRepeatedPassword("");

        assertFalse(member.isComplete());
    }

    @Test
    void isCompleteShouldReturnTrueForValidData() {
        member.setGovernmentID("GOV123");
        member.setFirstName("John");
        member.setLastName("Doe");
        member.setPhone("1234567890");
        member.setDateOfBirth(LocalDate.of(2000,1,1));
        member.setSex("M");
        member.setEmail("john.doe@example.com");
        member.setUsername("johndoe");
        member.setPassword("pass123");
        member.setRepeatedPassword("pass123");

        assertTrue(member.isComplete());
    }

    @Test
    void isCompleteShouldReturnFalseIfAnyFieldContainsSpace() {
        member.setGovernmentID("GOV123");
        member.setFirstName("John");
        member.setLastName("Doe");
        member.setPhone("123 456"); // space
        member.setDateOfBirth(LocalDate.of(2000,1,1));
        member.setSex("M");
        member.setEmail("john.doe@example.com");
        member.setUsername("johndoe");
        member.setPassword("pass123");
        member.setRepeatedPassword("pass123");

        assertFalse(member.isComplete());
    }
}
