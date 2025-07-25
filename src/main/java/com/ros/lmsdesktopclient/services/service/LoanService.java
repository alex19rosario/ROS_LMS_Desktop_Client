package com.ros.lmsdesktopclient.services.service;

import com.ros.lmsdesktopclient.dtos.AddLoanDTO;
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
}
