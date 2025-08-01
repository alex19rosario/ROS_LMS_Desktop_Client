package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.util.CommandType;
import com.ros.lmsdesktopclient.util.CommandTypeKey;
import com.ros.lmsdesktopclient.util.Views;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public abstract class CommandModule {

    @Binds
    @IntoMap
    @CommandTypeKey(CommandType.LOGIN)
    abstract Command loginCommand(LoginCommand command);

    @Binds
    @IntoMap
    @CommandTypeKey(CommandType.LOGOUT)
    abstract Command logoutCommand(LogoutCommand command);

    @Provides
    @IntoMap
    @CommandTypeKey(CommandType.OPEN_VIEW_ADD_BOOK)
    static Command openAddBookViewCommand() {
        return new OpenViewCommand(Views.ADD_BOOK);
    }

    @Provides
    @IntoMap
    @CommandTypeKey(CommandType.OPEN_VIEW_ADD_MEMBER)
    static Command openAddMemberViewCommand() {
        return new OpenViewCommand(Views.ADD_MEMBER);
    }

    @Provides
    @IntoMap
    @CommandTypeKey(CommandType.OPEN_VIEW_ISSUE_BOOK)
    static Command openIssueBookViewCommand() {
        return new OpenViewCommand(Views.ISSUE_BOOK);
    }
}
