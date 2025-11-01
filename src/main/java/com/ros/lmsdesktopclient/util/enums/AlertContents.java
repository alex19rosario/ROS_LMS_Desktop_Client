package com.ros.lmsdesktopclient.util.enums;

public enum AlertContents {
    MEMBER_ADDED_OK("The member was added successfully"),
    BOOK_ADDED_OK("The book was added successfully"),
    BOOK_ISSUED_OK("The book was issued successfully"),
    BOOK_RETURNED_OK("The book was returned successfully");

    private final String value;

    AlertContents(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
