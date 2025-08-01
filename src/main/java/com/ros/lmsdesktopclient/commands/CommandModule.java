package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.util.CommandType;
import com.ros.lmsdesktopclient.util.CommandTypeKey;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class CommandModule {
    @Binds
    @IntoMap
    @CommandTypeKey(CommandType.LOGIN)
    abstract Command loginCommand(LoginCommand command);
}
