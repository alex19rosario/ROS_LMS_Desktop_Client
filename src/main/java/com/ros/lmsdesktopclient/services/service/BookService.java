package com.ros.lmsdesktopclient.services.service;

import com.ros.lmsdesktopclient.dtos.AddBookDTO;
import com.ros.lmsdesktopclient.util.exceptions.*;

import java.net.http.HttpClient;

public interface BookService {
    void addBook(AddBookDTO book) throws InvalidISBNException, NetworkException, ServerErrorException, ExpiredSessionException, BookAlreadyExistException;
}
