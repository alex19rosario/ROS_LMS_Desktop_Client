package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.models.SearchBookModel;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

public class ClearFilterCommand extends Command{

    private final SearchBookModel searchBookModel;
    private final StringProperty isbn;

    public ClearFilterCommand(SearchBookModel searchBookModel, StringProperty isbn) {
        this.searchBookModel = searchBookModel;
        this.isbn = isbn;
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                searchBookModel.clear();
                isbn.setValue("");
                return null;
            }
        };
    }
}
