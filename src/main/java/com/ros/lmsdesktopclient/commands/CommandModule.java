package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.util.CommandType;
import com.ros.lmsdesktopclient.util.CommandTypeKey;
import com.ros.lmsdesktopclient.util.ViewHandler;
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

    @Provides
    @IntoMap
    @CommandTypeKey(CommandType.OPEN_VIEW_MAIN_MENU)
    static Command openMainViewCommand() {
        return new OpenViewCommand(Views.MAIN_MENU);
    }

    @Binds
    @IntoMap
    @CommandTypeKey(CommandType.ADD_AUTHOR)
    abstract Command addAuthorCommand(AddAuthorCommand command);

    @Binds
    @IntoMap
    @CommandTypeKey(CommandType.ADD_GENRE)
    abstract Command addGenreCommand(AddGenreCommand command);

    @Binds
    @IntoMap
    @CommandTypeKey(CommandType.ADD_BOOK)
    abstract Command addBookCommand(AddBookCommand command);

    @Provides
    @IntoMap
    @CommandTypeKey(CommandType.SELECT_FILE)
    static Command selectFileCommand(BookModel bookModel) {
        return new SelectFileCommand(bookModel, ViewHandler.getInstance().getStage());
    }
}
