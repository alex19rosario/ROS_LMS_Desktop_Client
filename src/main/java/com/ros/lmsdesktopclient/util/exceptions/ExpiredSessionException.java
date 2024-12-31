package com.ros.lmsdesktopclient.util.exceptions;

public class ExpiredSessionException extends RuntimeException {
    public ExpiredSessionException(String message) {
        super(message);
    }
}
