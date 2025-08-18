package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.JavaFxExtension;
import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.enums.Alerts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(JavaFxExtension.class)
class CommandTest {

    // Minimal concrete command for testing
    static class TestCommand extends Command {
        private final boolean shouldFail;
        TestCommand(ExecutorService executorService, UiExecutor uiExecutor, boolean shouldFail) {
            super(executorService, uiExecutor);
            this.shouldFail = shouldFail;
        }
        @Override
        protected void runCommand() throws Exception {
            if (shouldFail) throw new RuntimeException("boom");
            // simulate some progress
            updateProgress(0.5);
        }
    }

    private ExecutorService inlineExecutorMock() {
        ExecutorService exec = mock(ExecutorService.class);
        when(exec.submit(any(Runnable.class))).thenAnswer(inv -> {
            Runnable r = inv.getArgument(0);
            r.run(); // run immediately, same thread
            return CompletableFuture.completedFuture(null); // already "done" Future
        });
        return exec;
    }

    private UiExecutor inlineUiExecutorMock() {
        UiExecutor ui = mock(UiExecutor.class);
        // runLater executes immediately in tests
        doAnswer(inv -> { Runnable r = inv.getArgument(0); r.run(); return null; })
                .when(ui).runLater(any(Runnable.class));
        return ui;
    }

    @Test
    void execute_success_invokesSuccessCallback_and_setsProgressAndRunning() {
        ExecutorService exec = inlineExecutorMock();
        UiExecutor ui = inlineUiExecutorMock();

        TestCommand cmd = new TestCommand(exec, ui, false);

        AtomicBoolean success = new AtomicBoolean(false);
        cmd.setOnCommandSuccess(() -> success.set(true));

        // preconditions
        assertFalse(cmd.runningProperty().get());
        assertEquals(0.0, cmd.progressProperty().get(), 1e-9);

        cmd.execute();

        // everything runs inline, so we can assert immediately
        assertTrue(success.get());
        assertFalse(cmd.runningProperty().get());
        assertEquals(1.0, cmd.progressProperty().get(), 1e-9);

        // runLater used twice: once by updateProgress(0.5), once by success block
        verify(ui, times(2)).runLater(any(Runnable.class));
        verify(exec, times(1)).submit(any(Runnable.class));
    }

    @Test
    void execute_failure_invokesFailureCallback_and_setsProgressAndRunning() {
        ExecutorService exec = inlineExecutorMock();
        UiExecutor ui = inlineUiExecutorMock();

        TestCommand cmd = new TestCommand(exec, ui, true);

        AtomicBoolean failure = new AtomicBoolean(false);
        cmd.setOnCommandFailure(() -> failure.set(true));

        cmd.execute();

        assertTrue(failure.get());
        assertFalse(cmd.runningProperty().get());
        assertEquals(0.0, cmd.progressProperty().get(), 1e-9);

        // failure path uses runLater once
        verify(ui, times(1)).runLater(any(Runnable.class));
        verify(exec, times(1)).submit(any(Runnable.class));
    }

    @Test
    void updateProgress_updatesProperty_viaUiExecutor() {
        UiExecutor ui = inlineUiExecutorMock();

        TestCommand cmd = new TestCommand(null, ui, false);

        cmd.updateProgress(0.42);

        assertEquals(0.42, cmd.progressProperty().get(), 1e-9);
        verify(ui, times(1)).runLater(any(Runnable.class));
    }

    @Test
    void setGetAlert_roundTrip() {
        TestCommand cmd = new TestCommand(null, null, false);

        cmd.setAlert(Alerts.BOOK_ADDED_SUCCESS);

        assertEquals(Alerts.BOOK_ADDED_SUCCESS, cmd.getAlert());
    }

}
