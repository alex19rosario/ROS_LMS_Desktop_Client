package com.ros.lmsdesktopclient.services.service;

import com.ros.lmsdesktopclient.models.LoginModel;
import com.ros.lmsdesktopclient.util.exceptions.*;

import java.net.http.HttpClient;

public interface LoginService {
    void login(LoginModel loginModel) throws EmptyFieldsException, NetworkException, ServerErrorException, AuthenticationException, AccessDeniedException;
}
