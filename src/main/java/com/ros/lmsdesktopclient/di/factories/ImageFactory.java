package com.ros.lmsdesktopclient.di.factories;

import javafx.scene.image.Image;

import java.io.File;

@FunctionalInterface
public interface ImageFactory {
    Image create(File file);
}
