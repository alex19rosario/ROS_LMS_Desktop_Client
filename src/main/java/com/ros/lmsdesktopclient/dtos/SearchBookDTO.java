package com.ros.lmsdesktopclient.dtos;

import com.ros.lmsdesktopclient.util.enums.GenreType;

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
) {}
