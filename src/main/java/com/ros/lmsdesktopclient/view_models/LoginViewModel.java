package com.ros.lmsdesktopclient.view_models;
import com.ros.lmsdesktopclient.models.LoginModel;
import com.ros.lmsdesktopclient.commands.Command;
import com.ros.lmsdesktopclient.util.enums.CommandType;

import javax.inject.Inject;
import java.util.Map;

public final class LoginViewModel {
    private final LoginModel loginModel;
    private final Command loginCommand;

    @Inject
    public LoginViewModel(LoginModel loginModel, Map<CommandType, Command> commands){
        this.loginModel = loginModel;
        this.loginCommand = commands.get(CommandType.LOGIN);
    }

    public LoginModel getLoginModel() {
        return loginModel;
    }

    public Command getLoginCommand() {
        return loginCommand;
    }

    public void executeLoginCommand(){
        this.loginCommand.execute();
    }

}
