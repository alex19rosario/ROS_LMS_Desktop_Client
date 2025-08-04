package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.models.BookModel;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.File;

public class SelectFileCommand extends Command{

    private final BookModel book;
    private final Stage stage;

    @Inject
    public SelectFileCommand(BookModel book, Stage stage){
        this.book = book;
        this.stage = stage;
    }
    @Override
    protected Task<Void> createCommandTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                javafx.application.Platform.runLater(() -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Select Resource");
                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));

                    File selectedFile = fileChooser.showOpenDialog(stage);
                    if (selectedFile != null) {
                        Image image = new Image(selectedFile.toURI().toString());
                        book.setCoverImage(image);
                        book.setCoverImageFile(selectedFile);
                    }
                });
                return null;
            }
        };
    }
}
