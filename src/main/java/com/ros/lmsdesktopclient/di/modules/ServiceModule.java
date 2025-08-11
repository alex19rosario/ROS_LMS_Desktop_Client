package com.ros.lmsdesktopclient.di.modules;

import com.ros.lmsdesktopclient.di.factories.AuthenticatedHttpClientFactory;
import com.ros.lmsdesktopclient.di.factories.ServiceFactory;
import com.ros.lmsdesktopclient.services.service.*;
import com.ros.lmsdesktopclient.services.service_impl.*;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import java.net.http.HttpClient;

@Module
public abstract class ServiceModule {

    @Provides
    static LoginService loginService(AuthenticatedHttpClientFactory factory) {
        LoginService loginService = new LoginServiceImpl(factory);
        return ServiceFactory.createProxy(LoginService.class, loginService);
    }

    @Provides
    static GenreService genreService(HttpClient client) {
        GenreService genreService = new GenreServiceImpl(client);
        return ServiceFactory.createProxy(GenreService.class, genreService);
    }

    @Provides
    static BookService bookService(HttpClient client) {
        BookService bookService = new BookServiceImpl(client);
        return ServiceFactory.createProxy(BookService.class, bookService);
    }

    @Provides
    static MemberService memberService(HttpClient client) {
        MemberService memberService = new MemberServiceImpl(client);
        return ServiceFactory.createProxy(MemberService.class, memberService);
    }

    @Provides
    static StorageService storageService(HttpClient client) {
        StorageService storageService = new StorageServiceImpl(client);
        return ServiceFactory.createProxy(StorageService.class, storageService);
    }

    @Provides
    static LoanService loanService(HttpClient client) {
        LoanService loanService = new LoanServiceImpl(client);
        return ServiceFactory.createProxy(LoanService.class, loanService);
    }
}
