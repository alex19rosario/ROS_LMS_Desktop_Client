package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.ViewType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OpenViewCommandTest {

    @Mock
    ExecutorService executorService;

    @Mock
    ViewHandler viewHandler;

    UiExecutor immediateUiExecutor;
    OpenViewCommand command;

    @BeforeEach
    void setup() {
        // Test UiExecutor that runs immediately
        immediateUiExecutor = Runnable::run;
        command = new OpenViewCommand(ViewType.ADD_BOOK, executorService, viewHandler, immediateUiExecutor);
    }

    @Test
    void runCommand_shouldCallViewHandlerSwitchTo() throws Exception {
        // Since JavaFxExtension overrides Platform.runLater, this runs immediately
        command.runCommand();

        verify(viewHandler).switchTo(ViewType.ADD_BOOK);
    }

    @Test
    void runCommand_shouldCallCorrectViewType() throws Exception {
        OpenViewCommand command2 = new OpenViewCommand(ViewType.MAIN_MENU, executorService, viewHandler, immediateUiExecutor);

        command2.runCommand();
        verify(viewHandler).switchTo(ViewType.MAIN_MENU);
        verify(viewHandler, never()).switchTo(ViewType.ADD_BOOK);
    }

}
