package com.ros.lmsdesktopclient.services.service;

import com.ros.lmsdesktopclient.dtos.AddBookDTO;
import com.ros.lmsdesktopclient.util.exceptions.*;

import java.net.http.HttpClient;

public interface AddBookService {
    void addBook(AddBookDTO book, HttpClient client) throws InvalidISBNException, NetworkException, ServerErrorException, ExpiredSessionException, BookAlreadyExistException;
}
