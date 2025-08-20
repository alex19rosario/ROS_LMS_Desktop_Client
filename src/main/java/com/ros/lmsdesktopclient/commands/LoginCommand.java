package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.dtos.LoginDTO;
import com.ros.lmsdesktopclient.models.LoginModel;
import com.ros.lmsdesktopclient.services.service.LoginService;
import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.enums.Alerts;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.enums.ViewType;
import com.ros.lmsdesktopclient.util.exceptions.*;
import com.ros.lmsdesktopclient.util.ViewHandler;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

public final class LoginCommand extends Command {
    private final LoginModel loginModel;
    private final LoginService loginService;
    private final ViewHandler viewHandler;
    private final TokenHandler tokenHandler;
    private Throwable lastException; // store exception for onFailure()

    @Inject
    public LoginCommand(
            LoginModel loginModel,
            LoginService loginService,
            ExecutorService executorService,
            ViewHandler viewHandler,
            TokenHandler tokenHandler,
            UiExecutor uiExecutor
    ) {
        super(executorService, uiExecutor);
        this.loginModel = loginModel;
        this.loginService = loginService;
        this.viewHandler = viewHandler;
        this.tokenHandler = tokenHandler;
        this.setOnCommandSuccess(this::onSuccess);
        this.setOnCommandFailure(this::onFailure);
    }

    @Override
    protected void runCommand() throws Exception {
        try {
            checkForm(loginModel);
            LoginDTO loginDTO = new LoginDTO(loginModel.getUsername(), loginModel.getPassword());
            loginService.login(loginDTO);
        } catch (Exception ex) {
            this.lastException = ex;
            throw ex; // triggers failure in base class
        }
    }

    void onSuccess() {
        viewHandler.switchTo(ViewType.MAIN_MENU);
        loginModel.clear();
    }

    void onFailure() {
        tokenHandler.removeAll();

        if (lastException != null) {
            Alerts alert = switch (lastException) {
                case EmptyFieldsException ignored -> Alerts.EMPTY_FIELDS_WARN;
                case NetworkException ignored -> Alerts.NETWORK_ERROR;
                case ServerErrorException ignored -> Alerts.SERVER_ERROR;
                case AuthenticationException ignored -> Alerts.AUTHENTICATION_ERROR;
                case AccessDeniedException ignored -> Alerts.ACCESS_DENIED_ERROR;
                default -> throw new IllegalStateException("Unexpected exception: " + lastException);
            };

            setAlert(alert);
            getAlert().getModal(lastException.getMessage());
        }
        loginModel.clear();
    }

    private void checkForm(LoginModel loginModel) throws EmptyFieldsException {
        if (!loginModel.isComplete()) {
            throw new EmptyFieldsException("Login form: there are empty fields");
        }
    }

    public void setLastException(Throwable lastException) {
        this.lastException = lastException;
    }

    public Throwable getLastException() {
        return lastException;
    }
}
