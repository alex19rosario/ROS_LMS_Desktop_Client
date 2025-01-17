package com.ros.lmsdesktopclient.view_models.commands;

import com.ros.lmsdesktopclient.models.LoginModel;
import com.ros.lmsdesktopclient.services.service.LoginService;
import com.ros.lmsdesktopclient.util.Alerts;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.Views;
import com.ros.lmsdesktopclient.util.exceptions.*;
import javafx.concurrent.Task;

import java.net.http.HttpClient;

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
                loginService.login(loginModel);
                return null;
            }
        };
    }

    private void onSuccess(){
        double width = ViewHandler.getInstance().getSceneWidth();
        double height = ViewHandler.getInstance().getSceneHeight();
        ViewHandler.switchTo(Views.MAIN_MENU.getView(), width, height);
    }

    private void onFailure(){
        // Get the exception from the command task
        Throwable exception = getCommandTask().getException();

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
        setAlert(alert);
        getAlert().getModal();

        // Clear the login model
        loginModel.clear();
    }
}
