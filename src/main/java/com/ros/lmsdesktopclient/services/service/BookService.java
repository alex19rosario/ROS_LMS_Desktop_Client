package com.ros.lmsdesktopclient.services.service;

import com.ros.lmsdesktopclient.dtos.AddBookDTO;
import com.ros.lmsdesktopclient.dtos.BookDTO;
import com.ros.lmsdesktopclient.dtos.SearchBookDTO;
import com.ros.lmsdesktopclient.util.exceptions.*;

import java.net.http.HttpClient;
import java.util.List;

public interface BookService {
    void addBook(AddBookDTO book) throws
            InvalidISBNException,
            NetworkException,
            ServerErrorException,
            ExpiredSessionException,
            BookAlreadyExistException;

    List<BookDTO> searchBooks(SearchBookDTO filter) throws
            NetworkException,
            ServerErrorException,
            ExpiredSessionException;
}
