package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.dtos.ReturnBookDTO;
import com.ros.lmsdesktopclient.models.ReturnBookModel;
import com.ros.lmsdesktopclient.services.service.LoanService;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.annotations.LoginCommandQualifier;
import com.ros.lmsdesktopclient.util.enums.AlertContents;
import com.ros.lmsdesktopclient.util.enums.Alerts;
import com.ros.lmsdesktopclient.util.exceptions.*;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

public class ReturnBookCommand extends Command {

    private final ReturnBookModel returnBookModel;
    private final LoanService loanService;
    private final TokenHandler tokenHandler;
    private  final Command openLoginViewCommand;
    private Throwable lastException;

    @Inject
    public ReturnBookCommand(
            ReturnBookModel returnBookModel,
            LoanService loanService,
            ExecutorService executorService,
            TokenHandler tokenHandler,
            UiExecutor uiExecutor,
            @LoginCommandQualifier Command openLoginViewCommand
    ) {
        super(executorService, uiExecutor);
        this.returnBookModel = returnBookModel;
        this.loanService = loanService;
        this.tokenHandler = tokenHandler;
        this.openLoginViewCommand = openLoginViewCommand;
        setOnCommandSuccess(this::onSuccess);
        setOnCommandFailure(this::onFailure);
    }

    @Override
    protected void runCommand() throws Exception {
        try {
            checkForm(returnBookModel);
            ReturnBookDTO dto = new ReturnBookDTO(returnBookModel.getIsbn(), tokenHandler.getUsername());
            loanService.returnBook(dto);
        } catch (Exception ex) {
            this.lastException = ex;
            throw ex; // triggers failure in base class
        }
    }

    void onSuccess() {
        setAlert(Alerts.BOOK_RETURNED_SUCCESS);
        getAlert().getModal(AlertContents.BOOK_RETURNED_OK.getValue());
        //reset scree
        returnBookModel.clear();
    }

    void onFailure() {
        if(lastException != null) {
            Alerts alert = switch (lastException) {
                case EmptyFieldsException ignored -> Alerts.EMPTY_FIELDS_WARN;
                case NetworkException ignored -> Alerts.NETWORK_ERROR;
                case ServerErrorException ignored -> Alerts.SERVER_ERROR;
                case InvalidISBNException ignored -> Alerts.INVALID_ISBN_ERROR;
                case ExpiredSessionException ignored -> Alerts.EXPIRED_SESSION_ERROR;
                case BookNotRegisteredException ignore -> Alerts.BOOK_NOT_REGISTERED;
                case BookAlreadyInStockException ignore -> Alerts.BOOK_IN_STOCK;
                default -> throw  new IllegalStateException("Unexpected exception: " + lastException);
            };

            setAlert(alert);
            getAlert().getModal(lastException.getMessage());
        }

        if(lastException instanceof ExpiredSessionException) {
            openLoginViewCommand.execute();
        }
    }

    private void checkForm(ReturnBookModel returnBookModel) throws EmptyFieldsException, InvalidISBNException{
        if (!returnBookModel.isComplete()) {
            throw new EmptyFieldsException("Return Book Form: the field is empty");
        }

        checkIsbn(returnBookModel.getIsbn());
    }

    private void checkIsbn(String isbn) throws InvalidISBNException {
        // Regex for a valid ISBN-10 or ISBN-13
        String isbnRegex = "^(\\d{10}|\\d{13})$";

        // Check if the book's ISBN matches the regex
        if (isbn == null || !isbn.matches(isbnRegex)) {
            throw new InvalidISBNException("Invalid ISBN: The specified ISBN does not have the correct format.");
        }
    }

    public Throwable getLastException() {
        return lastException;
    }

    public void setLastException(Throwable lastException) {
        this.lastException = lastException;
    }
}
