package com.ros.lmsdesktopclient.dtos;

import java.io.File;

/**
 *
 * @param ISBN
 * @param title
 * @param authors
 * @param genres
 * @param staffUsername
 * @param coverImage
 */
public record AddBookDTO(
        String ISBN,
        String title,
        String authors,
        String genres,
        String staffUsername,
        File coverImage
        ) {}
