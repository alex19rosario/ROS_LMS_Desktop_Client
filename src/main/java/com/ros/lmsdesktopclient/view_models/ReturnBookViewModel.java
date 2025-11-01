package com.ros.lmsdesktopclient.view_models;


import com.ros.lmsdesktopclient.commands.Command;
import com.ros.lmsdesktopclient.models.ReturnBookModel;
import com.ros.lmsdesktopclient.util.enums.CommandType;

import javax.inject.Inject;
import java.util.Map;

public class ReturnBookViewModel {
    private final Command openMainViewCommand;
    private final Command returnBookCommand;

    private final ReturnBookModel returnBookModel;

    @Inject
    public ReturnBookViewModel(ReturnBookModel returnBookModel, Map<CommandType, Command> commands) {
        openMainViewCommand = commands.get(CommandType.OPEN_VIEW_MAIN_MENU);

        returnBookCommand = commands.get(CommandType.RETURN_BOOK);

        this.returnBookModel = returnBookModel;
    }

    public ReturnBookModel getReturnBookModel() {
        return returnBookModel;
    }

    public void executeOpenMainViewCommand() {
        this.openMainViewCommand.execute();
    }

    public void executeReturnBookCommand() {
        this.returnBookCommand.execute();
    }

    public Command getReturnBookCommand() {
        return returnBookCommand;
    }
}
