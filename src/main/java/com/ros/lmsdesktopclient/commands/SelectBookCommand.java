package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.models.SelectedBookModel;
import com.ros.lmsdesktopclient.services.service.StorageService;
import com.ros.lmsdesktopclient.util.Alerts;
import com.ros.lmsdesktopclient.util.Views;
import com.ros.lmsdesktopclient.util.exceptions.*;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.Objects;

public class SelectBookCommand extends Command{

    private final SelectedBookModel selectedBookModel;
    private final ObjectProperty<BookDisplayModel> selectedRowModel;
    private final StorageService storageService;
    private final Command openLoginViewCommand;

    public SelectBookCommand(SelectedBookModel selectedBookModel, ObjectProperty<BookDisplayModel> selectedRowModel, StorageService storageService) {
        this.selectedBookModel = selectedBookModel;
        this.selectedRowModel = selectedRowModel;
        this.storageService = storageService;
        this.openLoginViewCommand = new OpenViewCommand(Views.LOGIN);
        this.setOnCommandFailure(this::onFailure);
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws ServerErrorException, IOException, ExpiredSessionException, ImageNotFoundException, NetworkException {

                BookDisplayModel selected = selectedRowModel.get();

                Image coverImage = null;
                String imagePath = selected.getImagePath();

                if (imagePath != null && !imagePath.isBlank()) {
                    coverImage = storageService.getCoverImage(imagePath);
                }
                else {
                    coverImage = new Image(Objects.requireNonNull(getClass().getResource("/images/selected_book_placeholder.png")).toExternalForm());
                }

                Image finalCoverImage = coverImage; // must be effectively final for lambda

                Platform.runLater(() -> {
                    selectedBookModel.setIsbn(selected.getIsbn());
                    selectedBookModel.setTitle(selected.getTitle());
                    selectedBookModel.setAuthors(selected.getAuthors());
                    selectedBookModel.setGenres(selected.getGenres());
                    selectedBookModel.setStatus(selected.getStatus());
                    if (finalCoverImage != null) {
                        selectedBookModel.setCoverImage(finalCoverImage);
                    }
                });

                return null;
            }
        };
    }

    private void onFailure() {
        Throwable exception = getCommandTask().getException();

        Alerts alert = switch (exception){
            case NetworkException ignored -> Alerts.NETWORK_ERROR;
            case ServerErrorException ignored -> Alerts.SERVER_ERROR;
            case ExpiredSessionException ignored -> Alerts.EXPIRED_SESSION_ERROR;
            case ImageNotFoundException ignored -> Alerts.IMAGE_NOT_FOUND;
            default -> throw new IllegalStateException("Unexpected exception: " + exception);
        };
        String content = exception.getMessage();
        setAlert(alert);
        getAlert().getModal(content);

        if(exception instanceof ExpiredSessionException){
            openLoginViewCommand.execute();
        }
    }
}
