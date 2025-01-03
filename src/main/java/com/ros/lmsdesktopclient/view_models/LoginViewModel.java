package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.models.LoginModel;
import com.ros.lmsdesktopclient.services.ServiceFactory;
import com.ros.lmsdesktopclient.services.service.LoginService;
import com.ros.lmsdesktopclient.services.service_impl.LoginServiceImpl;
import com.ros.lmsdesktopclient.view_models.commands.Command;
import com.ros.lmsdesktopclient.view_models.commands.LoginCommand;

public class LoginViewModel {
    private final LoginModel loginModel;
    private final Command loginCommand;

    public LoginViewModel(LoginModel loginModel){
        this.loginModel = loginModel;
        LoginService loginService = ServiceFactory.createProxy(LoginService.class, new LoginServiceImpl());
        this.loginCommand = new LoginCommand(this.loginModel, loginService);
    }

    public LoginModel getLoginModel() {
        return loginModel;
    }

    public Command getLoginCommand(){
        return loginCommand;
    }

    public void executeLoginCommand(){
        this.loginCommand.execute();
    }


}
