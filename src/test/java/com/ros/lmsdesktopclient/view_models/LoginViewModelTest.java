package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.commands.Command;
import com.ros.lmsdesktopclient.models.LoginModel;
import com.ros.lmsdesktopclient.util.enums.CommandType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginViewModelTest {

    @Mock private LoginModel loginModel;
    @Mock private Command loginCommand;
    private LoginViewModel viewModel;

    @BeforeEach
    void setUp() {
        Map<CommandType, Command> commands = new EnumMap<>(CommandType.class);    // EnumMap for commands
        commands.put(CommandType.LOGIN, loginCommand);  // Put LOGIN command

        viewModel = new LoginViewModel(loginModel, commands);
    }

    @Test
    void getLoginModel_shouldReturnInjectedModel() {
        assertSame(loginModel, viewModel.getLoginModel());
    }

    @Test
    void getLoginCommand_shouldReturnLoginCommand() {
        assertSame(loginCommand, viewModel.getLoginCommand());
    }

    @Test
    void executeLoginCommand_shouldCallExecuteOnCommand() {
        viewModel.executeLoginCommand();
        verify(loginCommand, times(1)).execute(); // Verify execute() was called once
    }
}
