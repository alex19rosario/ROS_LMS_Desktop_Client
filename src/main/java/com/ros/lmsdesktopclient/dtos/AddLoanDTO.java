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
) {

    @Override
    public String toString() {
        return "AddLoanDTO{" +
                "bookId=" + bookId +
                ", memberUsername='" + memberUsername + '\'' +
                ", staffUsername='" + staffUsername + '\'' +
                '}';
    }
}
