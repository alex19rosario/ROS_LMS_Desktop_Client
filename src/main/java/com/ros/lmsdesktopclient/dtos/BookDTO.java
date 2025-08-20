package com.ros.lmsdesktopclient.dtos;

import com.ros.lmsdesktopclient.util.enums.GenreType;

import java.util.Set;

/**
 *
 * @param id
 * @param isbn
 * @param title
 * @param authors
 * @param genres
 * @param status
 * @param imagePath
 */
public record BookDTO(
        long id,
        String isbn,
        String title,
        Set<AuthorDTO> authors,
        Set<GenreType> genres,
        boolean status,
        String imagePath
) {}
