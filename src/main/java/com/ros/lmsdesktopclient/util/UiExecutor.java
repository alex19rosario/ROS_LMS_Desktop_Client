package com.ros.lmsdesktopclient.util;

@FunctionalInterface
public interface UiExecutor {
    void runLater(Runnable runnable);
}
