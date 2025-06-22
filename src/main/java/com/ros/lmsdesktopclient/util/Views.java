package com.ros.lmsdesktopclient.util;

import com.ros.lmsdesktopclient.views.*;

import java.util.function.Supplier;

public enum Views {
    LOGIN(LoginView::new),
    MAIN_MENU(MainMenuView::new),
    ADD_BOOK(AddBookView::new),
    ADD_MEMBER(AddMemberView::new);

    private final Supplier<BaseView> viewSupplier;

    Views(Supplier<BaseView> viewSupplier) {
        this.viewSupplier = viewSupplier;
    }

    public BaseView getViewInstance() {
        return viewSupplier.get();
    }

}
