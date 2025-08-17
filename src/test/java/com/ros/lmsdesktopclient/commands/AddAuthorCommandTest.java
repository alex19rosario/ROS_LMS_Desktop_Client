package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.JavaFxExtension;
import com.ros.lmsdesktopclient.models.AuthorModel;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@ExtendWith(JavaFxExtension.class)
class AddAuthorCommandTest {

    @Mock
    ExecutorService executorService;
    ListProperty<AuthorModel> authorList;
    AddAuthorCommand command;

    @BeforeEach
    void setup() {
        authorList = new SimpleListProperty<>(FXCollections.observableArrayList());
        command = new AddAuthorCommand(authorList, executorService);
    }

    @Test
    void runCommand_shouldAddNewAuthorModelToList() throws Exception {
        assertEquals(0, authorList.size());

        command.runCommand();

        // flush JavaFX runLater queue
        Platform.runLater(() -> {});
        Thread.sleep(50); // small wait to allow runLater to execute

        assertEquals(1, authorList.size());
    }
}
