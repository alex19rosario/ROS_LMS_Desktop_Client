package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.util.Views;
import com.ros.lmsdesktopclient.view_models.commands.Command;
import com.ros.lmsdesktopclient.view_models.commands.LogOutCommand;
import com.ros.lmsdesktopclient.view_models.commands.OpenViewCommand;

public class MainMenuViewModel {
    private final Command logOutCommand;
    private final Command openAddBookViewCommand;
    private final Command openAddMemberViewCommand;

    public MainMenuViewModel(){
        logOutCommand = new LogOutCommand();
        openAddBookViewCommand = new OpenViewCommand(Views.ADD_BOOK);
        openAddMemberViewCommand = new OpenViewCommand(Views.ADD_MEMBER);
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
}
