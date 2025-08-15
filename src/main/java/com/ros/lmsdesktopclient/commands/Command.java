package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.util.enums.Alerts;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.concurrent.ExecutorService;

public abstract class Command {
    private final DoubleProperty progress;
    private final BooleanProperty running;
    private Runnable onCommandSuccess;
    private Runnable onCommandFailure;
    private final ExecutorService executorService;
    private Alerts alert;

    public Command(ExecutorService executorService) {
        this.executorService = executorService;
        this.progress = new SimpleDoubleProperty();
        this.running = new SimpleBooleanProperty();
    }

    protected abstract void runCommand() throws Exception;

    public void setOnCommandSuccess(Runnable onCommandSuccess){
        this.onCommandSuccess = onCommandSuccess;
    }

    public void setOnCommandFailure(Runnable onCommandFailure){
        this.onCommandFailure = onCommandFailure;
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public BooleanProperty runningProperty() {
        return running;
    }

    public Alerts getAlert() {
        return alert;
    }

    public void setAlert(Alerts alert) {
        this.alert = alert;
    }

    public void execute() {
        running.set(true);
        progress.set(-1); // show indeterminate spinner

        executorService.submit(() -> {
            try {
                runCommand();
                // Success: run callback on JavaFX thread
                Platform.runLater(() -> {
                    running.set(false);
                    progress.set(1); // completed
                    if (onCommandSuccess != null) {
                        onCommandSuccess.run();
                    }
                });

            } catch (Exception e) {

                // Failure: run callback on JavaFX thread
                Platform.runLater(() -> {
                    running.set(false);
                    progress.set(0); // failed, reset to 0
                    if (onCommandFailure != null) {
                        onCommandFailure.run();
                    }
                });
            }
        });
    }

    // Optional helper for reporting progress from runCommand()
    protected void updateProgress(double value) {
        Platform.runLater(() -> progress.set(value));
    }
}
