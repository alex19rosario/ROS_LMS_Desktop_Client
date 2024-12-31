package com.ros.lmsdesktopclient.services.service;

import com.ros.lmsdesktopclient.dtos.BookDTO;
import com.ros.lmsdesktopclient.util.exceptions.*;

import java.net.http.HttpClient;

public interface AddBookService {
    void addBook(BookDTO book, HttpClient client) throws InvalidISBNException, NetworkException, ServerErrorException, ExpiredSessionException, BookAlreadyExistException;
    //Check fields
    //Check network
    //Check server
    //Add an exception for expired token
    //Clean the data
    //Convert the genres into a set to avoid repeated genres
    //Convert the authors into a set to avoid repeated authors
    //Everything should be in capital letters
    //Consume the service
}
