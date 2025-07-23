package com.ros.lmsdesktopclient.dtos;

import java.util.List;

/**
 *
 * @param bookDTOList
 * @param totalPages
 * @param size
 */
public record PaginatedBooksDTO(
        List<BookDTO> bookDTOList,
        int totalPages,
        int size
) {

    @Override
    public String toString() {
        return "PaginatedBooksDTO{" +
                "bookDTOList=" + bookDTOList +
                ", totalPages=" + totalPages +
                ", size=" + size +
                '}';
    }
}
