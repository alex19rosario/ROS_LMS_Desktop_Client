package com.ros.lmsdesktopclient.dtos;

import java.io.File;

/**
 *
 * @param isbn
 * @param title
 * @param authors
 * @param genres
 * @param staffUsername
 * @param coverImage
 */
public record AddBookDTO(
        String isbn,
        String title,
        String authors,
        String genres,
        String staffUsername,
        File coverImage
        ) {}
