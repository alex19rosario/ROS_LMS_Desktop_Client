package com.ros.lmsdesktopclient.di.modules;

import com.ros.lmsdesktopclient.commands.*;
import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.util.enums.CommandType;
import com.ros.lmsdesktopclient.util.CommandTypeKey;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.Views;
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

    @Binds
    @IntoMap
    @CommandTypeKey(CommandType.ADD_MEMBER)
    abstract Command addMemberCommand(AddMemberCommand command);

    @Binds
    @IntoMap
    @CommandTypeKey(CommandType.SEARCH_BOOKS)
    abstract Command searchBooksCommand(SearchBooksCommand command);

    @Binds
    @IntoMap
    @CommandTypeKey(CommandType.CLEAR_FILTER)
    abstract Command clearFilterCommand(ClearFilterCommand command);

    @Binds
    @IntoMap
    @CommandTypeKey(CommandType.SELECT_BOOK)
    abstract Command selectBookCommand(SelectBookCommand command);

    @Binds
    @IntoMap
    @CommandTypeKey(CommandType.LOAD_BOOKS)
    abstract Command loadBooksCommand(LoadBooksCommand command);

    @Binds
    @IntoMap
    @CommandTypeKey(CommandType.ISSUE_BOOK)
    abstract Command issueBookCommand(IssueBookCommand command);

    @Binds
    @IntoMap
    @CommandTypeKey(CommandType.GET_GENRES)
    abstract Command getGenresCommand(GetGenresCommand command);
}
