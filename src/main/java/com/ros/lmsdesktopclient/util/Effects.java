package com.ros.lmsdesktopclient.util;

import javafx.scene.Node;

public class Effects {
    public static void applyHover(Node component, String style, long milliseconds){
        component.setStyle(style);
        new Thread(() -> {
            try {
                Thread.sleep(150); // 150ms delay for the effect
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            javafx.application.Platform.runLater(() -> component.setStyle("")); // Reset style
        }).start();
    }
}
