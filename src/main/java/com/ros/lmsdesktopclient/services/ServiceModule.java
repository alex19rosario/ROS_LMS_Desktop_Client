package com.ros.lmsdesktopclient.services;

import com.ros.lmsdesktopclient.services.service.GenreService;
import com.ros.lmsdesktopclient.services.service.LoginService;
import com.ros.lmsdesktopclient.services.service_impl.GenreServiceImpl;
import com.ros.lmsdesktopclient.services.service_impl.LoginServiceImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ServiceModule {

    @Provides
    static LoginService loginService(AuthenticatedHttpClientFactory factory, GenreService genreService) {
        LoginService loginServiceImpl = new LoginServiceImpl(factory, genreService);
        return ServiceFactory.createProxy(LoginService.class, loginServiceImpl);
    }

    @Binds
    abstract GenreService genreService(GenreServiceImpl genreService);
}
