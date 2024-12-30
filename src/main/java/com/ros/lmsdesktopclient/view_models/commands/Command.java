package com.ros.lmsdesktopclient.view_models.commands;

import com.ros.lmsdesktopclient.util.Alerts;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Command<T> {
    private final DoubleProperty progress;
    private final BooleanProperty running;
    private Runnable onCommandSuccess;
    private Runnable onCommandFailure;
    private static final ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
    private Task<T> commandTask;
    private Alerts alert;


    public Command() {
        this.progress = new SimpleDoubleProperty();
        this.running = new SimpleBooleanProperty();
    }

    protected abstract Task<T> createCommandTask();

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

    public Task<T> getCommandTask() {
        return commandTask;
    }

    public Alerts getAlert() {
        return alert;
    }

    public void setAlert(Alerts alert) {
        this.alert = alert;
    }

    public void execute(){
        this.commandTask = createCommandTask();

        running.bind(commandTask.runningProperty());
        progress.bind(commandTask.progressProperty());

        pool.submit(commandTask);

        commandTask.setOnSucceeded(event -> {
            if (onCommandSuccess != null) {
                onCommandSuccess.run();
            }
        });

        commandTask.setOnFailed(event -> {
            if (onCommandFailure != null) {
                onCommandFailure.run();
            }
        });
    }


}
