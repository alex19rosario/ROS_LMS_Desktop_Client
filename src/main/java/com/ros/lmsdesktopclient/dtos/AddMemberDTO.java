package com.ros.lmsdesktopclient.dtos;

public record AddMemberDTO(String governmentID, String firstName, String lastName, String phone, byte age, char sex, String email, String username, String password, String repeatedPassword){
}
