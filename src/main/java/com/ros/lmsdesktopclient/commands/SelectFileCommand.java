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
    private final UiExecutor javaFxUiExecutor;

    @Inject
    public SelectFileCommand(
            BookModel book,
            Stage stage,
            FileChooser fileChooser,
            ExecutorService executorService,
            UiExecutor javaFxUiExecutor
    ){
        super(executorService);
        this.book = book;
        this.stage = stage;
        this.fileChooser = fileChooser;
        this.javaFxUiExecutor = javaFxUiExecutor;
    }

    @Override
    protected void runCommand() throws Exception {
        javaFxUiExecutor.runLater(() -> {
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
