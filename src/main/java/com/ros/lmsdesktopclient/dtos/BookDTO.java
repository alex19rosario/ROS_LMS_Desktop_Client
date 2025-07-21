package com.ros.lmsdesktopclient.dtos;

import com.ros.lmsdesktopclient.util.GenreType;

import java.util.Set;

public record BookDTO(
        long id,
        String isbn,
        String title,
        Set<AuthorDTO> authors,
        Set<GenreType> genres,
        boolean status,
        String imagePath
) {

}
