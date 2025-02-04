package com.ros.lmsdesktopclient.util;

public enum Views {
    LOGIN("views/login-view.fxml"),
    MAIN_MENU("views/main-menu-view.fxml"),
    ADD_BOOK("views/add-book-view.fxml"),
    ADD_MEMBER("views/add-member-view.fxml"),
    ISSUE_BOOK("views/issue-book-view.fxml");

    private final String view;

    Views(String view) {
        this.view = view;
    }

    public String getView(){
        return this.view;
    }
}
