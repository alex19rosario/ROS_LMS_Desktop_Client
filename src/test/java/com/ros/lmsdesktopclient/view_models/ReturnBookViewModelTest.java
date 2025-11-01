package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.commands.Command;
import com.ros.lmsdesktopclient.models.ReturnBookModel;
import com.ros.lmsdesktopclient.util.enums.CommandType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReturnBookViewModelTest {

    @Mock private Command openMainViewCommand;
    @Mock private Command returnBookCommand;
    @Mock private ReturnBookModel returnBookModel;

    private ReturnBookViewModel viewModel;

    @BeforeEach
    void setUp() {
        Map<CommandType, Command> commands = new EnumMap<>(CommandType.class);
        commands.put(CommandType.OPEN_VIEW_MAIN_MENU, openMainViewCommand);
        commands.put(CommandType.RETURN_BOOK, returnBookCommand);

        viewModel = new ReturnBookViewModel(returnBookModel, commands);
    }

    @Test
    void getReturnBookModel_shouldReturnReturnBookModel() {
        assertThat(viewModel.getReturnBookModel()).isSameAs(returnBookModel);
    }

    @Test
    void getReturnBookCommand_shouldReturnReturnBookCommand() {
        assertThat(viewModel.getReturnBookCommand()).isSameAs(returnBookCommand);
    }

    @Test
    void executeOpenMainViewCommand_shouldCallExecute() {
        viewModel.executeOpenMainViewCommand();
        verify(openMainViewCommand, times(1)).execute();
    }

    @Test
    void executeReturnBookCommand_shouldCallExecute() {
        viewModel.executeReturnBookCommand();
        verify(returnBookCommand, times(1)).execute();
    }
}
