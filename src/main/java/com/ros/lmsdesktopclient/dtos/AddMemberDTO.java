package com.ros.lmsdesktopclient.dtos;

public record AddMemberDTO(String governmentID, String firstName, String lastName, String phone, byte age, char sex, String email, String username, String password){
    @Override
    public String toString() {
        return "AddMemberDTO{" +
                "governmentID='" + governmentID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
