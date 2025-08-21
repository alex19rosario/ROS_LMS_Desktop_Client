package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.models.SelectedBookModel;
import com.ros.lmsdesktopclient.services.service.StorageService;
import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.enums.Alerts;
import com.ros.lmsdesktopclient.util.exceptions.ExpiredSessionException;
import com.ros.lmsdesktopclient.util.exceptions.ImageNotFoundException;
import com.ros.lmsdesktopclient.util.exceptions.NetworkException;
import com.ros.lmsdesktopclient.util.exceptions.ServerErrorException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SelectBookCommandTest {

    @Mock
    private SelectedBookModel selectedBookModel;

    @Mock
    private StorageService storageService;

    @Mock
    private ExecutorService executorService;

    @Mock
    private UiExecutor javaFxUiExecutor;

    @Mock
    private Command openLoginViewCommand;

    private SimpleObjectProperty<BookDisplayModel> selectedRowModel;
    private SelectBookCommand command;

    private Image mockDefaultCoverImage;

    @BeforeEach
    void setUp() {
        selectedRowModel = new SimpleObjectProperty<>();
        mockDefaultCoverImage = mock(Image.class);
        command = new SelectBookCommand(
                selectedBookModel,
                selectedRowModel,
                storageService,
                executorService,
                javaFxUiExecutor,
                mockDefaultCoverImage,
                openLoginViewCommand
        );
    }

    @Test
    void runCommand_shouldSetBookDetailsWithImage() throws Exception {
        // Arrange
        BookDisplayModel book = createTestBook("path/to/image.jpg");
        selectedRowModel.set(book);

        Image mockImage = mock(Image.class);
        when(storageService.getCoverImage("path/to/image.jpg")).thenReturn(mockImage);

        // Act
        command.runCommand();

        // Verify UI thread execution
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(javaFxUiExecutor).runLater(runnableCaptor.capture());

        // Execute the runnable to verify its behavior
        runnableCaptor.getValue().run();
        verify(selectedBookModel).setIsbn("12345");
        verify(selectedBookModel).setTitle("Test Book");
        verify(selectedBookModel).setCoverImage(mockImage);
    }

    @Test
    void runCommand_shouldUsePlaceholderWhenNoImagePath() throws Exception {
        // Arrange
        BookDisplayModel book = createTestBook(null);
        selectedRowModel.set(book);

        // Act
        command.runCommand();

        // Verify placeholder image was used
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(javaFxUiExecutor).runLater(runnableCaptor.capture());

        runnableCaptor.getValue().run();
        verify(selectedBookModel).setCoverImage(mockDefaultCoverImage); // Verify some image was set
    }

    @Test
    void onFailure_shouldShowNetworkErrorAlert() {
        // Arrange
        SelectBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new NetworkException("Network down"));

        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        // Act
        spyCommand.onFailure();

        // Assert
        verify(spyCommand).setAlert(Alerts.NETWORK_ERROR);
        verify(mockAlert).getModal("Network down");
    }

    @Test
    void onFailure_shouldShowServerErrorAlert() {
        // Arrange
        SelectBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new ServerErrorException("Server unavailable"));

        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        // Act
        spyCommand.onFailure();

        // Assert
        verify(spyCommand).setAlert(Alerts.SERVER_ERROR);
        verify(mockAlert).getModal("Server unavailable");
    }

    @Test
    void onFailure_shouldShowImageNotFoundAlert() {
        // Arrange
        SelectBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new ImageNotFoundException("Image not found"));

        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        // Act
        spyCommand.onFailure();

        // Assert
        verify(spyCommand).setAlert(Alerts.IMAGE_NOT_FOUND);
        verify(mockAlert).getModal("Image not found");
    }

    @Test
    void onFailure_shouldShowExpiredSessionAlertAndOpenLoginView() {
        // Arrange
        SelectBookCommand spyCommand = spy(command);
        spyCommand.setLastException(new ExpiredSessionException("Session expired"));

        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        // Act
        spyCommand.onFailure();

        // Assert
        verify(spyCommand).setAlert(Alerts.EXPIRED_SESSION_ERROR);
        verify(mockAlert).getModal("Session expired");
        verify(openLoginViewCommand).execute();
    }

    @Test
    void onFailure_withUnexpectedException_shouldThrowIllegalStateException() {
        // Arrange
        command.setLastException(new RuntimeException("Unexpected error"));

        // Act & Assert
        assertThrows(IllegalStateException.class, command::onFailure);
    }

    @Test
    void runCommand_shouldStoreExceptionAndRethrow_whenImageNotFound() throws Exception {
        // Arrange
        BookDisplayModel book = mock(BookDisplayModel.class);
        when(book.getImagePath()).thenReturn("invalid/path.jpg");
        selectedRowModel.set(book);

        // Simulate storage service throwing ImageNotFoundException
        doThrow(new ImageNotFoundException("Image not found"))
                .when(storageService)
                .getCoverImage("invalid/path.jpg");

        // Act + Assert
        assertThrows(ImageNotFoundException.class, command::runCommand);

        // Verify the exception was stored
        assertInstanceOf(ImageNotFoundException.class, command.getLastException());
    }

    private BookDisplayModel createTestBook(String imagePath) {
        BookDisplayModel book = mock(BookDisplayModel.class);
        when(book.getIsbn()).thenReturn("12345");
        when(book.getTitle()).thenReturn("Test Book");
        when(book.getAuthors()).thenReturn("Author 1, Author 2");
        when(book.getGenres()).thenReturn("Genre 1, Genre 2");
        when(book.getStatus()).thenReturn("Available");
        when(book.getImagePath()).thenReturn(imagePath);
        return book;
    }

}
