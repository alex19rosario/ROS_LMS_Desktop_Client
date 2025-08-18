package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.util.UiExecutor;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.File;
import java.util.concurrent.ExecutorService;

public class SelectFileCommand extends Command{

    private final BookModel book;
    private final Stage stage;
    private final FileChooser fileChooser;

    @Inject
    public SelectFileCommand(
            BookModel book,
            Stage stage,
            FileChooser fileChooser,
            ExecutorService executorService,
            UiExecutor uiExecutor
    ){
        super(executorService, uiExecutor);
        this.book = book;
        this.stage = stage;
        this.fileChooser = fileChooser;
    }

    @Override
    protected void runCommand() throws Exception {
        getUiExecutor().runLater(() -> {
            fileChooser.setTitle("Select Resource");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));

            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                Image image = new Image(selectedFile.toURI().toString());
                book.setCoverImage(image);
                book.setCoverImageFile(selectedFile);
            }
        });
    }
}
