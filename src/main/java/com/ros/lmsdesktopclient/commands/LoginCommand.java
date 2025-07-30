package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.dtos.LoginDTO;
import com.ros.lmsdesktopclient.models.LoginModel;
import com.ros.lmsdesktopclient.services.service.LoginService;
import com.ros.lmsdesktopclient.util.Alerts;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.Views;
import com.ros.lmsdesktopclient.util.exceptions.*;
import javafx.concurrent.Task;

public class LoginCommand extends Command {

    private final LoginModel loginModel;
    private final LoginService loginService;

    public LoginCommand(LoginModel loginModel, LoginService loginService){
        this.loginModel = loginModel;
        this.loginService = loginService;
        this.setOnCommandSuccess(this::onSuccess);
        this.setOnCommandFailure(this::onFailure);
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<>() {
            @Override
            protected Void call() throws EmptyFieldsException, AuthenticationException, ServerErrorException, NetworkException, AccessDeniedException {
                checkForm(loginModel);
                LoginDTO loginDTO = new LoginDTO(loginModel.getUsername(), loginModel.getPassword());
                loginService.login(loginDTO);
                return null;
            }
        };
    }

    private void onSuccess(){
        ViewHandler.switchTo(Views.MAIN_MENU);
    }

    private void onFailure(){
        // Get the exception from the command task
        Throwable exception = getCommandTask().getException();
        TokenHandler.getInstance().removeAll();

        // Use a switch expression to determine the alert type
        Alerts alert = switch (exception) {
            case EmptyFieldsException e -> Alerts.EMPTY_FIELDS_WARN;
            case NetworkException e -> Alerts.NETWORK_ERROR;
            case ServerErrorException e -> Alerts.SERVER_ERROR;
            case AuthenticationException e -> Alerts.AUTHENTICATION_ERROR;
            case AccessDeniedException e -> Alerts.ACCESS_DENIED_ERROR;
            default -> throw new IllegalStateException("Unexpected exception: " + exception);
        };

        // Set the determined alert and display the modal
        String content = exception.getMessage();
        setAlert(alert);
        getAlert().getModal(content);

        // Clear the login model
        loginModel.clear();
    }

    private void checkForm(LoginModel loginModel) throws EmptyFieldsException {
        if(!loginModel.isComplete()){
            throw new EmptyFieldsException("Login form: there are empty fields");
        }
    }
}
