package com.ros.lmsdesktopclient.dtos;

import java.time.LocalDate;

public record AddMemberDTO(String governmentID,
                           String firstName,
                           String lastName,
                           String phone,
                           LocalDate dateOfBirth,
                           String sex,
                           String email,
                           String username,
                           String password,
                           String staffUsername){
    @Override
    public String toString() {
        return "AddMemberDTO{" +
                "governmentID='" + governmentID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", sex=" + sex +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", staffUsername='" + staffUsername + '\'' +
                '}';
    }
}
