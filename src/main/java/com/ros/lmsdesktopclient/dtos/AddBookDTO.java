package com.ros.lmsdesktopclient.dtos;

import java.util.Set;

public record AddBookDTO(String ISBN, String title, Set<String> genres, Set<AuthorDTO> authors) {

    @Override
    public String toString() {
        return "BookDTO{" +
                "ISBN='" + ISBN + '\'' +
                ", title='" + title + '\'' +
                ", genres=" + genres +
                ", authors=" + authors +
                '}';
    }
}
