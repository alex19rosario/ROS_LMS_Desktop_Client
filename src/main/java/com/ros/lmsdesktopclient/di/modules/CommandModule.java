package com.ros.lmsdesktopclient.di.modules;

import com.ros.lmsdesktopclient.commands.*;
import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.annotations.LoginCommandQualifier;
import com.ros.lmsdesktopclient.util.enums.CommandType;
import com.ros.lmsdesktopclient.util.annotations.CommandTypeKey;
import com.ros.lmsdesktopclient.util.enums.ViewType;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import javafx.stage.FileChooser;

import java.util.concurrent.ExecutorService;


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
    static Command openAddBookViewCommand(ExecutorService executorService, ViewHandler viewHandler, UiExecutor javaFxUiExecutor) {
        return new OpenViewCommand(ViewType.ADD_BOOK, executorService, viewHandler, javaFxUiExecutor);
    }

    @Provides
    @IntoMap
    @CommandTypeKey(CommandType.OPEN_VIEW_ADD_MEMBER)
    static Command openAddMemberViewCommand(ExecutorService executorService, ViewHandler viewHandler, UiExecutor javaFxUiExecutor) {
        return new OpenViewCommand(ViewType.ADD_MEMBER, executorService, viewHandler, javaFxUiExecutor);
    }

    @Provides
    @IntoMap
    @CommandTypeKey(CommandType.OPEN_VIEW_ISSUE_BOOK)
    static Command openIssueBookViewCommand(ExecutorService executorService, ViewHandler viewHandler, UiExecutor javaFxUiExecutor) {
        return new OpenViewCommand(ViewType.ISSUE_BOOK, executorService, viewHandler, javaFxUiExecutor);
    }

    @Provides
    @IntoMap
    @CommandTypeKey(CommandType.OPEN_VIEW_MAIN_MENU)
    static Command openMainViewCommand(ExecutorService executorService, ViewHandler viewHandler, UiExecutor javaFxUiExecutor) {
        return new OpenViewCommand(ViewType.MAIN_MENU, executorService, viewHandler, javaFxUiExecutor);
    }

    @Binds
    @IntoMap
    @CommandTypeKey(CommandType.ADD_AUTHOR)
    abstract Command addAuthorCommand(AddAuthorCommand command);

    @Binds
    @IntoMap
    @CommandTypeKey(CommandType.ADD_BOOK)
    abstract Command addBookCommand(AddBookCommand command);

    @Provides
    @IntoMap
    @CommandTypeKey(CommandType.SELECT_FILE)
    static Command selectFileCommand(BookModel bookModel, ViewHandler viewHandler, FileChooser fileChooser, ExecutorService executorService, UiExecutor javaFxUiExecutor) {
        return new SelectFileCommand(bookModel, viewHandler.getStage(), fileChooser, executorService, javaFxUiExecutor);
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

    @Provides
    @LoginCommandQualifier
    static Command provideLoginViewCommand(ExecutorService executorService, ViewHandler viewHandler, UiExecutor javaFxUiExecutor) {
        return new OpenViewCommand(ViewType.LOGIN, executorService, viewHandler, javaFxUiExecutor);
    }
}
