package com.ros.lmsdesktopclient.di.modules;

import com.ros.lmsdesktopclient.util.enums.ViewType;
import com.ros.lmsdesktopclient.util.annotations.ViewTypeKey;
import com.ros.lmsdesktopclient.views.*;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModule {

    @Binds
    @IntoMap
    @ViewTypeKey(ViewType.LOGIN)
    abstract BaseView loginView(LoginView view);

    @Binds
    @IntoMap
    @ViewTypeKey(ViewType.MAIN_MENU)
    abstract BaseView mainMenuView(MainMenuView view);

    @Binds
    @IntoMap
    @ViewTypeKey(ViewType.ADD_BOOK)
    abstract BaseView addBookView(AddBookView view);

    @Binds
    @IntoMap
    @ViewTypeKey(ViewType.ADD_MEMBER)
    abstract BaseView addMemberView(AddMemberView view);

    @Binds
    @IntoMap
    @ViewTypeKey(ViewType.ISSUE_BOOK)
    abstract BaseView issueBookView(IssueBookView view);

    @Binds
    @IntoMap
    @ViewTypeKey(ViewType.RETURN_BOOK)
    abstract BaseView returnBookView(ReturnBookView view);
}
