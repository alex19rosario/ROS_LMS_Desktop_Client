package com.ros.lmsdesktopclient.dtos;

/**
 *
 * @param bookId
 * @param memberUsername
 * @param staffUsername
 */

public record AddLoanDTO(
        long bookId,
        String memberUsername,
        String staffUsername
) {}
