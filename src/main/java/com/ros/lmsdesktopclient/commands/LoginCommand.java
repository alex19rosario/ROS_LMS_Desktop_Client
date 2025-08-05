package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.dtos.LoginDTO;
import com.ros.lmsdesktopclient.models.LoginModel;
import com.ros.lmsdesktopclient.services.service.LoginService;
import com.ros.lmsdesktopclient.util.enums.Alerts;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.Views;
import com.ros.lmsdesktopclient.util.exceptions.*;
import javafx.concurrent.Task;

import javax.inject.Inject;

public final class LoginCommand extends Command {

    private final LoginModel loginModel;
    private final LoginService loginService;

    @Inject
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
            protected Void call() throws EmptyFieldsException, AuthenticationException, ServerErrorException, NetworkException, AccessDeniedException, InterruptedException {
                Thread.sleep(3000);
                checkForm(loginModel);
                LoginDTO loginDTO = new LoginDTO(loginModel.getUsername(), loginModel.getPassword());
                loginService.login(loginDTO);
                return null;
            }
        };
    }

    private void onSuccess(){
        ViewHandler.switchTo(Views.MAIN_MENU);
        loginModel.clear();
    }

    private void onFailure(){
        // Get the exception from the command task
        Throwable exception = getCommandTask().getException();
        TokenHandler.getInstance().removeAll();

        // Use a switch expression to determine the alert type
        Alerts alert = switch (exception) {
            case EmptyFieldsException ignored -> Alerts.EMPTY_FIELDS_WARN;
            case NetworkException ignored -> Alerts.NETWORK_ERROR;
            case ServerErrorException ignored -> Alerts.SERVER_ERROR;
            case AuthenticationException ignored -> Alerts.AUTHENTICATION_ERROR;
            case AccessDeniedException ignored -> Alerts.ACCESS_DENIED_ERROR;
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
