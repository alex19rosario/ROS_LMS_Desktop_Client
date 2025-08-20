package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.dtos.LoginDTO;
import com.ros.lmsdesktopclient.models.LoginModel;
import com.ros.lmsdesktopclient.services.service.LoginService;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.Alerts;
import com.ros.lmsdesktopclient.util.enums.ViewType;
import com.ros.lmsdesktopclient.util.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginCommandTest {

    @Mock
    LoginModel loginModel;
    @Mock
    LoginService loginService;
    @Mock
    ExecutorService executorService;
    @Mock
    ViewHandler viewHandler;
    @Mock
    TokenHandler tokenHandler;
    @Mock
    UiExecutor uiExecutor;

    LoginCommand command;

    @BeforeEach
    void setup() {
        command = new LoginCommand(
                loginModel,
                loginService,
                executorService,
                viewHandler,
                tokenHandler,
                uiExecutor
        );
    }

    @Test
    void testSuccessfulLoginSwitchesToMainMenu() throws Exception {
        when(loginModel.isComplete()).thenReturn(true);
        when(loginModel.getUsername()).thenReturn("user");
        when(loginModel.getPassword()).thenReturn("pass");

        command.runCommand();
        command.onSuccess(); // simulate success callback

        verify(viewHandler).switchTo(ViewType.MAIN_MENU);
        verify(loginModel).clear();
    }

    @Test
    void runCommand_shouldStoreExceptionAndRethrow_whenLoginFails() throws AccessDeniedException, AuthenticationException, ServerErrorException, NetworkException {

        // Make form validation succeed
        when(loginModel.isComplete()).thenReturn(true);
        when(loginModel.getUsername()).thenReturn("user");
        when(loginModel.getPassword()).thenReturn("pass");

        // Simulate login throwing an AuthenticationException
        doThrow(new AuthenticationException("Invalid credentials"))
                .when(loginService)
                .login(any(LoginDTO.class));

        // act + assert
        assertThrows(AuthenticationException.class, command::runCommand);

        // Verify the exception was stored
        assertInstanceOf(AuthenticationException.class, command.getLastException());
    }


    @Test
    void onFailure_shouldShowEmptyFieldsAlert() {
        LoginCommand spyCommand = spy(command);

        // prepare
        spyCommand.setLastException(new EmptyFieldsException("Fields cannot be empty"));
        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        // act
        spyCommand.onFailure();

        // assert
        verify(tokenHandler).removeAll();
        verify(spyCommand).setAlert(Alerts.EMPTY_FIELDS_WARN);
        verify(mockAlert).getModal("Fields cannot be empty");
        verify(loginModel).clear();
    }

    @Test
    void onFailure_shouldShowNetworkErrorAlert() {
        LoginCommand spyCommand = spy(command);

        // prepare
        spyCommand.setLastException(new NetworkException("No internet connection available"));
        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        // act
        spyCommand.onFailure();

        // assert
        verify(tokenHandler).removeAll();
        verify(spyCommand).setAlert(Alerts.NETWORK_ERROR);
        verify(mockAlert).getModal("No internet connection available");
        verify(loginModel).clear();
    }

    @Test
    void onFailure_shouldShowServerErrorAlert() {
        LoginCommand spyCommand = spy(command);

        // prepare
        spyCommand.setLastException(new ServerErrorException("Server is not reachable"));
        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        // act
        spyCommand.onFailure();

        // assert
        verify(tokenHandler).removeAll();
        verify(spyCommand).setAlert(Alerts.SERVER_ERROR);
        verify(mockAlert).getModal("Server is not reachable");
        verify(loginModel).clear();
    }

    @Test
    void onFailure_shouldShowAuthenticationErrorAlert() {
        LoginCommand spyCommand = spy(command);

        // prepare
        spyCommand.setLastException(new AuthenticationException("The username or password are incorrect."));
        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        // act
        spyCommand.onFailure();

        // assert
        verify(tokenHandler).removeAll();
        verify(spyCommand).setAlert(Alerts.AUTHENTICATION_ERROR);
        verify(mockAlert).getModal("The username or password are incorrect.");
        verify(loginModel).clear();
    }

    @Test
    void onFailure_shouldShowAccessDeniedAlert() {
        LoginCommand spyCommand = spy(command);

        // prepare
        spyCommand.setLastException(new AccessDeniedException("Access denied: Members are not authorized."));
        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        // act
        spyCommand.onFailure();

        // assert
        verify(tokenHandler).removeAll();
        verify(spyCommand).setAlert(Alerts.ACCESS_DENIED_ERROR);
        verify(mockAlert).getModal("Access denied: Members are not authorized.");
        verify(loginModel).clear();
    }

    @Test
    void onFailure_withUnexpectedException_shouldThrowIllegalStateException() {
        RuntimeException unexpected = new RuntimeException("Unexpected");

        command.setLastException(unexpected);

        assertThrows(IllegalStateException.class, command::onFailure);
    }

    @Test
    void runCommand_withEmptyFields_shouldThrowEmptyFieldsException() {
        // both username & password are empty by default
        assertThrows(EmptyFieldsException.class, command::runCommand);
    }
}
