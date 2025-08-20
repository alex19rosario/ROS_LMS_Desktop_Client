package com.ros.lmsdesktopclient.dtos;

import java.time.LocalDate;

/**
 *
 * @param governmentID
 * @param firstName
 * @param lastName
 * @param phone
 * @param dateOfBirth
 * @param sex
 * @param email
 * @param username
 * @param password
 * @param staffUsername
 */
public record AddMemberDTO(String governmentID,
                           String firstName,
                           String lastName,
                           String phone,
                           LocalDate dateOfBirth,
                           String sex,
                           String email,
                           String username,
                           String password,
                           String staffUsername){}
