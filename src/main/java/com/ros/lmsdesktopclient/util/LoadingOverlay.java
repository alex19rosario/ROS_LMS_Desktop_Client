package com.ros.lmsdesktopclient.util;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class LoadingOverlay {

    public static Region wrap(Node content, ProgressIndicator progressIndicator) {

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(progressIndicator);

        Color semiTransparentGray = new Color(0.83, 0.83, 0.83, 0.5);

        borderPane.setBackground(new Background(new BackgroundFill(
                semiTransparentGray, CornerRadii.EMPTY, Insets.EMPTY
        )));

        borderPane.visibleProperty().bind(progressIndicator.visibleProperty());

        StackPane root = new StackPane(content, borderPane);

        return root;
    }
}
