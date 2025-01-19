package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.models.MemberModel;
import com.ros.lmsdesktopclient.services.ServiceFactory;
import com.ros.lmsdesktopclient.services.service.MemberService;
import com.ros.lmsdesktopclient.services.service_impl.MemberServiceImpl;
import com.ros.lmsdesktopclient.util.Views;
import com.ros.lmsdesktopclient.view_models.commands.AddMemberCommand;
import com.ros.lmsdesktopclient.view_models.commands.Command;
import com.ros.lmsdesktopclient.view_models.commands.OpenViewCommand;

public class AddMemberViewModel {
    private final Command openMainViewCommand;
    private final Command addMemberCommand;
    private MemberModel memberModel;

    public AddMemberViewModel(){
        openMainViewCommand = new OpenViewCommand(Views.MAIN_MENU);
        memberModel = new MemberModel();
        MemberService memberService = ServiceFactory.createProxy(MemberService.class, new MemberServiceImpl());
        addMemberCommand = new AddMemberCommand(memberModel, memberService);
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
