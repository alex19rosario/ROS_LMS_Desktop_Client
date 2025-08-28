package com.ros.lmsdesktopclient.di.modules;

import com.ros.lmsdesktopclient.di.factories.ServiceFactory;
import com.ros.lmsdesktopclient.services.service.*;
import com.ros.lmsdesktopclient.services.service_impl.*;
import com.ros.lmsdesktopclient.util.TokenHandler;
import dagger.Module;
import dagger.Provides;

import java.net.http.HttpClient;

@Module
public abstract class ServiceModule {

    @Provides
    static LoginService loginService(HttpClient client, TokenHandler tokenHandler) {
        LoginService loginService = new LoginServiceImpl(client, tokenHandler);
        return ServiceFactory.createProxy(LoginService.class, loginService);
    }

    @Provides
    static GenreService genreService(HttpClient client, TokenHandler tokenHandler) {
        GenreService genreService = new GenreServiceImpl(client, tokenHandler);
        return ServiceFactory.createProxy(GenreService.class, genreService);
    }

    @Provides
    static BookService bookService(HttpClient client, TokenHandler tokenHandler) {
        BookService bookService = new BookServiceImpl(client, tokenHandler);
        return ServiceFactory.createProxy(BookService.class, bookService);
    }

    @Provides
    static MemberService memberService(HttpClient client, TokenHandler tokenHandler) {
        MemberService memberService = new MemberServiceImpl(client, tokenHandler);
        return ServiceFactory.createProxy(MemberService.class, memberService);
    }

    @Provides
    static StorageService storageService(HttpClient client, TokenHandler tokenHandler) {
        StorageService storageService = new StorageServiceImpl(client, tokenHandler);
        return ServiceFactory.createProxy(StorageService.class, storageService);
    }

    @Provides
    static LoanService loanService(HttpClient client, TokenHandler tokenHandler) {
        LoanService loanService = new LoanServiceImpl(client, tokenHandler);
        return ServiceFactory.createProxy(LoanService.class, loanService);
    }
}
