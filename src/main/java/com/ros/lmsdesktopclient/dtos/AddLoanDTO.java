package com.ros.lmsdesktopclient.dtos;

/**
 *
 * @param bookIsbn
 * @param memberUsername
 * @param staffUsername
 */

public record AddLoanDTO(
        String bookIsbn,
        String memberUsername,
        String staffUsername
) {}
