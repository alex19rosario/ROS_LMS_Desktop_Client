package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.di.factories.ImageFactory;
import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.util.UiExecutor;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SelectFileCommandTest {

    @Mock
    BookModel bookModel;

    @Mock
    Stage stage;

    @Mock
    FileChooser fileChooser;

    @Mock
    ExecutorService executorService;

    @Mock
    ImageFactory imageFactory;

    @Mock
    Image mockImage;

    UiExecutor immediateUiExecutor;
    SelectFileCommand command;

    @BeforeEach
    void setup() {
        when(fileChooser.getExtensionFilters()).thenReturn(FXCollections.observableArrayList());
        // Immediate executor runs tasks synchronously (no Platform.runLater needed)
        immediateUiExecutor = Runnable::run;
        command = new SelectFileCommand(bookModel, stage, fileChooser, executorService, immediateUiExecutor, imageFactory);
    }

    @Test
    void runCommand_shouldSetBookCoverImage_whenFileSelected() throws Exception {
        // Arrange
        File tempFile = File.createTempFile("test", ".png");
        tempFile.deleteOnExit();
        when(fileChooser.showOpenDialog(stage)).thenReturn(tempFile);
        when(imageFactory.create(tempFile)).thenReturn(mockImage);

        // Act
        command.runCommand();

        // Assert
        verify(bookModel).setCoverImage(mockImage);
        verify(bookModel).setCoverImageFile(tempFile);
    }

    @Test
    void runCommand_shouldDoNothing_whenNoFileSelected() throws Exception {
        // Arrange
        when(fileChooser.showOpenDialog(stage)).thenReturn(null);

        // Act
        command.runCommand();

        // Assert
        verify(bookModel, never()).setCoverImage(any());
        verify(bookModel, never()).setCoverImageFile(any());
    }
}
