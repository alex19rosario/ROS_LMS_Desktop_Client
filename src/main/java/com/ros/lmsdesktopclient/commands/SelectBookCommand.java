package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.models.SelectedBookModel;
import com.ros.lmsdesktopclient.services.service.StorageService;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.Alerts;
import com.ros.lmsdesktopclient.util.enums.ViewType;
import com.ros.lmsdesktopclient.util.exceptions.*;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;

import javax.inject.Inject;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

public class SelectBookCommand extends Command{

    private final SelectedBookModel selectedBookModel;
    private final ObjectProperty<BookDisplayModel> selectedRowModel;
    private final StorageService storageService;
    private final Command openLoginViewCommand;
    private Throwable lastException;

    @Inject
    public SelectBookCommand(
            SelectedBookModel selectedBookModel,
            ObjectProperty<BookDisplayModel> selectedRowModel,
            StorageService storageService,
            ExecutorService executorService,
            ViewHandler viewHandler
    ) {
        super(executorService);
        this.selectedBookModel = selectedBookModel;
        this.selectedRowModel = selectedRowModel;
        this.storageService = storageService;
        this.openLoginViewCommand = new OpenViewCommand(ViewType.LOGIN, executorService, viewHandler);
        this.setOnCommandFailure(this::onFailure);
    }

    @Override
    protected void runCommand() throws Exception {
        try {
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
        } catch (Exception ex) {
            this.lastException = ex;
            throw ex; // triggers failure in base class
        }
    }

    private void onFailure() {

        if(lastException != null) {
            Alerts alert = switch (lastException){
                case NetworkException ignored -> Alerts.NETWORK_ERROR;
                case ServerErrorException ignored -> Alerts.SERVER_ERROR;
                case ExpiredSessionException ignored -> Alerts.EXPIRED_SESSION_ERROR;
                case ImageNotFoundException ignored -> Alerts.IMAGE_NOT_FOUND;
                default -> throw new IllegalStateException("Unexpected exception: " + lastException);
            };
            setAlert(alert);
            getAlert().getModal(lastException.getMessage());
        }

        if(lastException instanceof ExpiredSessionException){
            openLoginViewCommand.execute();
        }
    }

}
