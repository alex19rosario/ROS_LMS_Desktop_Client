package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.util.Views;
import com.ros.lmsdesktopclient.view_models.commands.Command;
import com.ros.lmsdesktopclient.view_models.commands.OpenViewCommand;

public class AddMemberViewModel {
    private final Command openMainViewCommand;

    public AddMemberViewModel(){
        openMainViewCommand = new OpenViewCommand(Views.MAIN_MENU);
    }

    public void executeOpenMainViewCommand(){
        this.openMainViewCommand.execute();
    }
}
