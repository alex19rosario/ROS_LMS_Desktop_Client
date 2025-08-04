package com.ros.lmsdesktopclient.util.enums;

import com.ros.lmsdesktopclient.di.factories.ViewFactory;
import com.ros.lmsdesktopclient.views.*;

import java.util.function.Function;

public enum Views {
    LOGIN(ViewFactory::loginView),
    MAIN_MENU(ViewFactory::mainMenuView),
    ADD_BOOK(ViewFactory::addBookView),
    ADD_MEMBER(ViewFactory::addMemberView),
    ISSUE_BOOK(ViewFactory::issueBookView);

    private final Function<ViewFactory, BaseView> viewSupplier;

    Views(Function<ViewFactory, BaseView> viewSupplier) {
        this.viewSupplier = viewSupplier;
    }

    public BaseView getViewInstance(ViewFactory factory) {
        return viewSupplier.apply(factory);
    }
}
