package com.ros.lmsdesktopclient.dtos;

/**
 *
 * @param firstName
 * @param lastName
 */
public record AuthorDTO(String firstName, String lastName) {
    @Override
    public String toString() {
        return "AuthorDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
