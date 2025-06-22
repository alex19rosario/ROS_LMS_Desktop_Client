package com.ros.lmsdesktopclient.dtos;

import java.io.File;
import java.util.Set;

public record AddBookDTO(
        long ISBN,
        String title,
        String authors,
        String genres,
        File coverImage
        ) {

    @Override
    public String toString() {
        return "AddBookDTO{" +
                "ISBN=" + ISBN +
                ", title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", genres='" + genres + '\'' +
                ", coverImage=" + coverImage +
                '}';
    }
}
