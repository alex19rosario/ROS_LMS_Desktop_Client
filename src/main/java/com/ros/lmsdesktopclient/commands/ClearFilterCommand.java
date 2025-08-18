package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.models.SearchBookModel;
import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.enums.Alerts;
import com.ros.lmsdesktopclient.util.enums.PropertyType;
import com.ros.lmsdesktopclient.util.exceptions.*;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ExecutorService;


public class ClearFilterCommand extends Command{

    private final StringProperty isbn;
    private final SearchBookModel searchBookModel;
    private Throwable lastException;

    @Inject
    public ClearFilterCommand(
            Map<PropertyType, Property> properties,
            SearchBookModel searchBookModel,
            ExecutorService executorService,
            UiExecutor uiExecutor
    ) {
        super(executorService, uiExecutor);
        this.isbn = (StringProperty) properties.get(PropertyType.ISBN);
        this.searchBookModel = searchBookModel;
        this.setOnCommandFailure(this::onFailure);
    }

    @Override
    protected void runCommand() throws Exception {
        try {
            if (!searchBookModel.isComplete() && (isbn.get() == null || isbn.get().isBlank())) {
                throw new AlreadyClearedException("The search form is already cleared.");
            }
            getUiExecutor().runLater(() -> {
                searchBookModel.clear();
                isbn.setValue("");
            });
        } catch (Exception ex) {
            this.lastException = ex;
            throw ex; // triggers failure in base class
        }
    }

    void onFailure() {
        if(lastException != null) {
            Alerts alert = Alerts.ALREADY_CLEARED_ERROR;
            setAlert(alert);
            getAlert().getModal(lastException.getMessage());
        }
    }

    public Throwable getLastException() {
        return lastException;
    }

    public void setLastException(Throwable lastException) {
        this.lastException = lastException;
    }
}
