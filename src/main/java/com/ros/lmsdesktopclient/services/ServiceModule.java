package com.ros.lmsdesktopclient.services;

import com.ros.lmsdesktopclient.services.service.BookService;
import com.ros.lmsdesktopclient.services.service.GenreService;
import com.ros.lmsdesktopclient.services.service.LoginService;
import com.ros.lmsdesktopclient.services.service_impl.BookServiceImpl;
import com.ros.lmsdesktopclient.services.service_impl.GenreServiceImpl;
import com.ros.lmsdesktopclient.services.service_impl.LoginServiceImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import java.net.http.HttpClient;

@Module
public abstract class ServiceModule {

    @Provides
    static LoginService loginService(AuthenticatedHttpClientFactory factory, GenreService genreService) {
        LoginService loginService = new LoginServiceImpl(factory, genreService);
        return ServiceFactory.createProxy(LoginService.class, loginService);
    }

    @Binds
    abstract GenreService genreService(GenreServiceImpl genreService);

    @Provides
    static BookService bookService(HttpClient client) {
        BookService bookService = new BookServiceImpl(client);
        return ServiceFactory.createProxy(BookService.class, bookService);
    }
}
