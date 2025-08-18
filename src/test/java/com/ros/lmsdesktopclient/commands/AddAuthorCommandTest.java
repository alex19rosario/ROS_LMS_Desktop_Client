package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.JavaFxExtension;
import com.ros.lmsdesktopclient.models.AuthorModel;
import com.ros.lmsdesktopclient.util.UiExecutor;
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
    UiExecutor immediateUiExecutor;
    AddAuthorCommand command;

    @BeforeEach
    void setup() {
        authorList = new SimpleListProperty<>(FXCollections.observableArrayList());
        // Replace Platform.runLater with a direct call
        immediateUiExecutor = Runnable::run;

        command = new AddAuthorCommand(authorList, executorService, immediateUiExecutor);
    }

    @Test
    void runCommand_shouldAddNewAuthorModelToList() throws Exception {
        assertEquals(0, authorList.size());

        command.runCommand();

        assertEquals(1, authorList.size());
    }
}
