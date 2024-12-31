package com.ros.lmsdesktopclient.services.service;

import com.ros.lmsdesktopclient.models.LoginModel;
import com.ros.lmsdesktopclient.util.exceptions.AuthenticationException;
import com.ros.lmsdesktopclient.util.exceptions.EmptyFieldsException;
import com.ros.lmsdesktopclient.util.exceptions.NetworkException;
import com.ros.lmsdesktopclient.util.exceptions.ServerErrorException;

import java.net.http.HttpClient;

public interface LoginService {
    void login(LoginModel loginModel, HttpClient client) throws EmptyFieldsException, NetworkException, ServerErrorException, AuthenticationException;
}
