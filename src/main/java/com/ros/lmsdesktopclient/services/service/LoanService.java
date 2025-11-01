package com.ros.lmsdesktopclient.services.service;

import com.ros.lmsdesktopclient.dtos.AddLoanDTO;
import com.ros.lmsdesktopclient.dtos.ReturnBookDTO;
import com.ros.lmsdesktopclient.util.exceptions.*;

public interface LoanService {
    void issueBook(AddLoanDTO addLoanDTO) throws
            NetworkException,
            ServerErrorException,
            ExpiredSessionException,
            BookNotFoundException,
            BookNotAvailableException,
            MemberNotFoundException,
            MemberHasActiveLoanException,
            MemberHasOverdueLoanException;

    void returnBook(ReturnBookDTO returnBookDTO) throws
            NetworkException,
            ServerErrorException,
            ExpiredSessionException,
            BookNotRegisteredException,
            BookAlreadyInStockException;
}
