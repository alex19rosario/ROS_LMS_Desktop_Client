package com.ros.lmsdesktopclient.view_models.commands;

import com.ros.lmsdesktopclient.models.LoginModel;
import com.ros.lmsdesktopclient.services.LoginService;
import com.ros.lmsdesktopclient.util.Alerts;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.Views;
import com.ros.lmsdesktopclient.util.exceptions.AuthenticationException;
import com.ros.lmsdesktopclient.util.exceptions.EmptyFieldsException;
import com.ros.lmsdesktopclient.util.exceptions.NetworkException;
import com.ros.lmsdesktopclient.util.exceptions.ServerErrorException;
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
            protected Void call() {
                try {
                    loginService.login(loginModel, HttpClient.newHttpClient());
                } catch (EmptyFieldsException ex) {
                    setAlert(Alerts.EMPTY_FIELDS_WARN);
                    throw new RuntimeException("EMPTY FIELDS ERROR");
                } catch (AuthenticationException ex) {
                    setAlert(Alerts.AUTHENTICATION_ERROR);
                    throw new RuntimeException("AUTHENTICATION ERROR");
                } catch (ServerErrorException ex) {
                    setAlert(Alerts.SERVER_ERROR);
                    throw new RuntimeException("SERVER ERROR");
                } catch (NetworkException ex) {
                    setAlert(Alerts.NETWORK_ERROR);
                    throw new RuntimeException("NETWORK ERROR");
                }
                return null;
            }
        };
    }

    private void onSuccess(){
        double sceneWidth = this.viewHandler.getSceneWidth();
        double sceneHeight = this.viewHandler.getSceneHeight();
        ViewHandler.setScene(Views.MAIN_MENU.getView(), sceneWidth, sceneHeight);
    }

    private void onFailure(){
        getAlert().getModal();
        loginModel.clear();
    }
}
