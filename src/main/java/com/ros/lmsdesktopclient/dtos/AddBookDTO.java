package com.ros.lmsdesktopclient.dtos;

import java.io.File;

public record AddBookDTO(
        String ISBN,
        String title,
        String authors,
        String genres,
        String staffUsername,
        File coverImage
        ) {

    @Override
    public String toString() {
        return "AddBookDTO{" +
                "ISBN=" + ISBN +
                ", title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", genres='" + genres + '\'' +
                ", staffUsername='" + staffUsername + '\'' +
                ", coverImage=" + coverImage +
                '}';
    }
}
