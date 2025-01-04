package com.ros.lmsdesktopclient.services.service_impl;

import com.ros.lmsdesktopclient.dtos.AddBookDTO;
import com.ros.lmsdesktopclient.services.service.AddBookService;
import com.ros.lmsdesktopclient.util.exceptions.*;

import java.net.http.HttpClient;

public class AddBookServiceImpl implements AddBookService {

    @Override
    public void addBook(AddBookDTO book, HttpClient client) throws InvalidISBNException, NetworkException, ServerErrorException, ExpiredSessionException, BookAlreadyExistException {

        checkISBN(book);
        System.out.println("Adding book:\n" + book);
    }

    private void checkISBN(AddBookDTO book) throws InvalidISBNException {
        // Regex for a valid ISBN-10 or ISBN-13
        String isbnRegex = "^(\\d{10}|\\d{13})$";

        // Check if the book's ISBN matches the regex
        if (book == null || book.ISBN() == null || !book.ISBN().matches(isbnRegex)) {
            throw new InvalidISBNException("Invalid ISBN: The specified ISBN does not have the correct format.");
        }
    }
}
