package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.commands.Command;
import com.ros.lmsdesktopclient.util.enums.CommandType;

import javax.inject.Inject;
import java.util.Map;

public class MainMenuViewModel {
    private final Command logOutCommand;
    private final Command openAddBookViewCommand;
    private final Command openAddMemberViewCommand;
    private final Command openIssueBookViewCommand;

    @Inject
    public MainMenuViewModel(Map<CommandType, Command> commands){
        logOutCommand = commands.get(CommandType.LOGOUT);
        openAddBookViewCommand = commands.get(CommandType.OPEN_VIEW_ADD_BOOK);
        openAddMemberViewCommand = commands.get(CommandType.OPEN_VIEW_ADD_MEMBER);
        openIssueBookViewCommand = commands.get(CommandType.OPEN_VIEW_ISSUE_BOOK);
    }

    public void executeLogOutCommand(){
        logOutCommand.execute();
    }

    public void executeOpenAddBookViewCommand(){
        openAddBookViewCommand.execute();
    }

    public void executeOpenAddMemberViewCommand() {
        openAddMemberViewCommand.execute();
    }

    public void executeOpenIssueBookViewCommand() {
        openIssueBookViewCommand.execute();
    }
}
