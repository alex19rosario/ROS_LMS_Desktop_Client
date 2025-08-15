package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.commands.Command;
import com.ros.lmsdesktopclient.util.enums.CommandType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumMap;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MainMenuViewModelTest {

    @Mock private Command logOutCommand;
    @Mock private Command openAddBookViewCommand;
    @Mock private Command openAddMemberViewCommand;
    @Mock private Command openIssueBookViewCommand;

    private MainMenuViewModel viewModel;

    @BeforeEach
    void setUp() {

        // Populate the map
        Map<CommandType, Command> commands = new EnumMap<>(CommandType.class);
        commands.put(CommandType.LOGOUT, logOutCommand);
        commands.put(CommandType.OPEN_VIEW_ADD_BOOK, openAddBookViewCommand);
        commands.put(CommandType.OPEN_VIEW_ADD_MEMBER, openAddMemberViewCommand);
        commands.put(CommandType.OPEN_VIEW_ISSUE_BOOK, openIssueBookViewCommand);

        // Create the view model with mocks
        viewModel = new MainMenuViewModel(commands);
    }

    @Test
    void executeLogOutCommand_shouldCallExecuteOnLogOutCommand() {
        viewModel.executeLogOutCommand();
        verify(logOutCommand, times(1)).execute();
    }

    @Test
    void executeOpenAddBookViewCommand_shouldCallExecuteOnOpenAddBookCommand() {
        viewModel.executeOpenAddBookViewCommand();
        verify(openAddBookViewCommand, times(1)).execute();
    }

    @Test
    void executeOpenAddMemberViewCommand_shouldCallExecuteOnOpenAddMemberCommand() {
        viewModel.executeOpenAddMemberViewCommand();
        verify(openAddMemberViewCommand, times(1)).execute();
    }

    @Test
    void executeOpenIssueBookViewCommand_shouldCallExecuteOnOpenIssueBookCommand() {
        viewModel.executeOpenIssueBookViewCommand();
        verify(openIssueBookViewCommand, times(1)).execute();
    }
}
