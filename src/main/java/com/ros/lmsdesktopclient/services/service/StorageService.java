package com.ros.lmsdesktopclient.services.service;

import com.ros.lmsdesktopclient.util.exceptions.ExpiredSessionException;
import com.ros.lmsdesktopclient.util.exceptions.ImageNotFoundException;
import com.ros.lmsdesktopclient.util.exceptions.NetworkException;
import com.ros.lmsdesktopclient.util.exceptions.ServerErrorException;
import javafx.scene.image.Image;

import java.io.IOException;

public interface StorageService {

    Image getCoverImage(String filename) throws
            NetworkException,
            ServerErrorException,
            ExpiredSessionException,
            ImageNotFoundException,
            IOException;
}
