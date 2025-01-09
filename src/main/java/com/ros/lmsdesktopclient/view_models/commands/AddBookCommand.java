package com.ros.lmsdesktopclient.view_models.commands;

import com.ros.lmsdesktopclient.dtos.AuthorDTO;
import com.ros.lmsdesktopclient.dtos.AddBookDTO;
import com.ros.lmsdesktopclient.models.AuthorModel;
import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.services.service.AddBookService;
import com.ros.lmsdesktopclient.util.Alerts;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.Views;
import com.ros.lmsdesktopclient.util.exceptions.*;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AddBookCommand extends Command{

    private final BookModel book;
    private final List<AuthorModel> authors;
    private final AddBookService addBookService;

    public AddBookCommand(BookModel book, List<AuthorModel> authors, AddBookService addBookService){
        this.book = book;
        this.authors = authors;
        this.addBookService = addBookService;
        this.setOnCommandSuccess(this::onSuccess);
        this.setOnCommandFailure(this::onFailure);
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<>() {
            @Override
            protected Void call() throws EmptyFieldsException, InvalidISBNException, BookAlreadyExistException, ServerErrorException, ExpiredSessionException, NetworkException {
                checkForm(book, authors);
                Set<String> genres = book.getGenres()
                        .stream()
                        .map(StringProperty::get)
                        .map(String::toUpperCase)
                        .collect(Collectors.toSet());

                Set<AuthorDTO> authorDTOS = authors.stream()
                        .map(author -> new AuthorDTO(author.getFirstName().toUpperCase(), author.getLastName().toUpperCase()))
                        .collect(Collectors.toSet());

                AddBookDTO bookDTO = new AddBookDTO(Long.parseLong(book.getIsbn()), book.getTitle(), genres, authorDTOS);

                addBookService.addBook(bookDTO, HttpClient.newHttpClient());

                return null;
            }
        };
    }

    private void onSuccess(){
        setAlert(Alerts.BOOK_ADDED_SUCCESS);
        getAlert().getModal();
        //To reset the screen
        double width = ViewHandler.getInstance().getSceneWidth();
        double height = ViewHandler.getInstance().getSceneHeight();
        ViewHandler.switchTo(Views.ADD_BOOK.getView(), width, height);
    }

    private void onFailure(){

        Throwable exception = getCommandTask().getException();

        Alerts alert = switch (exception){
            case EmptyFieldsException e -> Alerts.EMPTY_FIELDS_WARN;
            case NetworkException e -> Alerts.NETWORK_ERROR;
            case ServerErrorException e -> Alerts.SERVER_ERROR;
            case InvalidISBNException e -> Alerts.INVALID_ISBN_ERROR;
            case ExpiredSessionException e -> Alerts.EXPIRED_SESSION_ERROR;
            case BookAlreadyExistException e -> Alerts.EXISTING_BOOK_ERROR;
            default -> throw new IllegalStateException("Unexpected exception: " + exception);
        };
        setAlert(alert);
        getAlert().getModal();

        if(exception instanceof ExpiredSessionException){
            //Redirect to log in screen
            double width = ViewHandler.getInstance().getSceneWidth();
            double height = ViewHandler.getInstance().getSceneHeight();
            ViewHandler.switchTo(Views.LOGIN.getView(), width, height);
        }
    }

    private void checkForm(BookModel bookModel, List<AuthorModel> authorModels) throws EmptyFieldsException {
        if (!bookModel.isComplete() || authorModels.isEmpty() || !authorModels.stream().allMatch(AuthorModel::isComplete)) {
            throw new EmptyFieldsException("Add Book Form: there are empty fields");
        }
    }
}
