package com.ros.lmsdesktopclient.view_models;

import com.ros.lmsdesktopclient.commands.CommandModule;
import com.ros.lmsdesktopclient.models.ModelModule;
import com.ros.lmsdesktopclient.services.NetworkModule;
import com.ros.lmsdesktopclient.services.ServiceModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {CommandModule.class, ServiceModule.class, NetworkModule.class, ModelModule.class} )
public interface ViewModelFactory {
    LoginViewModel loginViewModel();
    MainMenuViewModel mainMenuViewModel();
    AddBookViewModel addBookViewModel();
    AddMemberViewModel addMemberViewModel();
    IssueBookViewModel issueBookViewModel();
}
