package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.dtos.AddMemberDTO;
import com.ros.lmsdesktopclient.models.MemberModel;
import com.ros.lmsdesktopclient.services.service.MemberService;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.enums.Alerts;
import com.ros.lmsdesktopclient.util.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddMemberCommandTest {

    @Mock MemberService memberService;
    @Mock ExecutorService executorService;
    @Mock TokenHandler tokenHandler;
    @Mock Command openLoginViewCommand;
    @Mock MemberModel memberModel;
    @Mock UiExecutor uiExecutor;

    AddMemberCommand command;

    @BeforeEach
    void setup() {
        command = new AddMemberCommand(
                memberModel,
                memberService,
                executorService,
                tokenHandler,
                uiExecutor,
                openLoginViewCommand
        );
    }

    @Test
    void runCommand_shouldCallMemberServiceWithValidData() throws Exception {
        when(memberModel.isComplete()).thenReturn(true);
        when(memberModel.getGovernmentID()).thenReturn("1234567");
        when(memberModel.getFirstName()).thenReturn("John");
        when(memberModel.getLastName()).thenReturn("Doe");
        when(memberModel.getPhone()).thenReturn("1234567890");
        when(memberModel.getEmail()).thenReturn("john@example.com");
        when(memberModel.getPassword()).thenReturn("secret");
        when(memberModel.getRepeatedPassword()).thenReturn("secret");
        when(memberModel.getDateOfBirth()).thenReturn(LocalDate.of(2000, 1, 1));
        when(memberModel.getSex()).thenReturn("M");
        when(memberModel.getUsername()).thenReturn("johndoe");
        when(tokenHandler.getUsername()).thenReturn("admin");

        command.runCommand();

        verify(memberService).addMember(any(AddMemberDTO.class));
    }

    @Test
    void runCommand_withEmptyFields_shouldThrowEmptyFieldsException() {
        when(memberModel.isComplete()).thenReturn(false);
        assertThrows(EmptyFieldsException.class, command::runCommand);
    }

    @Test
    void runCommand_shouldStoreExceptionAndRethrow_whenServiceFails() throws Exception {
        when(memberModel.isComplete()).thenReturn(true);
        when(memberModel.getGovernmentID()).thenReturn("1234567");
        when(memberModel.getFirstName()).thenReturn("John");
        when(memberModel.getLastName()).thenReturn("Doe");
        when(memberModel.getPhone()).thenReturn("1234567890");
        when(memberModel.getEmail()).thenReturn("john@example.com");
        when(memberModel.getPassword()).thenReturn("secret");
        when(memberModel.getRepeatedPassword()).thenReturn("secret");
        when(memberModel.getDateOfBirth()).thenReturn(LocalDate.of(2000, 1, 1));
        when(memberModel.getSex()).thenReturn("M");
        when(memberModel.getUsername()).thenReturn("johndoe");
        when(tokenHandler.getUsername()).thenReturn("admin");

        doThrow(new NetworkException("network down"))
                .when(memberService).addMember(any(AddMemberDTO.class));

        assertThrows(NetworkException.class, command::runCommand);
        assertInstanceOf(NetworkException.class, command.getLastException());
    }

    @Test
    void onSuccess_shouldClearFormAndShowSuccessAlert() {
        AddMemberCommand spyCommand = spy(command);
        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onSuccess();

        verify(spyCommand).setAlert(Alerts.MEMBER_ADDED_SUCCESS);
        verify(alertMock).getModal("The member was added successfully");
        verify(memberModel).clear();
    }

    @Test
    void onFailure_shouldShowEmptyFieldsAlert() {
        AddMemberCommand spyCommand = spy(command);
        spyCommand.setLastException(new EmptyFieldsException("missing fields"));

        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.EMPTY_FIELDS_WARN);
        verify(alertMock).getModal("missing fields");
    }

    @Test
    void onFailure_shouldShowNetworkErrorAlert() {
        AddMemberCommand spyCommand = spy(command);
        spyCommand.setLastException(new NetworkException("no connection"));

        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.NETWORK_ERROR);
        verify(alertMock).getModal("no connection");
    }

    @Test
    void onFailure_shouldHandleInvalidGovernmentID() {
        AddMemberCommand spyCommand = spy(command);
        spyCommand.setLastException(new InvalidGovernmentIDException("Invalid Government ID"));

        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.INVALID_ID_ERROR);
        verify(alertMock).getModal("Invalid Government ID");
    }

    @Test
    void onFailure_shouldHandleInvalidPhoneNumber() {
        AddMemberCommand spyCommand = spy(command);
        spyCommand.setLastException(new InvalidPhoneNumberException("Invalid Phone Number"));

        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.INVALID_PHONE_ERROR);
        verify(alertMock).getModal("Invalid Phone Number");
    }

    @Test
    void onFailure_shouldHandleInvalidEmail() {
        AddMemberCommand spyCommand = spy(command);
        spyCommand.setLastException(new InvalidEmailException("Invalid Email"));

        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.INVALID_EMAIL_ERROR);
        verify(alertMock).getModal("Invalid Email");
    }

    @Test
    void onFailure_shouldHandleInvalidPassword() {
        AddMemberCommand spyCommand = spy(command);
        spyCommand.setLastException(new InvalidPasswordException("Invalid Password"));

        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.INVALID_PASSWORD_ERROR);
        verify(alertMock).getModal("Invalid Password");
    }

    @Test
    void onFailure_shouldHandlePasswordsDoNotMatch() {
        AddMemberCommand spyCommand = spy(command);
        spyCommand.setLastException(new PasswordsDoNotMatchException("Password does not match."));

        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.UNMATCHED_PASSWORDS_ERROR);
        verify(alertMock).getModal("Password does not match.");
    }

    @Test
    void onFailure_shouldHandleServerError() {
        AddMemberCommand spyCommand = spy(command);
        spyCommand.setLastException(new ServerErrorException("Server Error"));

        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.SERVER_ERROR);
        verify(alertMock).getModal("Server Error");
    }

    @Test
    void onFailure_shouldHandleMemberAlreadyExist() {
        AddMemberCommand spyCommand = spy(command);
        spyCommand.setLastException(new MemberAlreadyExistException("Member already exists"));

        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.EXISTING_MEMBER_ERROR);
        verify(alertMock).getModal("Member already exists");
    }

    @Test
    void onFailure_shouldHandleUsernameAlreadyExist() {
        AddMemberCommand spyCommand = spy(command);
        spyCommand.setLastException(new UsernameAlreadyExistException("Username already exists"));

        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.EXISTING_USERNAME_ERROR);
        verify(alertMock).getModal("Username already exists");
    }

    @Test
    void onFailure_shouldHandleEmailAlreadyExist() {
        AddMemberCommand spyCommand = spy(command);
        spyCommand.setLastException(new EmailAlreadyExistException("Email already exists"));

        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.EXISTING_EMAIL_ERROR);
        verify(alertMock).getModal("Email already exists");
    }

    @Test
    void onFailure_shouldHandleInvalidDateOfBirth() {
        AddMemberCommand spyCommand = spy(command);
        spyCommand.setLastException(new InvalidDateOfBirthException("Invalid Date of Birth"));

        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.INVALID_DATE_OF_BIRTH);
        verify(alertMock).getModal("Invalid Date of Birth");
    }

    @Test
    void onFailure_shouldHandleExpiredSession_andOpenLoginView() {
        AddMemberCommand spyCommand = spy(command);
        spyCommand.setLastException(new ExpiredSessionException("session expired"));

        Alerts alertMock = mock(Alerts.class);
        doReturn(alertMock).when(spyCommand).getAlert();

        spyCommand.onFailure();

        verify(spyCommand).setAlert(Alerts.EXPIRED_SESSION_ERROR);
        verify(alertMock).getModal("session expired");
        verify(openLoginViewCommand).execute();
    }


    @Test
    void onFailure_withUnexpectedException_shouldThrowIllegalStateException() {
        command.setLastException(new RuntimeException("unexpected"));
        assertThrows(IllegalStateException.class, command::onFailure);
    }

}
