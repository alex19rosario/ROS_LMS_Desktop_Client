package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.models.SearchBookModel;
import com.ros.lmsdesktopclient.util.enums.Alerts;
import com.ros.lmsdesktopclient.util.enums.PropertyType;
import com.ros.lmsdesktopclient.util.exceptions.*;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import javax.inject.Inject;
import java.util.Map;


public class ClearFilterCommand extends Command{

    private final StringProperty isbn;
    private final SearchBookModel searchBookModel;

    @Inject
    public ClearFilterCommand(Map<PropertyType, Property> properties, SearchBookModel searchBookModel) {
        this.isbn = (StringProperty) properties.get(PropertyType.ISBN);
        this.searchBookModel = searchBookModel;
        this.setOnCommandFailure(this::onFailure);
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception{

                if (!searchBookModel.isComplete() && (isbn.get() == null || isbn.get().isBlank())) {
                    throw new AlreadyClearedException("The search form is already cleared.");
                }
                Platform.runLater(() -> {
                    searchBookModel.clear();
                    isbn.setValue("");
                });
                return null;
            }
        };
    }

    private void onFailure() {
        Throwable exception = getCommandTask().getException();
        Alerts alert = Alerts.ALREADY_CLEARED_ERROR;
        String content = exception.getMessage();
        setAlert(alert);
        getAlert().getModal(content);
    }
}
