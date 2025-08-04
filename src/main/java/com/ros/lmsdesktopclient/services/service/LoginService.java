package com.ros.lmsdesktopclient.services.service;

import com.ros.lmsdesktopclient.dtos.LoginDTO;
import com.ros.lmsdesktopclient.util.exceptions.*;


public interface LoginService {
    void login(LoginDTO loginDTO) throws NetworkException, ServerErrorException, AuthenticationException, AccessDeniedException;
}
