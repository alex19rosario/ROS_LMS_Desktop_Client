package com.ros.lmsdesktopclient.di.factories;

import com.ros.lmsdesktopclient.di.modules.CommandModule;
import com.ros.lmsdesktopclient.di.modules.ModelModule;
import com.ros.lmsdesktopclient.di.modules.NetworkModule;
import com.ros.lmsdesktopclient.di.modules.ServiceModule;
import com.ros.lmsdesktopclient.di.modules.ViewModelModule;
import com.ros.lmsdesktopclient.views.*;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        CommandModule.class,
        ServiceModule.class,
        NetworkModule.class,
        ModelModule.class,
        ViewModelModule.class
} )
public interface ViewFactory {
    LoginView loginView();
    MainMenuView mainMenuView();
    AddBookView addBookView();
    AddMemberView addMemberView();
    IssueBookView issueBookView();
}
