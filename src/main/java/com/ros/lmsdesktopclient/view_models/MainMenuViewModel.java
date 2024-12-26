package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.view_models.commands.Command;
import com.ros.lmsdesktopclient.view_models.commands.LogOutCommand;

public class MainMenuViewModel {
    private Command logOutCommand;

    public MainMenuViewModel(){
        logOutCommand = new LogOutCommand();
    }

    public void executeLogOutCommand(){
        logOutCommand.execute();
    }
}
