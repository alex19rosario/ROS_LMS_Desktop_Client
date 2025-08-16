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

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class IssueBookCommand extends Command {

    private final ObjectProperty<BookDisplayModel> selectedRowModel;
    private final StringProperty memberUsername;
    private final LoanService loanService;
    private final TokenHandler tokenHandler;
    private final Command openLoginViewCommand;
    private Throwable lastException;

    @Inject
    public IssueBookCommand(
            ObjectProperty<BookDisplayModel> selectedRowModel,
            Map<PropertyType, Property> properties,
            LoanService loanService,
            ExecutorService executorService,
            ViewHandler viewHandler,
            TokenHandler tokenHandler
    ) {
        super(executorService);
        this.selectedRowModel = selectedRowModel;
        this.memberUsername = (StringProperty) properties.get(PropertyType.MEMBER_USERNAME);
        this.loanService = loanService;
        this.tokenHandler = tokenHandler;
        this.openLoginViewCommand = new OpenViewCommand(ViewType.LOGIN, executorService, viewHandler);
        this.setOnCommandSuccess(this::onSuccess);
        this.setOnCommandFailure(this::onFailure);
    }

    @Override
    protected void runCommand() throws Exception {
        try {
            checkForm(selectedRowModel, memberUsername);
            checkIfBookAvailable(selectedRowModel);

            AddLoanDTO addLoanDTO = new AddLoanDTO(selectedRowModel.get().getId(), memberUsername.get(), tokenHandler.getUsername());
            loanService.issueBook(addLoanDTO);
        } catch (Exception ex) {
            this.lastException = ex;
            throw ex; // triggers failure in base class
        }
    }

    private void onSuccess() {
        setAlert(Alerts.BOOK_ISSUED_SUCCESS);
        getAlert().getModal(AlertContents.BOOK_ISSUED_OK.getValue());
        //To reset the screen
        memberUsername.setValue("");

    }

    private void onFailure() {

        if(lastException != null) {
            Alerts alert = switch (lastException){
                case EmptyFieldsException ignored -> Alerts.EMPTY_FIELDS_WARN;
                case NetworkException ignored -> Alerts.NETWORK_ERROR;
                case ServerErrorException ignored -> Alerts.SERVER_ERROR;
                case ExpiredSessionException ignored -> Alerts.EXPIRED_SESSION_ERROR;
                case BookNotFoundException ignored -> Alerts.BOOK_NOT_FOUND;
                case BookNotAvailableException ignored -> Alerts.BOOK_NOT_AVAILABLE;
                case MemberNotFoundException ignored -> Alerts.MEMBER_NOT_FOUND;
                case MemberHasActiveLoanException ignored -> Alerts.MEMBER_ACTIVE_LOAN;
                case MemberHasOverdueLoanException ignored -> Alerts.MEMBER_OVERDUE_LOAN;
                default -> throw new IllegalStateException("Unexpected exception: " + lastException);
            };
            setAlert(alert);
            getAlert().getModal(lastException.getMessage());
        }

        if(lastException instanceof ExpiredSessionException){
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
