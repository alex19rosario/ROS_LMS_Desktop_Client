package com.ros.lmsdesktopclient.dtos;

import com.ros.lmsdesktopclient.util.GenreType;

/**
 *
 * @param page
 * @param size
 * @param title
 * @param genre
 * @param authorFirstName
 * @param authorLastName
 * @param isAvailable
 */
public record SearchBookDTO(
        int page,
        int size,
        String title,
        GenreType genre,
        String authorFirstName,
        String authorLastName,
        Boolean isAvailable
) {

    @Override
    public String toString() {
        return "SearchBookDTO{" +
                "page=" + page +
                ", size=" + size +
                ", title='" + title + '\'' +
                ", genre=" + genre +
                ", authorFirstName='" + authorFirstName + '\'' +
                ", authorLastName='" + authorLastName + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
