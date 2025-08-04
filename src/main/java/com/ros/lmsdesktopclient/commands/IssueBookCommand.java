package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.dtos.AddLoanDTO;
import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.services.service.LoanService;
import com.ros.lmsdesktopclient.util.*;
import com.ros.lmsdesktopclient.util.enums.*;
import com.ros.lmsdesktopclient.util.exceptions.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import javax.inject.Inject;
import java.util.Map;

public class IssueBookCommand extends Command {

    private final ObjectProperty<BookDisplayModel> selectedRowModel;
    private final StringProperty memberUsername;
    private final LoanService loanService;
    private final Command openLoginViewCommand;

    @Inject
    public IssueBookCommand(
            ObjectProperty<BookDisplayModel> selectedRowModel,
            Map<PropertyType, Property> properties,
            LoanService loanService
    ) {
        this.selectedRowModel = selectedRowModel;
        this.memberUsername = (StringProperty) properties.get(PropertyType.MEMBER_USERNAME);
        this.loanService = loanService;
        this.openLoginViewCommand = new OpenViewCommand(Views.LOGIN);
        this.setOnCommandSuccess(this::onSuccess);
        this.setOnCommandFailure(this::onFailure);
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws EmptyFieldsException, NetworkException, ServerErrorException, ExpiredSessionException, BookNotFoundException, BookNotAvailableException, MemberNotFoundException, MemberHasActiveLoanException, MemberHasOverdueLoanException {
                checkForm(selectedRowModel, memberUsername);
                checkIfBookAvailable(selectedRowModel);

                AddLoanDTO addLoanDTO = new AddLoanDTO(selectedRowModel.get().getId(), memberUsername.get(), TokenHandler.getInstance().getUsername());
                loanService.issueBook(addLoanDTO);
                return null;
            }
        };
    }

    private void onSuccess() {
        setAlert(Alerts.BOOK_ISSUED_SUCCESS);
        getAlert().getModal(AlertContents.BOOK_ISSUED_OK.getValue());
        //To reset the screen
        memberUsername.setValue("");

    }

    private void onFailure() {
        Throwable exception = getCommandTask().getException();

        Alerts alert = switch (exception){
            case EmptyFieldsException ignored -> Alerts.EMPTY_FIELDS_WARN;
            case NetworkException ignored -> Alerts.NETWORK_ERROR;
            case ServerErrorException ignored -> Alerts.SERVER_ERROR;
            case ExpiredSessionException ignored -> Alerts.EXPIRED_SESSION_ERROR;
            case BookNotFoundException ignored -> Alerts.BOOK_NOT_FOUND;
            case BookNotAvailableException ignored -> Alerts.BOOK_NOT_AVAILABLE;
            case MemberNotFoundException ignored -> Alerts.MEMBER_NOT_FOUND;
            case MemberHasActiveLoanException ignored -> Alerts.MEMBER_ACTIVE_LOAN;
            case MemberHasOverdueLoanException ignored -> Alerts.MEMBER_OVERDUE_LOAN;
            default -> throw new IllegalStateException("Unexpected exception: " + exception);
        };
        String content = exception.getMessage();
        setAlert(alert);
        getAlert().getModal(content);

        if(exception instanceof ExpiredSessionException){
            openLoginViewCommand.execute();
        }
    }

    private void checkForm(ObjectProperty<BookDisplayModel> selectedRowModel, StringProperty memberUsername) throws EmptyFieldsException {
        boolean isBookNull = selectedRowModel.get() == null;
        boolean isUsernameEmpty = memberUsername.get() == null || memberUsername.get().trim().isEmpty();

        if (isBookNull || isUsernameEmpty) {
            throw new EmptyFieldsException("You must select a book and enter a member username.");
        }
    }

    private void checkIfBookAvailable(ObjectProperty<BookDisplayModel> selectedRowModel) throws BookNotAvailableException {
        boolean isBookNotAvailable = selectedRowModel.get().statusProperty().get().equalsIgnoreCase(BookStatus.UNAVAILABLE.toString());

        if(isBookNotAvailable)
            throw new BookNotAvailableException("The selected book, with isbn: " + selectedRowModel.get().getIsbn() + ", is not available");
    }
}
