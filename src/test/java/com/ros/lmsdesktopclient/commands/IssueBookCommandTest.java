package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.dtos.AddLoanDTO;
import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.services.service.LoanService;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.enums.AlertContents;
import com.ros.lmsdesktopclient.util.enums.Alerts;
import com.ros.lmsdesktopclient.util.enums.BookStatus;
import com.ros.lmsdesktopclient.util.enums.PropertyType;
import com.ros.lmsdesktopclient.util.exceptions.*;
import javafx.beans.property.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueBookCommandTest {
    @Mock
    private LoanService loanService;

    @Mock
    private ExecutorService executorService;

    @Mock
    private TokenHandler tokenHandler;

    @Mock
    private UiExecutor uiExecutor;

    @Mock
    private Command openLoginViewCommand;

    private ObjectProperty<BookDisplayModel> selectedRowModel;
    private StringProperty memberUsername;
    private IssueBookCommand command;

    @BeforeEach
    void setUp() {
        selectedRowModel = new SimpleObjectProperty<>();
        memberUsername = new SimpleStringProperty();

        Map<PropertyType, Property> properties = Map.of(
                PropertyType.MEMBER_USERNAME, memberUsername
        );

        command = new IssueBookCommand(
                selectedRowModel,
                properties,
                loanService,
                executorService,
                tokenHandler,
                uiExecutor,
                openLoginViewCommand
        );
    }

    @Test
    void runCommand_shouldIssueBookSuccessfully() throws Exception {
        // Arrange
        BookDisplayModel book = createTestBook(true);
        selectedRowModel.set(book);
        memberUsername.set("member1");
        when(tokenHandler.getUsername()).thenReturn("librarian");

        // Act
        command.runCommand();

        // Assert
        verify(loanService).issueBook(argThat(dto ->
                dto.bookIsbn().equalsIgnoreCase("9788489848") &&
                        dto.memberUsername().equals("member1") &&
                        dto.staffUsername().equals("librarian")
        ));
    }

    @Test
    void runCommand_shouldThrowEmptyFieldsExceptionWhenBookNotSelected() {
        // Arrange
        selectedRowModel.set(null);
        memberUsername.set("member1");

        // Act & Assert
        assertThrows(EmptyFieldsException.class, () -> command.runCommand());
        assertInstanceOf(EmptyFieldsException.class, command.getLastException());
    }

    @Test
    void runCommand_shouldThrowEmptyFieldsExceptionWhenUsernameEmpty() {
        // Arrange
        BookDisplayModel book = createTestBook(true);
        selectedRowModel.set(book);
        memberUsername.set("");

        // Act & Assert
        assertThrows(EmptyFieldsException.class, () -> command.runCommand());
        assertInstanceOf(EmptyFieldsException.class, command.getLastException());
    }

    @Test
    void runCommand_shouldThrowBookNotAvailableException() {
        // Arrange
        BookDisplayModel book = createTestBook(false);
        selectedRowModel.set(book);
        memberUsername.set("member1");

        // Act & Assert
        assertThrows(BookNotAvailableException.class, () -> command.runCommand());
        assertInstanceOf(BookNotAvailableException.class, command.getLastException());
    }

    @Test
    void runCommand_shouldStoreExceptionWhenLoanServiceFails() throws Exception {
        // Arrange
        BookDisplayModel book = createTestBook(true);
        selectedRowModel.set(book);
        memberUsername.set("member1");
        when(tokenHandler.getUsername()).thenReturn("librarian");

        doThrow(new NetworkException("Network error"))
                .when(loanService).issueBook(any(AddLoanDTO.class));

        // Act & Assert
        assertThrows(NetworkException.class, () -> command.runCommand());
        assertInstanceOf(NetworkException.class, command.getLastException());
    }

    @Test
    void onSuccess_shouldSetSuccessAlertAndResetForm() {
        // Arrange
        IssueBookCommand spyCommand = spy(command);
        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        // Act
        spyCommand.onSuccess();

        // Assert
        verify(spyCommand).setAlert(Alerts.BOOK_ISSUED_SUCCESS);
        verify(mockAlert).getModal(AlertContents.BOOK_ISSUED_OK.getValue());
        assertEquals("", memberUsername.get());
    }

    @Test
    void onFailure_shouldSetCorrectAlertForEachExceptionType() {
        // Test each exception type in separate test blocks
        testSingleExceptionAlert(new EmptyFieldsException("error"), Alerts.EMPTY_FIELDS_WARN);
        testSingleExceptionAlert(new NetworkException("error"), Alerts.NETWORK_ERROR);
        testSingleExceptionAlert(new ServerErrorException("error"), Alerts.SERVER_ERROR);
        testSingleExceptionAlert(new ExpiredSessionException("error"), Alerts.EXPIRED_SESSION_ERROR);
        testSingleExceptionAlert(new BookNotFoundException("error"), Alerts.BOOK_NOT_FOUND);
        testSingleExceptionAlert(new BookNotAvailableException("error"), Alerts.BOOK_NOT_AVAILABLE);
        testSingleExceptionAlert(new MemberNotFoundException("error"), Alerts.MEMBER_NOT_FOUND);
        testSingleExceptionAlert(new MemberHasActiveLoanException("error"), Alerts.MEMBER_ACTIVE_LOAN);
        testSingleExceptionAlert(new MemberHasOverdueLoanException("error"), Alerts.MEMBER_OVERDUE_LOAN);
    }

    @Test
    void onFailure_shouldOpenLoginViewForExpiredSession() {
        // Arrange
        IssueBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new ExpiredSessionException("session expired"));

        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        // Act
        spyCommand.onFailure();

        // Assert
        verify(openLoginViewCommand).execute();
    }

    @Test
    void onFailure_shouldThrowIllegalStateExceptionForUnexpectedException() {
        // Arrange
        command.setLastException(new RuntimeException("unexpected"));

        // Act & Assert
        assertThrows(IllegalStateException.class, command::onFailure);
    }

    private void testExceptionAlert(IssueBookCommand spyCommand, Exception exception, Alerts expectedAlert) {
        // Arrange
        spyCommand.setLastException(exception);
        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        // Act
        spyCommand.onFailure();

        // Assert
        verify(spyCommand).setAlert(expectedAlert);
        verify(mockAlert).getModal(exception.getMessage());

        // Reset for next test
        reset(mockAlert);
    }

    private BookDisplayModel createTestBook(boolean available) {
        BookDisplayModel book = new BookDisplayModel();
        book.setId(1L);
        book.setIsbn("9788489848");
        book.setTitle("Test Book");
        book.setStatus(available ? BookStatus.AVAILABLE.toString() : BookStatus.UNAVAILABLE.toString());
        return book;
    }

    private void testSingleExceptionAlert(Exception exception, Alerts expectedAlert) {
        // Arrange fresh mocks for each test
        IssueBookCommand spyCommand = spy(command);
        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();
        spyCommand.setLastException(exception);

        // Act
        spyCommand.onFailure();

        // Assert
        verify(spyCommand).setAlert(expectedAlert);
        verify(mockAlert).getModal(exception.getMessage());
    }
}
