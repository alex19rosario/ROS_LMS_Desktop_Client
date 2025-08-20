package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.commands.Command;
import com.ros.lmsdesktopclient.models.MemberModel;
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
class AddMemberViewModelTest {

    @Mock private Command openMainViewCommand;
    @Mock private Command addMemberCommand;
    @Mock private MemberModel memberModel;

    private AddMemberViewModel viewModel;

    @BeforeEach
    void setUp() {
        Map<CommandType, Command> commands = new EnumMap<>(CommandType.class);
        commands.put(CommandType.OPEN_VIEW_MAIN_MENU, openMainViewCommand);
        commands.put(CommandType.ADD_MEMBER, addMemberCommand);

        viewModel = new AddMemberViewModel(commands, memberModel);
    }

    @Test
    void getAddMemberCommand_shouldReturnAddMemberCommand() {
        assertThat(viewModel.getAddMemberCommand()).isSameAs(addMemberCommand);
    }

    @Test
    void getMemberModel_shouldReturnMemberModel() {
        assertThat(viewModel.getMemberModel()).isSameAs(memberModel);
    }

    @Test
    void executeOpenMainViewCommand_shouldCallExecuteOnOpenMainViewCommand() {
        viewModel.executeOpenMainViewCommand();
        verify(openMainViewCommand, times(1)).execute();
    }

    @Test
    void executeAddMemberCommand_shouldCallExecuteOnAddMemberCommand() {
        viewModel.executeAddMemberCommand();
        verify(addMemberCommand, times(1)).execute();
    }

}
