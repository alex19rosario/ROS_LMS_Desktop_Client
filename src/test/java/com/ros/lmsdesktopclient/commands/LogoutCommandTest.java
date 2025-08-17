package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.ViewType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LogoutCommandTest {

    @Mock
    ExecutorService executorService;
    @Mock
    ViewHandler viewHandler;
    @Mock
    TokenHandler tokenHandler;

    LogoutCommand command;

    @BeforeEach
    void setup() {
        command = new LogoutCommand(executorService, viewHandler, tokenHandler);
    }

    @Test
    void runCommand_shouldRemoveAllTokens() throws Exception {
        // act
        command.runCommand();

        // assert
        verify(tokenHandler).removeAll();
    }

    @Test
    void onSuccess_shouldSwitchToLoginView() {
        // use spy to call private onSuccess via reflection of callback
        LogoutCommand spyCommand = spy(command);

        // act
        spyCommand.onSuccess();

        // assert
        verify(viewHandler).switchTo(ViewType.LOGIN);
    }

}
