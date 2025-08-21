package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.models.SearchBookModel;
import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.enums.Alerts;
import com.ros.lmsdesktopclient.util.enums.PropertyType;
import com.ros.lmsdesktopclient.util.exceptions.AlreadyClearedException;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ClearFilterCommandTest {

    @Mock
    private SearchBookModel searchBookModel;

    @Mock
    private ExecutorService executorService;

    @Mock
    private UiExecutor javaFxUiExecutor;

    private ClearFilterCommand command;
    private StringProperty isbnProperty;
    private Map<PropertyType, Property> properties;


    @BeforeEach
    void setUp() {
        isbnProperty = new SimpleStringProperty("12345");
        properties = new HashMap<>();
        properties.put(PropertyType.ISBN, isbnProperty);

        command = new ClearFilterCommand(properties, searchBookModel, executorService, javaFxUiExecutor);
    }

    @Test
    void execute_ShouldClearFiltersWhenFormIsNotCleared() throws Exception {
        // Arrange
        when(searchBookModel.isComplete()).thenReturn(true);

        // Act
        command.runCommand();

        // Assert
        // Verify UI thread execution
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(javaFxUiExecutor).runLater(runnableCaptor.capture());

        // Execute the runnable to verify its behavior
        runnableCaptor.getValue().run();
        verify(searchBookModel).clear();
        assertEquals("", isbnProperty.get());
    }

    @Test
    void execute_ShouldThrowExceptionWhenFormIsAlreadyCleared() {
        // Arrange
        when(searchBookModel.isComplete()).thenReturn(false);
        isbnProperty.set(""); // already cleared

        // Act & Assert
        Exception exception = assertThrows(AlreadyClearedException.class, () -> command.runCommand());
        assertEquals("The search form is already cleared.", exception.getMessage());

        // Verify no UI updates were attempted
        verify(javaFxUiExecutor, never()).runLater(any());
    }

    @Test
    void onFailure_ShouldNotSetAlertWhenNoExceptionOccurred() {
        // Arrange
        command.setLastException(null);

        // Act
        command.onFailure();

        // Assert
        assertNull(command.getAlert());
    }

    @Test
    void execute_ShouldSetLastExceptionWhenFailureOccurs() {
        // Arrange
        when(searchBookModel.isComplete()).thenReturn(false);
        isbnProperty.set(""); // already cleared

        // Act
        try {
            command.runCommand();
            fail("Expected AlreadyClearedException");
        } catch (Exception e) {
            // Expected
        }

        // Assert
        assertNotNull(command.getLastException());
        assertInstanceOf(AlreadyClearedException.class, command.getLastException());
    }

    @Test
    void onFailure_shouldShowAlreadyClearedAlert() {
        // Arrange
        ClearFilterCommand spyCommand = Mockito.spy(command);

        String errorMessage = "The search form is already cleared.";
        spyCommand.setLastException(new AlreadyClearedException(errorMessage));

        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        // Act
        spyCommand.onFailure();

        // Assert
        verify(spyCommand).setAlert(Alerts.ALREADY_CLEARED_ERROR);
        verify(mockAlert).getModal(errorMessage);
    }

    @Test
    void onFailure_withUnexpectedException_shouldSetAlert() {
        // Arrange
        ClearFilterCommand spyCommand = spy(command);

        RuntimeException unexpected = new RuntimeException("Unexpected error");
        spyCommand.setLastException(unexpected);

        Alerts mockAlert = mock(Alerts.class);
        doReturn(mockAlert).when(spyCommand).getAlert();

        // Act
        spyCommand.onFailure();

        // Assert
        verify(spyCommand).setAlert(Alerts.ALREADY_CLEARED_ERROR);
        verify(mockAlert).getModal("Unexpected error");
    }

}
