package com.ros.lmsdesktopclient.services.service;

import com.ros.lmsdesktopclient.dtos.AddMemberDTO;
import com.ros.lmsdesktopclient.util.exceptions.*;

public interface MemberService {
    void addMember(AddMemberDTO member) throws
            NetworkException,
            ServerErrorException,
            ExpiredSessionException,
            MemberAlreadyExistException,
            EmailAlreadyExistException;
}
