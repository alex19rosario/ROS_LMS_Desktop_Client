package com.ros.lmsdesktopclient.view_models.commands;

import com.ros.lmsdesktopclient.dtos.AddMemberDTO;
import com.ros.lmsdesktopclient.models.MemberModel;
import com.ros.lmsdesktopclient.services.service.MemberService;
import com.ros.lmsdesktopclient.util.exceptions.*;
import javafx.concurrent.Task;

import java.util.function.Function;

public class AddMemberCommand extends Command{

    private final MemberModel member;
    private final MemberService memberService;

    public AddMemberCommand(MemberModel member, MemberService memberService){
        this.member = member;
        this.memberService = memberService;
    }

    @Override
    protected Task<Void> createCommandTask() {

        return new Task<>() {
            @Override
            protected Void call() throws MemberAlreadyExistException, InvalidGovernmentIDException, InvalidPasswordException, ServerErrorException, ExpiredSessionException, InvalidEmailException, NetworkException, EmailAlreadyExistException, EmptyFieldsException {
                checkForm();
                AddMemberDTO memberDTO = mapper.apply(member);
                memberService.addMember(memberDTO);
                return null;
            }
        };
    }

    private void onSuccess(){

    }

    private void onFailure(){

    }

    private void checkForm() throws EmptyFieldsException, InvalidGovernmentIDException, InvalidEmailException, InvalidPasswordException, PasswordsDoNotMatchException {

    }

    private final Function<MemberModel, AddMemberDTO> mapper = memberModel ->
            new AddMemberDTO(memberModel.getGovernmentID(),
                    memberModel.getFirstName(),
                    memberModel.getLastName(),
                    memberModel.getPhone(),
                    Byte.parseByte(memberModel.getAge()),
                    memberModel.getSex().charAt(0),
                    memberModel.getEmail(),
                    memberModel.getUsername(),
                    memberModel.getPassword(),
                    memberModel.getRepeatedPassword());
}
