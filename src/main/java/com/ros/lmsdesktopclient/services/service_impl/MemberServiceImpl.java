package com.ros.lmsdesktopclient.services.service_impl;

import com.ros.lmsdesktopclient.dtos.AddMemberDTO;
import com.ros.lmsdesktopclient.services.service.MemberService;
import com.ros.lmsdesktopclient.util.exceptions.*;

public class MemberServiceImpl implements MemberService {
    @Override
    public void addMember(AddMemberDTO member) throws NetworkException, ServerErrorException, ExpiredSessionException, MemberAlreadyExistException, EmailAlreadyExistException, PasswordsDoNotMatchException {
        System.out.println("Adding member: " + member);
    }
}
