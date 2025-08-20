package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.commands.Command;
import com.ros.lmsdesktopclient.models.MemberModel;
import com.ros.lmsdesktopclient.util.enums.CommandType;


import javax.inject.Inject;
import java.util.Map;

public class AddMemberViewModel {
    private final Command openMainViewCommand;
    private final Command addMemberCommand;
    private final MemberModel memberModel;

    @Inject
    public AddMemberViewModel(Map<CommandType, Command> commands, MemberModel memberModel){
        this.openMainViewCommand = commands.get(CommandType.OPEN_VIEW_MAIN_MENU);
        this.memberModel = memberModel;
        this.addMemberCommand = commands.get(CommandType.ADD_MEMBER);
    }

    public Command getAddMemberCommand() {
        return addMemberCommand;
    }
    public void executeOpenMainViewCommand(){
        this.openMainViewCommand.execute();
    }

    public void executeAddMemberCommand(){
        this.addMemberCommand.execute();
    }

    public MemberModel getMemberModel(){
        return memberModel;
    }
}
