package com.ros.lmsdesktopclient.services;

import com.ros.lmsdesktopclient.dtos.BookDTO;
import com.ros.lmsdesktopclient.util.exceptions.EmptyFieldsException;
import com.ros.lmsdesktopclient.util.exceptions.ExpiredSessionException;
import com.ros.lmsdesktopclient.util.exceptions.NetworkException;
import com.ros.lmsdesktopclient.util.exceptions.ServerErrorException;

import java.net.http.HttpClient;

public class AddBookServiceImpl implements AddBookService{
    @Override
    public void addBook(BookDTO book, HttpClient client) throws EmptyFieldsException, NetworkException, ServerErrorException, ExpiredSessionException {
        System.out.println(book);
    }
}
