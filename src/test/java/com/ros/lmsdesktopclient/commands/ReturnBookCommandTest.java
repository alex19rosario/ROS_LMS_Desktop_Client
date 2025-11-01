package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.dtos.ReturnBookDTO;
import com.ros.lmsdesktopclient.models.ReturnBookModel;
import com.ros.lmsdesktopclient.services.service.LoanService;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.enums.Alerts;
import com.ros.lmsdesktopclient.util.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReturnBookCommandTest {

    @Mock LoanService loanService;
    @Mock ExecutorService executorService;
    @Mock UiExecutor uiExecutor;
    @Mock TokenHandler tokenHandler;
    @Mock Command openLoginViewCommand;
    @Mock ReturnBookModel returnBookModel;

    ReturnBookCommand command;

    @BeforeEach
    void setUp() {
        command = new ReturnBookCommand(
                returnBookModel,
                loanService,
                executorService,
                tokenHandler,
                uiExecutor,
                openLoginViewCommand
        );
    }

    // --------------------------------------------------
    // runCommand() tests
    // --------------------------------------------------

    @Test
    void runCommand_shouldCallLoanServiceWithValidData() throws Exception {
        when(returnBookModel.isComplete()).thenReturn(true);
        when(returnBookModel.getIsbn()).thenReturn("1234567890123");
        when(tokenHandler.getUsername()).thenReturn("librarian");

        command.runCommand();

        verify(loanService).returnBook(any(ReturnBookDTO.class));
    }

    @Test
    void runCommand_withEmptyFields_shouldThrowEmptyFieldsException() {
        when(returnBookModel.isComplete()).thenReturn(false);
        assertThrows(EmptyFieldsException.class, command::runCommand);
    }

    @Test
    void runCommand_withInvalidIsbn_shouldThrowInvalidISBNException() {
        when(returnBookModel.isComplete()).thenReturn(true);
        when(returnBookModel.getIsbn()).thenReturn("invalidisbn");

        assertThrows(InvalidISBNException.class, command::runCommand);
    }

    @Test
    void runCommand_shouldStoreExceptionAndRethrow_whenLoanServiceFails() throws Exception {
        when(returnBookModel.isComplete()).thenReturn(true);
        when(returnBookModel.getIsbn()).thenReturn("1234567890123");
        when(tokenHandler.getUsername()).thenReturn("librarian");
        doThrow(new NetworkException("network down")).when(loanService).returnBook(any(ReturnBookDTO.class));

        assertThrows(NetworkException.class, command::runCommand);
        assertInstanceOf(NetworkException.class, command.getLastException());
    }

    // --------------------------------------------------
    // onSuccess() tests
    // --------------------------------------------------

    @Test
    void onSuccess_shouldClearFormAndShowSuccessAlert() {
        ReturnBookCommand spyCommand = spy(command);
        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onSuccess();

        verify(spyCommand).setAlert(Alerts.BOOK_RETURNED_SUCCESS);
        verify(alertMock).getModal("The book was returned successfully");
        verify(returnBookModel).clear();
    }

    // --------------------------------------------------
    // onFailure() tests
    // --------------------------------------------------

    @Test
    void onFailure_shouldShowEmptyFieldsAlert() {
        ReturnBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new EmptyFieldsException("Return form empty"));
        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.EMPTY_FIELDS_WARN);
        verify(mockAlert).getModal("Return form empty");
    }

    @Test
    void onFailure_shouldShowNetworkErrorAlert() {
        ReturnBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new NetworkException("no connection"));
        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.NETWORK_ERROR);
        verify(mockAlert).getModal("no connection");
    }

    @Test
    void onFailure_shouldShowServerErrorAlert() {
        ReturnBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new ServerErrorException("server down"));
        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.SERVER_ERROR);
        verify(mockAlert).getModal("server down");
    }

    @Test
    void onFailure_shouldShowInvalidISBNAlert() {
        ReturnBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new InvalidISBNException("invalid isbn"));
        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.INVALID_ISBN_ERROR);
        verify(mockAlert).getModal("invalid isbn");
    }

    @Test
    void onFailure_shouldShowExpiredSessionAlertAndOpenLoginView() {
        ReturnBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new ExpiredSessionException("session expired"));
        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.EXPIRED_SESSION_ERROR);
        verify(alertMock).getModal("session expired");
        verify(openLoginViewCommand).execute();
    }

    @Test
    void onFailure_shouldShowBookNotRegisteredAlert() {
        ReturnBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new BookNotRegisteredException("book not registered"));
        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.BOOK_NOT_REGISTERED);
        verify(mockAlert).getModal("book not registered");
    }

    @Test
    void onFailure_shouldShowBookAlreadyInStockAlert() {
        ReturnBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new BookAlreadyInStockException("already in stock"));
        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.BOOK_IN_STOCK);
        verify(mockAlert).getModal("already in stock");
    }

    @Test
    void onFailure_withUnexpectedException_shouldThrowIllegalStateException() {
        command.setLastException(new RuntimeException("unexpected"));
        assertThrows(IllegalStateException.class, command::onFailure);
    }
}
